package com.micro.redis.core.service;

import java.util.List;
import java.util.Optional;

public interface ICacheService {
	
	String set(String key, Object value, Optional<Integer> ex) throws Exception;
	
	<T> T get(String key);
	
	Integer del(String... keys);
	
	Integer dbSize();
	
	Integer incr(String key);
	
	Integer zadd(String key, Integer score, String member);
	
	Integer zcard(String key, String member);
	
	Integer zrank(String key, String member);
	
	List<String> zrange(String key, Integer start, Integer stop);
	
	void clearCache();

}
