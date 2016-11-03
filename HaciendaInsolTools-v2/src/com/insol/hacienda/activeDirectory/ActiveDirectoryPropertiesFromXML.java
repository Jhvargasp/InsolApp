package com.insol.hacienda.activeDirectory;

import com.data.toolkit.utility.SimpleXMLNodeReader;

public class ActiveDirectoryPropertiesFromXML {

	private SimpleXMLNodeReader xml = new SimpleXMLNodeReader();
	private String username;
	private String password;
	private String userBase;
	private String searchBase;
	private String ldapServerURL;
	private String filter;
	private String fields;
	
	public ActiveDirectoryPropertiesFromXML(String location) {
		xml.loadFrom(location);
		this.load();
	}
	
	private void load() {
		this.username = xml.getNode("ad-admin-user");
		this.password = xml.getNode("ad-admin-password");
		this.userBase = xml.getNode("ad-user-base");
		this.searchBase = xml.getNode("ad-search-base");
		this.ldapServerURL = xml.getNode("ad-ldap-server");
		this.filter = xml.getNode("ad-filter");
		this.fields = xml.getNode("ad-fields");
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getUserBase() {
		return userBase;
	}

	public String getLdapServerURL() {
		return ldapServerURL;
	}

	public String getFilter() {
		return filter;
	}

	public String[] getFields() {
		return fields.split(",");
	}

	public String getSearchBase() {
		return searchBase;
	}
}
