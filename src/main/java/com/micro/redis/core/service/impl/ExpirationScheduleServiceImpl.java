package com.micro.redis.core.service.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.micro.redis.core.cache.Cache;
import com.micro.redis.core.service.IExpirationScheduleService;

@Component
public class ExpirationScheduleServiceImpl implements IExpirationScheduleService {
	
	@Autowired
	private Cache cache;
	
	/** The Constant logger. */
	public static final Logger logger = LoggerFactory.getLogger(ExpirationScheduleServiceImpl.class);
	
	public void scheduleNewExpirationTask(final String key, final Integer expirationTimeInSec) throws Exception {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        ExpirationTask task = new ExpirationTask(cache, key);
         
        ScheduledFuture<String> futureExecution = executor.schedule(task, expirationTimeInSec , TimeUnit.SECONDS);
         
        try {
        	logger.info("Executing a new scheduled expiration task - key= {}, time= {}", key, expirationTimeInSec);
        	futureExecution.get();
//        	executor.awaitTermination(100, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
        	logger.error("Error when executing a scheduled expiration task - key= {}", key);
        	throw e;
        } 
         
        executor.shutdown();
    }

}
