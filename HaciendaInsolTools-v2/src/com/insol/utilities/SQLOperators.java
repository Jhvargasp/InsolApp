package com.insol.utilities;

import java.util.HashMap;


public class SQLOperators {

	public static String getOperator(String operator) {
		HashMap<String, String> ops = getPrivateList();
		
		if(ops.containsKey(operator)){
			return ops.get(operator);
		}
		return null;
	}

	private static HashMap<String, String> getPrivateList() {
		
		HashMap<String, String> operators = new HashMap<>();
		
		operators.put("equal", 				" = ");
		operators.put("lower_than", 		" < ");
		operators.put("lower_or_equal", 	" <= ");
		operators.put("greater_than", 		" > ");
		operators.put("greater_or_equal", 	" >= ");
		operators.put("not", 				" <> ");
		operators.put("contains", 			" LIKE ");
		operators.put("starts_with", 		" LIKE ");
		operators.put("ends_with", 			" LIKE ");
		
		return operators;
	}

}
