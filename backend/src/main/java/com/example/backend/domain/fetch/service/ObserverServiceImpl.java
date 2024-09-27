package com.example.backend.domain.fetch.service;

import com.example.backend.domain.fetch.dto.request.ObserverRegisterRequestDto;
import com.example.backend.domain.fetch.dto.response.ObserverRegisterResponseDto;
import com.example.backend.domain.fetch.entity.Fetch;
import com.example.backend.domain.fetch.entity.Observer;
import com.example.backend.domain.fetch.repository.FetchRepository;
import com.example.backend.domain.fetch.repository.ObserverRepository;
import com.example.backend.global.error.BusinessException;
import com.example.backend.global.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.geom.Point2D;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ObserverServiceImpl implements ObserverService {

    private ObserverRepository observerRepository;
    private FetchRepository fetchRepository;

    @Autowired
    public ObserverServiceImpl(ObserverRepository observerRepository, FetchRepository fetchRepository) {
        this.observerRepository = observerRepository;
        this.fetchRepository = fetchRepository;
    }

    @Transactional
    public ObserverRegisterResponseDto registerObserver(Long fetchId, ObserverRegisterRequestDto requestDto) {
        log.info("register observer request: {}, fetchId: {}", requestDto, fetchId);

        String observerSerialNumber = requestDto.getObserverSerialNumber();
        Point2D.Double location = requestDto.getLocation();

        Fetch fetch = fetchRepository.findById(fetchId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FETCH_NOT_FOUND));

        Optional<Observer> optionalObserver = observerRepository.findByObserverSerialNumberAndFetch_FetchId(observerSerialNumber, fetchId);
        if (optionalObserver.isPresent()) {
            throw new BusinessException(ErrorCode.OBSERVER_DUPLICATED);
        }

        Observer observer = Observer.builder()
                .observerSerialNumber(observerSerialNumber)
                .location(location)
                .fetch(fetch)
                .build();

        Observer saveObserver = observerRepository.save(observer);

        return ObserverRegisterResponseDto.builder()
                .observerSerialNumber(saveObserver.getObserverSerialNumber())
                .observerId(saveObserver.getObserver_id())
                .location(saveObserver.getLocation())
                .build();

    }

}
