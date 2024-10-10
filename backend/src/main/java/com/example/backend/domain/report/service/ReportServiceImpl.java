package com.example.backend.domain.report.service;

import com.example.backend.domain.Sse.controller.SseController;
import com.example.backend.domain.Sse.repository.SseRepository;
import com.example.backend.domain.Sse.service.SseService;
import com.example.backend.domain.member.entity.Member;
import com.example.backend.domain.member.entity.MemberFetch;
import com.example.backend.domain.member.repository.MemberFetchRepository;
import com.example.backend.domain.member.repository.MemberRepository;
import com.example.backend.domain.report.dto.response.ReportListResponseDto;
import com.example.backend.domain.robot.entity.Fetch;
import com.example.backend.domain.robot.entity.FetchObserver;
import com.example.backend.domain.robot.entity.Observer;
import com.example.backend.domain.robot.repository.FetchObserverRepository;
import com.example.backend.domain.robot.repository.ObserverRepository;
import com.example.backend.domain.report.dto.request.ReportRequestDto;
import com.example.backend.domain.report.dto.response.ReportGetResponseDto;
import com.example.backend.domain.report.entity.Report;
import com.example.backend.domain.report.repository.ReportRepository;
import com.example.backend.global.error.BusinessException;
import com.example.backend.global.error.ErrorCode;
import io.netty.handler.codec.base64.Base64Decoder;
import com.example.backend.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final ObserverRepository observerRepository;
    private final CookieUtil cookieUtil;
    private final MemberRepository memberRepository;
    private final MemberFetchRepository memberFetchRepository;
    private final FetchObserverRepository fetchObserverRepository;

    private final SseController sseController;
    private final SseRepository sseRepository;

    /**
     * 새로운 기록
     * @param requestDto
     * @return
     */
    public ReportGetResponseDto newReport(ReportRequestDto requestDto) {
        log.info("New report");

        Observer observer = observerRepository.findByObserverSerialNumber(requestDto.getObserverSerialNumber())
                .orElseThrow(() -> new BusinessException(ErrorCode.OBSERVER_NOT_FOUND));

//        byte[] decodedBytes = Base64.getDecoder().decode(requestDto.getPicture());
//
//        String picture = new String(Base64.getDecoder().decode(Base64.getDecoder().decode(decodedBytes)), StandardCharsets.UTF_8);

        String picture = requestDto.getPicture();

        Report report = Report.builder()
                .observer(observer)
                .picture(picture)
                .isPicked(false)
                .build();

        reportRepository.save(report);

        String observerSerialNumber = observer.getObserverSerialNumber();
        Long observerId = observer.getObserver_id();

        ReportGetResponseDto responseDto = ReportGetResponseDto.builder()
                .reportId(report.getId())
                .observerId(observerId)
                .observerSerialNumber(observerSerialNumber)
                .picture(picture)
                .isPicked(report.isPicked())
                .message("새로운 기록이 생겼습니다.")
                .build();

        sseRepository.save(observerSerialNumber, new SseEmitter());
        log.info("New report success");
//        cookieUtil.getMemberEmail()
        sseController.send(responseDto, "새로운 기록이 생겼습니다.");

        return responseDto;
    }

    /**
     * 임베디드가 회수 요청 받기
     * @param observerId
     * @return
     */
    public List<ReportGetResponseDto> getReports(Long observerId) {
        log.info("Get reports");

        Observer observer = observerRepository.findById(observerId)
                .orElseThrow(() -> new BusinessException(ErrorCode.OBSERVER_NOT_FOUND));

        List<Report> reports = reportRepository.findByObserver(observer);

        return reports.stream()
                .map(report -> ReportGetResponseDto.of(observerId, report))
                .toList();
    }

    public List<ReportListResponseDto> getReportList(HttpServletRequest request) {
        log.info("Get reportList");

        long memberId = cookieUtil.getUserId(request);
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        List<MemberFetch> fetches = memberFetchRepository.findByMember(member);

        List<ReportListResponseDto> reportList = new ArrayList<>();

        for (MemberFetch memberFetch : fetches) {
            Fetch fetch = memberFetch.getFetch();

            List<FetchObserver> fetchObservers = fetchObserverRepository.findByFetch(fetch);

            for (FetchObserver fetchObserver : fetchObservers) {
                Observer observer = fetchObserver.getObserver();

                List<Report> unpickedReports = observer.getReports().stream()
                        .filter(report -> !report.isPicked())
                        .collect(Collectors.toList());

                for (Report report : unpickedReports) {
                    ReportListResponseDto reportDto = ReportListResponseDto.builder()
                            .observerId(observer.getObserver_id())
                            .reportId(report.getId())
                            .picture(report.getPicture())
                            .createdAt(report.getCreatedAt())
                            .build();
                    reportList.add(reportDto);
                }
            }
        }
        return reportList;
    }
}
