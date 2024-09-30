package com.example.backend.domain.fetch.service;

import com.example.backend.domain.fetch.dto.request.FetchRegisterRequestDto;
import com.example.backend.domain.fetch.dto.response.FetchRegisterResponseDto;
import com.example.backend.domain.fetch.entity.Fetch;
import com.example.backend.domain.fetch.repository.FetchRepository;
import com.example.backend.domain.member.entity.Member;
import com.example.backend.domain.member.entity.MemberFetch;
import com.example.backend.domain.member.repository.MemberFetchRepository;
import com.example.backend.domain.member.repository.MemberRepository;
import com.example.backend.global.error.BusinessException;
import com.example.backend.global.error.ErrorCode;
import com.example.backend.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FetchServiceImpl implements FetchService {

    private final FetchRepository fetchRepository;
    private final MemberRepository memberRepository;
    private final MemberFetchRepository memberFetchRepository;
    private final CookieUtil cookieUtil;

    /**
     * fetch 등록
     * @param requestDto
     * @param request
     * @return
     */

    @Transactional
    public FetchRegisterResponseDto registerFetch(FetchRegisterRequestDto requestDto, HttpServletRequest request) {
        log.info("RegisterFetch request : {} ", requestDto);
        long memberId = cookieUtil.getUserId(request);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        String fetchSerialNumber = requestDto.getFetchSerialNumber();
        String nickname = requestDto.getNickname();


        Optional<Fetch> existFetch = fetchRepository.findByFetchSerialNumber(fetchSerialNumber);


        if(existFetch.isPresent()) {

            Fetch fetch = existFetch.get();
            Optional<MemberFetch> existMemberFetch = memberFetchRepository.findByFetchAndMember(fetch, member);

            if(existMemberFetch.isPresent()) {

                throw new BusinessException(ErrorCode.FETCH_DUPLICATED);

            } else {


                MemberFetch memberFetch = MemberFetch.builder()
                        .member(member)
                        .fetch(fetch)
                        .build();

                memberFetchRepository.save(memberFetch);
            }
        }else {
            Fetch fetch = Fetch.builder()
                    .fetchSerialNumber(fetchSerialNumber)
                    .nickname(nickname)
                    .build();

            fetchRepository.save(fetch);

            MemberFetch memberFetch = MemberFetch.builder()
                    .member(member)
                    .fetch(fetch)
                    .build();

            memberFetchRepository.save(memberFetch);

            }

        return FetchRegisterResponseDto.builder()
                .memberId(memberId)
                .fetchSerialNumber(fetchSerialNumber)
                .nickname(nickname)
                .build();
    }

    @Transactional
    public void deleteFetch(Long fetchId, HttpServletRequest request) {
        log.info("Deleting fetch request");
        long memberId = cookieUtil.getUserId(request);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));


        Optional<Fetch> fetch = fetchRepository.findById(fetchId);


        if(!fetch.isPresent()) {
            throw new BusinessException(ErrorCode.FETCH_NOT_FOUND);
        } else {
            Optional<MemberFetch> memberFetch = memberFetchRepository.findByFetchAndMember(fetch.get(), member);
            memberFetchRepository.delete(memberFetch.get());
        }
    }

}
