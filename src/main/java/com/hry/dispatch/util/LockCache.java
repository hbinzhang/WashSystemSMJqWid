package com.hry.dispatch.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockCache {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LockCache.class);
	private static Map<String, Lock> lockCache = new ConcurrentHashMap<String, Lock>();
	
	public static void addOne(String uname) {
		LOGGER.info("addOne uname: " + uname);
		if(lockCache.containsKey(uname)) {
			return;
		} else {
			lockCache.put(uname, new Lock(uname));
		}
	}
	
	public static Lock acquire(String uname) throws Exception {
		LOGGER.info("addOne uname: " + uname);
		if(lockCache.containsKey(uname)) {
			return lockCache.get(uname);
		} else {
			throw new Exception("No Lock avaible");
		}
	}
}
