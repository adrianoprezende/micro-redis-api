package com.micro.redis.core.service;

public interface IExpirationScheduleService {
	
	void scheduleNewExpirationTask(final String key, final Integer expirationTimeInSec) throws Exception;

}
