package com.insol.hacienda.activeDirectory.repositories;

import java.util.ArrayList;
import java.util.HashMap;

import com.insol.hacienda.activeDirectory.ActiveDirectoryPropertiesFromXML;
import com.insol.hacienda.processAdministrator.repositories.HaciendaDatabaseRepository;
import com.insol.utilities.ReFormat;

import RLib2.ActiveDirectoryAccount;
import RLib2.ActiveDirectoryPropertiesHelper;
import RLib2.ActiveDirectoryUtils;


public abstract class ActiveDirectoryRepository {
	
	ArrayList<HashMap<String, String>> accounts;
	ActiveDirectoryPropertiesFromXML config;
	ActiveDirectoryPropertiesHelper ADHelper;
	
	public ActiveDirectoryRepository() {
		config = new ActiveDirectoryPropertiesFromXML(configLocation());
		ADHelper = new ActiveDirectoryPropertiesHelper();
		
		ADHelper.userToConnectToAD = "CN=" + config.getUsername() + "," + config.getUserBase();
		ADHelper.password = config.getPassword();
		ADHelper.userBase = config.getUserBase();
		ADHelper.searchBase = config.getSearchBase();
		ADHelper.ldapServerURL = config.getLdapServerURL();
		ADHelper.filter = config.getFilter();
		ADHelper.fields = config.getFields();
	}
	
	public abstract String configLocation();
	
	public ArrayList<HashMap<String,String>> getUsersByProcess(String process) {
		HaciendaDatabaseRepository haciendaRepo = new HaciendaDatabaseRepository();
		return getUsersByGroups(haciendaRepo.getGroupsByProcess(process));
	}
	
	public ArrayList<HashMap<String,String>> getUsersByGroups(String groups) {
		return getUsersByGroups(groups.split(","));
	}
	
	public ArrayList<HashMap<String,String>> getUsersByGroups(String[] groups) {
		
		try {
			accounts = new ArrayList<>();
			ADHelper.GroupsNames = groups;
			for (ActiveDirectoryAccount account :  ActiveDirectoryUtils.getMembers(ADHelper)) {
				
				HashMap<String, String> accountItem = ReFormat.toKeyValueMap(account.UserName, account.DisplayName + " (" + account.UserName + ")");
				
				if(!accounts.contains(accountItem)){
					accounts.add( accountItem);
				}
			}

			if(accounts.size() > 0){	
				return accounts;
			}

		} catch (Exception e) {
			System.err.println("Error: " + e.toString());
		}
		return null;
	}
	
}
