package com.insol.hacienda.filenet.repositories;

import java.util.HashMap;

import com.insol.utilities.SQLOperators;

public class FileNetHelper {

	public static String getAndFilter(String query, String item) {
		return getAndFilter(query, item, null);
	}
	
	public static String getAndFilter(String query, String item, String column) {
		if(item != null && !item.isEmpty()){
			String c = (column != null) ? column + " = " : "";
			query += " AND " + c + item;
		}
		return query;
	}

	public static String getWhereFilter(HashMap<String, Object> form, String type) {
		
		String whereFilter = null;
		String _op = form.get("operator").toString().toLowerCase(); 
		String operator = SQLOperators.getOperator(_op);
		String column = form.get("field").toString();
		String value = form.get("fieldValue").toString();
		
		switch (_op) {
		case "contains":
			
			if(type.equals("string")){
				whereFilter = column + " LIKE '%" + value + "%' ";
			} else {
				whereFilter = column + " LIKE %" + value + "% ";
			}
			
			break;
		case "starts_with":
			
			if(type.equals("string")){
				whereFilter = column + " LIKE '" + value + "%' ";
			} else {
				whereFilter = column + " LIKE " + value + "% ";
			}		
			
			break;
		case "ends_with":
			
			if(type.equals("string")){
				whereFilter = column + " LIKE '%" + value + "' ";
			} else {
				whereFilter = column + " LIKE %" + value + " ";
			}
			
			break;
		default:
			
			if(type.equals("string")){
				whereFilter = column +  operator + "'" + value + "' ";
			} else {
				
				String w = (column.toLowerCase().equals("wobnum")) ? "0x" : "";
				
				whereFilter = column + operator + "" + w + value + " ";
			}		
			break;
		}
		
		return whereFilter;
	}

	public static Integer getQuantity(HashMap<String, Object> form) {
		try{
			
			String q = form.get("quantity").toString();
			
			if (q == null || q.isEmpty() || Integer.valueOf(q).intValue() >= 500) {

				return 500;

			} else {
				
				return Integer.parseInt(q);
				
			}
			
			
		} catch (Exception e) {
			System.err.println("Exception in FileNetRepository.getQuantitiy" + e.getMessage());
		}
		
		return null;
		
	}
	
}
