package com.insol.hacienda.processAdministrator.repositories;

import java.util.ArrayList;
import java.util.HashMap;

import com.data.toolkit.database.Database;
import com.data.toolkit.database.Database.DBType;
import com.data.toolkit.repository.Repository;
import com.insol.hacienda.filenet.utils.InsolUtils;

public class HaciendaDatabaseRepository extends Repository {

	InsolUtils utils = InsolUtils.getSingleton("NPP");
	String[] groupsList;
	
	
	public static void main(String[] args) {
		
		
		HaciendaDatabaseRepository repo = new HaciendaDatabaseRepository();
		System.out.println(repo.getFilterByProcess("PD"));
		
		
	}
	
	public HaciendaDatabaseRepository() {
		super();
		this.db = new Database(utils.getPEDBConnection());
	}
	
	public String[] getGroupsByProcess(String process) {
		
		db.prepare("SELECT [grupos] FROM [VWdb].[dbo].[INSOL_Process_Administrator] WHERE proceso = ? ");
		db.setParameter(DBType.STRING, process);

		ArrayList<HashMap<String,Object>> results = db.executeAndGetList();
		
		groupsList = new String[] {};
		
		if(!results.isEmpty()){
			
			for (HashMap<String, Object> result : results) {
				String groupCSV = result.get("grupos").toString();
				if(groupCSV.contains(",")){
					groupsList = groupCSV.split(",");
					if(groupsList.length > 0){
						return groupsList;
					}
				}
			}
			
		}
		
		return null;
	}
	
	public String getFilterByProcess(String process) {
		
		db.prepare("SELECT [extraFilter] FROM [VWdb].[dbo].[INSOL_Process_Administrator] WHERE proceso = ? ");
		db.setParameter(DBType.STRING, process);

		ArrayList<HashMap<String,Object>> results = db.executeAndGetList();
		
		String xFilter = null;
		
		if(!results.isEmpty()){
			int i = 0;
			for (HashMap<String, Object> result : results) {
				if(i == 0){
					if(result.containsKey("extrafilter")){
						xFilter = result.get("extrafilter").toString();	
					}
				}
				i ++;
			}
		return xFilter;
		}
		
		return null;
	}
}
