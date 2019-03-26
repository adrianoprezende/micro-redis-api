package com.micro.redis.core.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.micro.redis.main.SpringBootRestApiApp;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringBootRestApiApp.class)
public class CacheServiceTests {
	
	@Autowired
	private ICacheService service;
	
	@Test
	public void shouldSetAndGetAStringValue() throws Exception {
		String key = "myKeyTest1", value = "test", setResponseExpected = "OK";
		
		String setResponse = this.service.set(key, value, Optional.empty());
		assertEquals(setResponseExpected, setResponse);
		
		Object getResponse = this.service.get(key);
		assertEquals(value, getResponse);
	}
	
	@Test
	public void shouldSetAndGetAIntegerValue() throws Exception {
		String key = "myKeyTest2", setResponseExpected = "OK";
		Integer value = 101;
		
		String setResponse = this.service.set(key, value, Optional.empty());
		assertEquals(setResponseExpected, setResponse);
		
		Object getResponse = this.service.get(key);
		assertEquals(value, getResponse);
	}
	
	@Test
	public void shouldIncrementAIntegerNonZeroValue() throws Exception {
		String key = "myKeyTest3", setResponseExpected = "OK";
		Integer value = 101;
		Integer expectedValue = 102;
		
		String setResponse = this.service.set(key, value, Optional.empty());
		assertEquals(setResponseExpected, setResponse);
		
		Object getResponse = this.service.incr(key);
		assertEquals(expectedValue, getResponse);
	}
	
	@Test
	public void shouldIncrementAIntegerWithouMapingBeforeValue() throws InterruptedException {
		String key = "myKeyTest4";
		Integer expectedValue = 1;
		
		Object getResponse = this.service.incr(key);
		assertEquals(expectedValue, getResponse);
	}
	
	@Test
	public void shouldIncrementAStringNumber() throws Exception {
		String key = "myKeyTest5", setResponseExpected = "OK";
		String value = "101";
		Integer expectedValue = 102;
		
		String setResponse = this.service.set(key, value, Optional.empty());
		assertEquals(setResponseExpected, setResponse);
		
		Object getResponse = this.service.incr(key);
		assertEquals(expectedValue, getResponse);
	}
	
	@Test
	public void shouldWorkInMultiThreads() throws InterruptedException, ExecutionException {
		int threads = 10;
		ExecutorService threadService = Executors.newFixedThreadPool(threads);
		Collection<Future<String>> futures = new ArrayList<>(threads);

		String key = "myKeyThreadTest1", value = "test", setResponseExpected = "OK";
		
		Callable<String> callableTask = () -> {
		    TimeUnit.MILLISECONDS.sleep(300);
		    return this.service.set(key, value, Optional.empty());
		};

		for (int t = 0; t < threads; t++) {
			futures.add(threadService.submit(callableTask));
		}

		for (Future<String> f : futures) {
			assertEquals(setResponseExpected, f.get());
	    }
		
		assertEquals(value, this.service.get(key));
	}
	
	@Test
	public void shouldIncrementMultiThread() throws Exception {
		int threads = 50;
		ExecutorService threadService = Executors.newFixedThreadPool(threads);
		
		String key = "myKeyThreadTest2";
		Integer value = 100;
		Integer expectedFinalValue = value + threads;
		
		this.service.set(key, value, Optional.empty());
		
		Callable<Object> callableTask = () -> {
		    TimeUnit.MILLISECONDS.sleep(300);
		    return this.service.incr(key);
		};
		
		List<Callable<Object>> callableTasks = new ArrayList<>();
		for (int t = 0; t < threads; t++) {
			callableTasks.add(callableTask);
		}
		
		threadService.invokeAll(callableTasks);
		
		assertEquals(expectedFinalValue, this.service.get(key));
	}
	
	@Test
	public void shouldSetAndExpireAfterOneSecond() throws Exception {
		String key = "myKeyExpiryTest1", value = "test", setResponseExpected = "OK";
		
		Callable<Object> taskGet = () -> {
		    TimeUnit.MILLISECONDS.sleep(1001);
		    return this.service.get(key);
		};
		
		Callable<Object> taskSet = () -> {
		    TimeUnit.MILLISECONDS.sleep(0);
		    return this.service.set(key, value, Optional.of(1));
		};
		
		List<Callable<Object>> callableTasks = new ArrayList<>();
		callableTasks.add(taskSet);
		callableTasks.add(taskGet);
		
		ExecutorService threadService = Executors.newFixedThreadPool(1);
		List<Future<Object>> futures = new ArrayList<>(1);
		futures.addAll(threadService.invokeAll(callableTasks));
		
		assertEquals(setResponseExpected, futures.get(0).get());
		assertEquals(null, futures.get(1).get());
	}
	
	@Test
	public void shouldAddZSet() throws Exception {
		String key = "myKeyTestZAdd1", member = "test";
		Integer setResponseExpected = 1, score = 2;
		
		Integer setResponse = this.service.zadd(key, score, member);
		assertEquals(setResponseExpected, setResponse);
	}
	
	@Test
	public void shouldUpdateZSet() throws Exception {
		String key = "myKeyTestZUpdate1", member = "test";
		Integer setResponseExpected = 1, setResponseExpectedAfterUpdate = 0, scoreAdd = 2, scoreUpdate = 5;
		
		Integer setResponse = this.service.zadd(key, scoreAdd, member);
		assertEquals(setResponseExpected, setResponse);
		
		Integer setResponseUpdated = this.service.zadd(key, scoreUpdate, member);
		assertEquals(setResponseExpectedAfterUpdate, setResponseUpdated);
	}
	
	@Test
	public void shouldUpdateScoreZSet() throws Exception {
		String key = "myKeyTestZAdd2", member = "test";
		Integer setResponseExpected = 0, scoreOne = 1, scoreTwo = 2;
		
		this.service.zadd(key, scoreOne, member);
		Integer setResponse = this.service.zadd(key, scoreTwo, member);
		assertEquals(setResponseExpected, setResponse);
	}
	
	@Test
	public void shouldReturnZCardCorrect() throws Exception {
		String key = "myKeyTestZCard1", member = "test";
		Integer scoreOne = 1, scoreTwo = 2;
		
		this.service.zadd(key, scoreOne, member);
		Integer returnOne = this.service.zcard(key, member);
		assertEquals(scoreOne, returnOne);
		
		this.service.zadd(key, scoreTwo, member);
		Integer returnTwo = this.service.zcard(key, member);
		assertEquals(scoreTwo, returnTwo);
	}
	
	@Test
	public void shouldReturnZRankCorrect() throws Exception {
		String key = "myKeyTestZRank1", memberOne = "test", memberTwo = "test2", memberThree = "test3";
		Integer scoreOne = 1, scoreThree = 3, scoreFive = 5, expectedRank = 1;
		
		this.service.zadd(key, scoreOne, memberOne);
		this.service.zadd(key, scoreFive, memberTwo);
		this.service.zadd(key, scoreThree, memberThree);
		
		Integer rank = this.service.zrank(key, memberThree);
		assertEquals(expectedRank, rank);
	}
	
	@Test
	public void shouldReturnZRangeCorrect() throws Exception {
		String key = "myKeyTestZRange1", member1 = "test1", member2 = "test2", member3 = "test3", expectedMember = "test3";
		Integer score1 = 1, score2 = 2, score3 = 3;
		
		this.service.zadd(key, score1, member1);
		this.service.zadd(key, score2, member2);
		this.service.zadd(key, score3, member3);
		
		List<String> members = this.service.zrange(key, 2, 3);
		assertEquals(expectedMember, members.get(0));
	}
	
	@Test
	public void shouldReturnZRangeCorrectWithNegativeNumbers() throws Exception {
		String key = "myKeyTestZRange2", member1 = "test1", member2 = "test2", member3 = "test3";
		Integer score1 = 1, score2 = 2, score3 = 3;
		List<String> returnedMembers = null;
		
		this.service.zadd(key, score1, member1);
		this.service.zadd(key, score2, member2);
		this.service.zadd(key, score3, member3);
		
		List<String> expectedMembers = new ArrayList<>();
		expectedMembers.add(member2);
		expectedMembers.add(member3);
		
		returnedMembers = this.service.zrange(key, -2, -1);
		assertEquals(expectedMembers, returnedMembers);
		
		expectedMembers.clear();
		expectedMembers.add(member1);
		expectedMembers.add(member2);
		expectedMembers.add(member3);
		
		returnedMembers = this.service.zrange(key, 0, -1);
		assertEquals(expectedMembers, returnedMembers);
	}
	
	@Test
	public void shouldReturnCorrectCacheSize() throws Exception {
		this.service.clearCache();
		
		String key = "myKeyTestCacheSize1", member1 = "test1", member2 = "test2", member3 = "test3";
		Integer score1 = 1, score2 = 2, score3 = 3;
		
		this.service.zadd(key, score1, member1);
		this.service.zadd(key, score2, member2);
		this.service.zadd(key, score3, member3);
		
		String key2 = "myKeyTestCacheSize2", value = "test";
		
		this.service.set(key2, value, Optional.empty());
		
		Integer size = this.service.dbSize();
		Integer expectedSize = 2;
		
		assertEquals(expectedSize, size);
		
		this.service.del(key);
		
		Integer sizeAfterDel = this.service.dbSize();
		Integer expectedSizeAfterDel = 1;
		
		assertEquals(expectedSizeAfterDel, sizeAfterDel);
	}
	
	

}
