package com.micro.redis.core.service.impl;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.micro.redis.core.cache.Cache;

public class ExpirationTask implements Callable<String> {
	
	public static final Logger logger = LoggerFactory.getLogger(ExpirationTask.class);
	
	private Cache cache;
	
	private String key;

	public ExpirationTask(final Cache cache, final String keyToExpire) {
		this.cache = cache;
		this.key = keyToExpire;
	}

	@Override
	public String call() throws Exception {
		try {
			logger.info("Removing a single data - key= {}", this.key);
			return this.cache.removeSingleData(this.key);
		} catch (Exception e) {
			logger.error("Error when removing a single data - key= {}", this.key);
			throw e;
		}
	}
}
