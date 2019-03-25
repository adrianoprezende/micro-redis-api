package com.micro.redis.core.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class Cache {

	private Map<String, ? super Object> singleData = new ConcurrentHashMap<>();

	@SuppressWarnings("unchecked")
	public <T> T getSingleData(final String key) {
		return (T) singleData.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T addSingleData(final String key, final Object data) {
		return (T) this.singleData.compute(key, (mkey, mVal) -> data);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T removeSingleData(final String key) {
		return (T) this.singleData.remove(key);
	}
	
	public Integer getCacheSize() {
		return singleData.size();
	}
	
	public Integer incrementSingleData(final String key) {
		return (Integer) singleData.compute(key, (mkey, mVal) -> {
			if (mVal == null) {
				return 1;
			}
			
			if (mVal instanceof String) {
				return Integer.valueOf((String) mVal) + 1;
			}
			
			if (mVal instanceof Integer) {
				return (Integer)mVal + 1;
			}
			
			//TODO Criar uma exceção específica.
			throw new RuntimeException("The value are not in the supported type");
		}
		);
	}
	
	@SuppressWarnings("unchecked")
	public Integer addSortedSetData(final String key, final String member, final Integer score) {
		Object oldValue = this.singleData.get(key);
		
		HashMap<String, Integer> newValue = (HashMap<String, Integer>) this.singleData.compute(key, (mkey, mVal) -> {
			
			if (mVal == null) {
				Map<String, Integer> newMap = new HashMap<>();
				newMap.put(member, score);
				return newMap;
			}
			
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Integer> convertedVal = mapper.convertValue(mVal, HashMap.class);
			
			convertedVal.put(member, score);
			return this.rank(convertedVal);
		});
		
		return newValue.equals(oldValue) ? 0 : 1;
	}
	
	@SuppressWarnings("unchecked")
	public Integer getSortedSetData(final String key, final String member) {
		Object curData = this.singleData.get(key);
		
		if (curData != null && curData instanceof HashMap) {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Integer> convertedData = mapper.convertValue(curData, HashMap.class);
			return convertedData.get(member);
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public Integer getRankedSortedSetData(final String key, final String member) {
		Object curData = this.singleData.get(key);
		
		if (curData != null && curData instanceof HashMap) {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Integer> convertedData = mapper.convertValue(curData, HashMap.class);
			
			int rank = 0;
			for (String k : convertedData.keySet()) {
			    if (member.equals(k)) {
			    	return rank;
			    }
			    
			    rank++;
			} 
			
			return rank;
		}
		
		return null;
	}
	
	private Map<String, Integer> rank(final Map<String, Integer> data) {
		List<Entry<String, Integer>> list = new ArrayList<>(data.entrySet());
        list.sort(Entry.comparingByValue());

        return list.stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2, LinkedHashMap::new));
	}
	
	@SuppressWarnings("unchecked")
	public List<String> getSortedSetRange(final String key, final Integer start, final Integer stop) {
		Object curData = this.singleData.get(key);
		
		if (curData != null && curData instanceof HashMap) {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Integer> convertedVal = mapper.convertValue(curData, HashMap.class);
			List<Entry<String, Integer>> list = new ArrayList<>(convertedVal.entrySet());
			
			int mStart = this.getStart(start, list.size());
			int mStop = this.getStop(stop, list.size());
			
			return list.subList(mStart, mStop).stream().map(item -> item.getKey()).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}
	
	private Integer getStart(Integer start, Integer listSize) {
		if (start < 0) {
			return listSize + start;
		} else if(start > 0 && start > listSize) {
			return listSize;
		}
		return start;
	}
	
	private Integer getStop(Integer stop, Integer listSize) {
		if (stop < 0) {
			return (listSize + 1) + stop;
		} else if(stop > 0 && stop > listSize) {
			return listSize;
		}
		return stop;
	}
	
}