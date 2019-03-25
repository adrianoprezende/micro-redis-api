package com.micro.redis.core.cache;

import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Teste {

	public static void main(String[] args) {
		
//		// a ConcurrentHashMAp of string keys and Long values 
//		ConcurrentMap<String, Integer> map = new ConcurrentHashMap<>(); 
//		map.put("apple", 3); 
//		map.put("mango", 4); 
//		System.out.println("map before calling compute: " + map); 
//		
//		// in JDK 8 - you can also use compute() and lambda expression to 
//		// atomically update a value or mapping in ConcurrentHashMap 
//		map.compute("apple2", (key, value) -> value == null ? 1 : value + 1); 
//		System.out.println("map after calling compute on apple: " + map);
		
		Map<String, Integer> map = new TreeMap<>();

		// Add Items to the TreeMap
		map.put("Two", 2);
		map.put("One", 1);
		map.put("Three", 3);
		
		// Iterate over them
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
		    System.out.println(entry.getKey() + " => " + entry.getValue());
		} 

	}

}
