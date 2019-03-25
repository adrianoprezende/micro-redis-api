package com.micro.redis.core.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.redis.core.cache.Cache;
import com.micro.redis.core.service.ICacheService;
import com.micro.redis.core.service.IExpirationScheduleService;

@Component
public class CacheServiceImpl implements ICacheService {
	
	/** The Constant logger. */
	public static final Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);
	
	private static final String DEFAULT_SUCCESS_MESSAGE = "OK";
	
	/** The repository. */
	@Autowired
	private Cache cache;
	
	@Autowired
	private IExpirationScheduleService expirationScheduleService;

	@Override
	public String set(final String key, final Object value, final Optional<Integer> ex) throws Exception {
		
		this.cache.addSingleData(key, value);
		
		if(ex.isPresent()) {
			expirationScheduleService.scheduleNewExpirationTask(key, ex.get());
		}
		return DEFAULT_SUCCESS_MESSAGE;
	}

	@Override
	public <T> T get(final String key) {
		return this.cache.getSingleData(key);
	}

	@Override
	public Integer del(final String... keys) {
		int deletedKeysCount = 0;
		
		for(final String key : keys) {
			String deletedValue = this.cache.removeSingleData(key);
			if(deletedValue != null) {
				deletedKeysCount++;
			}
		}
		
		return deletedKeysCount;
	}

	@Override
	public Integer dbSize() {
		return this.cache.getCacheSize();
	}

	@Override
	public Integer incr(final String key) {
		return this.cache.incrementSingleData(key);
	}

	@Override
	public Integer zadd(String key, Integer score, String member) {
		return this.cache.addSortedSetData(key, member, score);
	}

	@Override
	public Integer zrank(String key, String member) {
		return this.cache.getRankedSortedSetData(key, member);
	}

	@Override
	public Integer zcard(String key, String member) {
		return this.cache.getSortedSetData(key, member);
	}

	@Override
	public List<String> zrange(String key, Integer start, Integer stop) {
		return this.cache.getSortedSetRange(key, start, stop);
	}

}
