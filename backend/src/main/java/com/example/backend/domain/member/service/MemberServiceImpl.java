package com.example.backend.domain.member.service;

import com.example.backend.domain.member.dto.request.CustomUserInfoDto;
import com.example.backend.domain.member.dto.request.LoginRequestDto;
import com.example.backend.domain.member.dto.request.SignupRequestDto;
import com.example.backend.domain.member.dto.response.LoginResponseDto;
import com.example.backend.domain.member.entity.Member;
import com.example.backend.domain.member.repository.MemberRepository;
import com.example.backend.global.error.BusinessException;
import com.example.backend.global.error.ErrorCode;
import com.example.backend.global.result.ResultCode;
import com.example.backend.global.util.CookieUtil;
import com.example.backend.global.util.JwtUtil;
import io.micrometer.observation.annotation.Observed;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.constraintvalidators.hv.time.DurationMaxValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.Duration;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final MemberRedisService memberRedisService;
    private final PasswordEncoder passwordEncoder;

    private static final String AUTH_CODE_PREFIX = "AuthCode:";
    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;


    @Value("${spring.mail.auth-code-expiration-millis}")
    private Long authCodeExpirationMillis;

    /**
     * 회원 가입
     *
     * @param signupRequestDto
     * @return
     * @throws BusinessException
     */
    @Override
    @Transactional
    public void signup(SignupRequestDto signupRequestDto) throws BusinessException {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String nickname = signupRequestDto.getNickname();
        String email = signupRequestDto.getEmail();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());

        Optional<Member> memberNickname = memberRepository.findByNickname(nickname);
        if (memberNickname.isPresent()) {
            throw new BusinessException(ErrorCode.MEMBER_DUPLICATED);
        }

        Optional<Member> memberEmail = memberRepository.findByEmail(email);
        if (memberEmail.isPresent()) {
            throw new BusinessException(ErrorCode.MEMBER_DUPLICATED);
        }

        Member member = Member.builder()
                .nickname(nickname)
                .email(email)
                .password(password)
                .build();

        memberRepository.save(member);

    }

    /**
     * 이메일 인증 번호 랜덤 생성 메서드
     *
     * @return
     */
    private String createCode() {
        Random random = new Random();
        StringBuilder randomCode = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            randomCode.append(random.nextInt(10));
        }

        return randomCode.toString();
    }

    /**
     * 이메일 인증 번호 발송 메서드
     *
     * @param email
     * @return
     */
    @Override
    public void sendCodeToEmail(String email) {
        Optional<Member> memberEmail = memberRepository.findByEmail(email);
        if (memberEmail.isPresent()) {
            throw new BusinessException(ErrorCode.MEMBER_DUPLICATED);
        }

        String code = createCode();
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject("[RestFetch] 인증 번호입니다.");

            mimeMessageHelper.setText(setContext(code), true);
            mailSender.send(mimeMessage);

            log.info("success");

        } catch (MessagingException e) {
            log.info("fail");
            throw new BusinessException(ErrorCode.EMAIL_SEND_FAIL);
        }

        // 이미 인증 번호를 보내서 레디스 서버에 인증번호가 있음
        String redisCode = memberRedisService.getValues(AUTH_CODE_PREFIX + email);
        if (redisCode != null) {
            memberRedisService.deleteValue(AUTH_CODE_PREFIX + email);
        }
        memberRedisService.setValue(AUTH_CODE_PREFIX + email, code, Duration.ofMillis(authCodeExpirationMillis));
    }

    /**
     * thymeleaf 사용
     *
     * @param code
     * @return
     */
    private String setContext(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("email", context);

    }

    /**
     * 인증번호 확인 메서드
     *
     * @param email
     * @param authCode
     * @return
     */
    @Override
    public void verifyCode(String email, String authCode) {
        log.info("verify emails {}", email);
        String redisAuthCode = memberRedisService.getValues((AUTH_CODE_PREFIX + email));
        log.info("redis get pinNumber : {} ", redisAuthCode);
        if (Integer.parseInt(redisAuthCode) != Integer.parseInt(authCode)) {
            throw new BusinessException(ErrorCode.MEMBER_INVALID_CODE);
        }
    }


    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) throws BadCredentialsException, UsernameNotFoundException {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();
        log.info(email);
        Member member = memberRepository.findByEmail(email).orElseThrow(
                () -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND)
        );

        String nickname = member.getNickname();

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new BusinessException(ErrorCode.MEMBER_DIFF_PASSWORD);
        }

        log.info("[MemberServiceImpl - login] loginReqDto : {}", loginRequestDto.getEmail());

        CustomUserInfoDto info = CustomUserInfoDto.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .password(member.getPassword())
                .nickname(member.getNickname())
                .build();

        String accessToken = jwtUtil.createAccessToken(info);
        String refreshToken = jwtUtil.createRefreshToken(info);

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(3600);

        String storedRefreshToken = memberRedisService.getRefreshToken(member.getMemberId());

        if (storedRefreshToken != null) {
            memberRedisService.deleteRefreshToken(member.getMemberId());
        }

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(604800);

        memberRedisService.saveRefreshToken(String.valueOf(member.getMemberId()), refreshToken, 7, TimeUnit.DAYS);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        LoginResponseDto loginResponseDto = LoginResponseDto
                .builder()
                .memberId(member.getMemberId())
                .email(email)
                .nickname(nickname)
                .build();
        return loginResponseDto;
    }

    public void logout(HttpServletRequest request) {
        String email = cookieUtil.getMemberEmail(request);
        memberRedisService.deleteValue(AUTH_CODE_PREFIX + email);
    }

//    /**
//     * 엑세스 토큰 발급
//     * 리프레시 토큰만 있는 상황
//     *
//     * @param loginReqDto
//     * @return
//     */
//
////    public Cookie createAccessToken(LoginRequestDto loginReqDto, HttpServletResponse response, HttpServletRequest request) {
////
////        // 클라이언트에서 받은 토큰
////        String refreshTokenReq = null;
////        Cookie[] cookies = request.getCookies();
////
////        if (cookies == null) { //토큰이 없을때
////            throw new BusinessException(ErrorCode.MEMBER_COOKIE_NOT_FOUND);
////        } else { // 받은 쿠키들 중에서 refreshToken 찾기
////            for (Cookie cookie : cookies) {
////                if ("refreshToken".equals(cookie.getName())) {
////                    refreshTokenReq = cookie.getValue();
////                    break;
////                }
////            }
////        }
////
////        // 토큰에서 맴버 아이디 추출
////        Long memberIdFromToken = jwtUtil.getMemberId(refreshTokenReq);
////
////        // 레디스에서 받은 토큰 에서 맴버 아이디 추출
////        String memberIdToken = memberRedisService.getRefreshToken(memberIdFromToken);
////
////        // 토큰 인증하고, 두개의 토큰이 같으면
////        if (refreshTokenReq.equals(memberIdToken) && jwtUtil.validateToken(refreshTokenReq) && jwtUtil.validateToken(memberIdToken)) {
////
////            Member member = memberRepository.findByMemberId(memberIdFromToken).orElseThrow(
////                    () -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND)
////            );
////
////            CustomUserInfoDto info = CustomUserInfoDto.builder()
////                    .memberId(member.getMemberId())
////                    .email(member.getEmail())
////                    .password(member.getPassword())
////                    .nickname(member.getNickname())
////                    .build();
////
////            String accessToken = jwtUtil.createAccessToken(info);
////
////            Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
////            accessTokenCookie.setHttpOnly(true);
////            accessTokenCookie.setSecure(true);
////            accessTokenCookie.setPath("/");
////            accessTokenCookie.setMaxAge(60 * 60); // 1시간
////
////            return accessTokenCookie;
////        }
////
////        throw new BusinessException(ErrorCode.COMMON_ETC);
////    }
}
