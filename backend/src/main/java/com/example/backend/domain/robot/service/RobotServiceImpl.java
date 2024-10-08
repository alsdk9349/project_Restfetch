package com.example.backend.domain.robot.service;

import com.example.backend.domain.member.entity.Member;
import com.example.backend.domain.member.entity.MemberFetch;
import com.example.backend.domain.member.repository.MemberFetchRepository;
import com.example.backend.domain.member.repository.MemberRepository;
import com.example.backend.domain.report.dto.response.ReportListResponseDto;
import com.example.backend.domain.robot.dto.response.AllListResponseDto;
import com.example.backend.domain.robot.dto.response.FetchGetResponseDto;
import com.example.backend.domain.robot.dto.response.ObserverGetResponseDto;
import com.example.backend.domain.robot.entity.Fetch;
import com.example.backend.domain.robot.entity.FetchObserver;
import com.example.backend.domain.robot.entity.Observer;
import com.example.backend.domain.robot.repository.FetchObserverRepository;
import com.example.backend.domain.robot.repository.ObserverRepository;
import com.example.backend.global.error.BusinessException;
import com.example.backend.global.error.ErrorCode;
import com.example.backend.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RobotServiceImpl implements RobotService {

    private final ObserverRepository observerRepository;
    private final CookieUtil cookieUtil;
    private final MemberRepository memberRepository;
    private final MemberFetchRepository memberFetchRepository;
    private final FetchObserverRepository fetchObserverRepository;

    public AllListResponseDto getAllList(HttpServletRequest request) {
        log.info("Get allList");

        long memberId = cookieUtil.getUserId(request);
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        List<MemberFetch> memberFetches = memberFetchRepository.findByMember(member);

        List<FetchGetResponseDto> fetchList = new ArrayList<>();
        List<ObserverGetResponseDto> observerList = new ArrayList<>();

        // 모든 Fetch에 대해 반복
        for (MemberFetch memberFetch : memberFetches) {
            Fetch fetch = memberFetch.getFetch();

            // Fetch 정보를 DTO로 변환하여 리스트에 추가
            FetchGetResponseDto fetchDto = FetchGetResponseDto.builder()
                    .fetchId(fetch.getFetchId())
                    .fetchSerialNumber(fetch.getFetchSerialNumber())
                    .fetchName(fetch.getNickname())  // Fetch 이름 등 필요한 정보를 추가
                    .build();

            fetchList.add(fetchDto);  // Fetch DTO 추가

            // 해당 Fetch에 연결된 Observer 조회
            List<FetchObserver> fetchObservers = fetchObserverRepository.findByFetch(fetch);

            // 모든 Observer에 대해 반복
            for (FetchObserver fetchObserver : fetchObservers) {
                Observer observer = fetchObserver.getObserver();

                // Observer 정보를 DTO로 변환하여 리스트에 추가
                ObserverGetResponseDto observerDto = ObserverGetResponseDto.builder()
                        .observerId(observer.getObserver_id())
                        .observerSerialNumber(observer.getObserverSerialNumber())// Observer 이름 등 필요한 정보를 추가
                        .latitude(observer.getLatitude())
                        .longitude(observer.getLongitude())
                        .location(observer.getLocation())
                        .build();

                observerList.add(observerDto);  // Observer DTO 추가
            }
        }

        // AllListResponseDto 생성
        return new AllListResponseDto(fetchList, observerList);
    }
}
