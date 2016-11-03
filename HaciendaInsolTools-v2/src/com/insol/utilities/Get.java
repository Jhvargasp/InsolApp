package com.insol.utilities;

import java.util.HashMap;

public class Get {

	public static String valueOrDefault(String str) {
		return Get.valueOrDefault(str, "");
	}
	
	public static String valueOrDefault(String str, String defaultValue) {
		return (str != null && !str.equals("")) ? str : defaultValue;
	}
	
	public static String valueOrDefault(HashMap<String, String> haystack, String key) {
		return Get.valueOrDefault(haystack, key, "");
	}
	
	public static String valueOrDefault(HashMap<String, String> haystack, String key, String defaultValue) {
		return (haystack.containsKey(key) && !haystack.get(key).isEmpty()) ? haystack.get(key) : defaultValue;
	}
		
	public static Object valueOrDefault(Object thing, String defaultValue) {
		return (thing != null && !thing.equals("")) ? thing : defaultValue;
	}
		
	public static String valueOrNull(String str) {
		return (str != null && !str.equals("")) ? str : null;
	}
	
	public static Object valueOrNull(Object str) {
		return (str != null && !str.equals("")) ? str : null;
	}

}
