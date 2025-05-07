package com.ssafy.project.domain.auth.repository;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisRepository {
	private final RedisTemplate<String, String> redisTemplate;
	
	public void save(String key, String value, Long timeout) {
		redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
	}
	
	public String findByKey(String key) {
		return redisTemplate.opsForValue().get(key);
	}
	
	public void delete(String key) {
		redisTemplate.delete(key);
	}
	
	public boolean exists(String key) {
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}
}
