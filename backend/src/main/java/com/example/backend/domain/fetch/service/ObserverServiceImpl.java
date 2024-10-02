package com.example.backend.domain.fetch.service;

import com.example.backend.domain.fetch.dto.request.ObserverRegisterRequestDto;
import com.example.backend.domain.fetch.dto.response.FetchGetResponseDto;
import com.example.backend.domain.fetch.dto.response.ObserverGetResponseDto;
import com.example.backend.domain.fetch.dto.response.ObserverRegisterResponseDto;
import com.example.backend.domain.fetch.entity.Fetch;
import com.example.backend.domain.fetch.entity.FetchObserver;
import com.example.backend.domain.fetch.entity.Observer;
import com.example.backend.domain.fetch.repository.FetchObserverRepository;
import com.example.backend.domain.fetch.repository.FetchRepository;
import com.example.backend.domain.fetch.repository.ObserverRepository;
import com.example.backend.domain.member.entity.Member;
import com.example.backend.domain.member.entity.MemberFetch;
import com.example.backend.domain.member.repository.MemberRepository;
import com.example.backend.global.error.BusinessException;
import com.example.backend.global.error.ErrorCode;
import com.example.backend.global.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ObserverServiceImpl implements ObserverService {

    private final ObserverRepository observerRepository;
    private final FetchRepository fetchRepository;
    private final FetchObserverRepository fetchObserverRepository;
    private final CookieUtil cookieUtil;

    /**
     * 옵저버 등록
     * @param fetchId
     * @param requestDto
     * @return
     */

    @Transactional
    public ObserverRegisterResponseDto registerObserver(Long fetchId, ObserverRegisterRequestDto requestDto) {
        log.info("register observer request: {}, fetchId: {}", requestDto, fetchId);

        String observerSerialNumber = requestDto.getObserverSerialNumber();
        Double latitude = requestDto.getLatitude();
        Double longitude = requestDto.getLongitude();

        Fetch fetch = fetchRepository.findById(fetchId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FETCH_NOT_FOUND));

        Optional<Observer> existObserver = observerRepository.findByObserverSerialNumber(observerSerialNumber);

        if (existObserver.isPresent()) {

            Observer observer = existObserver.get();
            Optional<FetchObserver> existFetchObserver = fetchObserverRepository.findByFetchAndObserver(fetch, existObserver.get());

            if (existFetchObserver.isPresent()) {
                throw new BusinessException(ErrorCode.OBSERVER_DUPLICATED);
            }

            FetchObserver fetchObserver = FetchObserver.builder()
                    .fetch(fetch)
                    .observer(observer)
                    .build();

        } else {

            Observer observer = Observer.builder()
                    .observerSerialNumber(observerSerialNumber)
                    .latitude(latitude)
                    .longitude(longitude)
                    .build();

            observerRepository.save(observer);

            FetchObserver fetchObserver = FetchObserver.builder()
                            .fetch(fetch)
                            .observer(observer)
                            .build();

            fetchObserverRepository.save(fetchObserver);
        }

        Optional<Observer> observer = observerRepository.findByObserverSerialNumber(observerSerialNumber);
        Long observerId = observer.get().getObserver_id();

        return ObserverRegisterResponseDto.builder()
                .observerId(observerId)
                .observerSerialNumber(observerSerialNumber)
                .latitude(latitude)
                .longitude(longitude)
                .build();

    }

    /**
     * 옵저버 삭제
     * @param fetchId
     * @param observerId
     */
    @Transactional
    public void deleteObserver(Long fetchId, Long observerId) {
        log.info("Deleting observer request");

        Fetch fetch = fetchRepository.findById(fetchId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FETCH_NOT_FOUND));


        Optional<Observer> observer = observerRepository.findById(observerId);


        if(!observer.isPresent()) {
            throw new BusinessException(ErrorCode.OBSERVER_NOT_FOUND);
        } else {
            Optional<FetchObserver> fetchObserver = fetchObserverRepository.findByFetchAndObserver(fetch, observer.get());
            if(fetchObserver.isEmpty()) {
                throw new BusinessException(ErrorCode.OBSERVER_NOT_FOUND);
            }
            fetchObserverRepository.delete(fetchObserver.get());
        }
    }

    @Override
    public List<ObserverGetResponseDto> getObserver(Long fetchId) {

        Fetch fetch = fetchRepository.findById(fetchId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FETCH_NOT_FOUND));

        List<FetchObserver> observers = fetchObserverRepository.findByFetch(fetch);

        List<ObserverGetResponseDto> observerList = new ArrayList<>();
        for (FetchObserver fetchObserver : observers) {
            Observer observer = fetchObserver.getObserver();
            ObserverGetResponseDto observerGetResponseDto = ObserverGetResponseDto.builder()
                    .observerId(observer.getObserver_id())
                    .observerSerialNumber(observer.getObserverSerialNumber())
                    .longitude(observer.getLatitude())
                    .latitude(observer.getLatitude())
                    .build();

            observerList.add(observerGetResponseDto);
        }

        return observerList;

    }
}
