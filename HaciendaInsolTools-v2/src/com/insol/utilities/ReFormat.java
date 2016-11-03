package com.insol.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.common.base.CaseFormat;

public class ReFormat {

	public static ArrayList<HashMap<String, String>> toItemList(List<String> list) {
		return ReFormat.toItemList(list, true, new HashMap<String, String>());
	}

	public static ArrayList<HashMap<String, String>> toItemList(List<String> list, boolean useReadableName) {
		return ReFormat.toItemList(list, useReadableName, null);
	}
	
	public static ArrayList<HashMap<String, String>> toItemList(List<String> list,
			HashMap<String, String> dictionary) {
		return ReFormat.toItemList(list, true, dictionary);
	}

	public static ArrayList<HashMap<String, String>> toItemList(List<String> list, boolean useReadableName,
			HashMap<String, String> dictionary) {

		try {

			ArrayList<HashMap<String, String>> items = new ArrayList<>();
			for (String listItem : list) {

				String textValue = (useReadableName) ? ReFormat.toReadableName(listItem, dictionary) : listItem;
				HashMap<String, String> item = ReFormat.toKeyValueMap(listItem, textValue);
				if (!items.contains(item)) {
					items.add(item);
				}
			}

			if (items.size() > 0) {
				return items;
			}

		} catch (Exception e) {
			System.err.println("Exception creating list in ReFormat.toItemsList: " + e.getMessage());
		}

		return null;
	}

	public static HashMap<String, String> toKeyValueMap(String key, String value) {
		try {
			HashMap<String, String> _map = new HashMap<>();
			_map.put("key", key);
			_map.put("value", value);
			return _map;

		} catch (Exception e) {
			System.err.println("Exception creating list in ReFormat.toKeyValueMap: " + e.getMessage());
		}
		return null;
	}

	public static String toReadableName(String name) {
		return toReadableName(name, new HashMap<String, String>());
	}

	public static String toReadableName(String name, HashMap<String, String> dictionary) {

		try {

			name = splitFromCamelCase(name.replace("_", ""));
			name = valueReplace(name, dictionary);

		} catch (Exception e) {
			System.err.println("Exception cleaning string name in ReFormat.toReadableName: " + e.getMessage());
		}

		return name;
	}

	public static String splitFromCamelCase(String s) {
		try{
			return s.replaceAll(String.format("%s|%s|%s", "(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
		} catch (Exception e) {
			System.err.println("Exception splitting string from camelCase in ReFormat.splitFromCamelCase: " + e.getMessage());
		}
		return s;
	}

	public static String valueReplace(String target, HashMap<String, String> replaceDictionary) {

		try{
			if(!replaceDictionary.isEmpty()){
			
				for (Entry<String, String> word : replaceDictionary.entrySet()) {
					if (target.contains(word.getKey())) {
						target = target.replace(word.getKey(), word.getValue());
					}
				}

			}
			
		} catch (Exception e) {
			System.err.println("Exception cleaning  name in ReFormat.replace: " + e.getMessage());
		}
		
		return target;
	}

	public static String camelCaseFromUpperUnderscore(String s) {
		return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, s);
	}

	public static String camelCaseFromUnderscore(String s) {
		return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, s);
	}
}
