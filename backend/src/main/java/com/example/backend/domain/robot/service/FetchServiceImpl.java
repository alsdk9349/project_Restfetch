package com.example.backend.domain.robot.service;

import com.example.backend.domain.robot.dto.request.FetchRegisterRequestDto;
import com.example.backend.domain.robot.dto.response.FetchGetResponseDto;
import com.example.backend.domain.robot.dto.response.FetchRegisterResponseDto;
import com.example.backend.domain.robot.entity.Fetch;
import com.example.backend.domain.robot.repository.FetchRepository;
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

import java.util.ArrayList;
import java.util.List;
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
            log.info("2{}", fetch);

            fetchRepository.save(fetch);

            MemberFetch memberFetch = MemberFetch.builder()
                    .member(member)
                    .fetch(fetch)
                    .build();

            memberFetchRepository.save(memberFetch);

            }

        Optional<Fetch> fetch = fetchRepository.findByFetchSerialNumber(fetchSerialNumber);
        Long fetchId = fetch.get().getFetchId();

        return FetchRegisterResponseDto.builder()
                .fetchId(fetchId)
                .fetchSerialNumber(fetchSerialNumber)
                .nickname(nickname)
                .build();
    }

    /**
     * fetch 삭제
     * @param fetchId
     * @param request
     */

    @Transactional
    public void deleteFetch(Long fetchId, HttpServletRequest request) {
        log.info("Deleting fetch request");
        long memberId = cookieUtil.getUserId(request);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));


        Optional<Fetch> fetch = fetchRepository.findById(fetchId);

        if(fetch.isEmpty()) {
            throw new BusinessException(ErrorCode.FETCH_NOT_FOUND);
        } else {
            log.info("fetch get{}", fetch.get());
            Optional<MemberFetch> memberFetch = memberFetchRepository.findByFetchAndMember(fetch.get(), member);
            if(memberFetch.isEmpty()) {
                throw new BusinessException(ErrorCode.FETCH_NOT_FOUND);
            }
            memberFetchRepository.delete(memberFetch.get());
        }
    }

    /**
     * 패치 조회
     * @param request
     * @return
     */
    public List<FetchGetResponseDto> getFetch(HttpServletRequest request) {
        log.info("Fetch Get request");

        long memberId = cookieUtil.getUserId(request);
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        List<MemberFetch> fetches = memberFetchRepository.findByMember(member);

        List<FetchGetResponseDto> fetchList = new ArrayList<>();
        for (MemberFetch memberFetch : fetches) {
            Fetch fetch = memberFetch.getFetch();
            FetchGetResponseDto fetchGetResponseDto = FetchGetResponseDto.builder()
                    .fetchId(fetch.getFetchId())
                    .fetchName(fetch.getNickname())
                    .fetchSerialNumber(fetch.getFetchSerialNumber())
                    .build();
            fetchList.add(fetchGetResponseDto);
        }

        return fetchList;
    }

}
