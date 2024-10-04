package com.example.backend.domain.pick.service;

import com.example.backend.domain.pick.dto.request.PickRequestDto;
import com.example.backend.domain.pick.entity.Pick;
import com.example.backend.domain.pick.repository.PickRepository;
import com.example.backend.domain.report.entity.Report;
import com.example.backend.domain.report.repository.ReportRepository;
import com.example.backend.global.error.BusinessException;
import com.example.backend.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PickServiceImpl implements PickService {

    private final PickRepository pickRepository;
    private final ReportRepository reportRepository;

    @Override
    public void pick(long reportId) {

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new BusinessException(ErrorCode.REPORT_NOT_FOUND));

        if (report.isPicked()) {
            throw new BusinessException(ErrorCode.REPORT_ALREADY_PICK);
        }

        Pick pick = Pick.builder()
                .report(report)
                .build();

        Optional<Pick> existPick = pickRepository.findByReport(report);

        if (existPick.isPresent()) {
            throw new BusinessException(ErrorCode.REPORT_DUPLICATED);
        }

        pickRepository.save(pick);

    }
}