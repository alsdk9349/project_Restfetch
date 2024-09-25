package com.example.backend.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Component
@Transactional
@RequiredArgsConstructor
public class MemberRedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void setValue(String key, String data, Duration duration) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, data, duration);
    }

    @Transactional(readOnly = true)
    public String getValues(String key) {
        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        if (ops.get(key) == null) {
            return "false";
        }
        return (String) ops.get(key);
    }

    public void deleteValue(String key) {redisTemplate.delete(key);}
}
