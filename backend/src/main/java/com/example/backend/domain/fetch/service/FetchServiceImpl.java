package com.example.backend.domain.fetch.service;

import com.example.backend.domain.fetch.dto.request.FetchRegisterRequestDto;
import com.example.backend.domain.fetch.dto.response.FetchRegisterResponseDto;
import com.example.backend.domain.fetch.entity.Fetch;
import com.example.backend.domain.fetch.repository.FetchRepository;
import com.example.backend.domain.member.entity.Member;
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
    private final CookieUtil cookieUtil;

    @Transactional
    public FetchRegisterResponseDto registerFetch(FetchRegisterRequestDto requestDto, HttpServletRequest request) {
        log.info("RegisterFetch request : {} ", requestDto);
        long memberId = cookieUtil.getUserId(request);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        String fetchSerialNumber = requestDto.getFetchSerialNumber();
        String nickname = requestDto.getNickname();


        Optional<Fetch> ByfetchSerialNumber = fetchRepository.findByFetchSerialNumberAndMember_MemberId(fetchSerialNumber, memberId);
        if(ByfetchSerialNumber.isPresent()) {
            throw new BusinessException(ErrorCode.FETCH_DUPLICATED);
        }

        Fetch fetch = Fetch.builder()
                .fetchSerialNumber(fetchSerialNumber)
                .nickname(nickname)
                .member(member)
                .build();

        Fetch saveFetch = fetchRepository.save(fetch);

        return FetchRegisterResponseDto.builder()
                .memberId(saveFetch.getMember().getMemberId())
                .fetchId(saveFetch.getFetchId())
                .nickname(saveFetch.getNickname())
                .build();
    }

    @Transactional
    public void deleteFetch(Long fetchId, HttpServletRequest request) {
        log.info("Deleting fetch request");

        Optional<Fetch> fetch = fetchRepository.findById(fetchId);

        if(!fetch.isPresent()) {
            throw new BusinessException(ErrorCode.FETCH_NOT_FOUND);
        }

        fetchRepository.deleteById(fetchId);
    }

}
