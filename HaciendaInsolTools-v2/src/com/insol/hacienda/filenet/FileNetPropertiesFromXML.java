package com.insol.hacienda.filenet;

import com.data.toolkit.utility.SimpleXMLNodeReader;

public class FileNetPropertiesFromXML {

	private SimpleXMLNodeReader xml = new SimpleXMLNodeReader();
	private String contextFactory;
	private String uri;
	private String uri2;
	private String objectStore;
	private String stanza;
	private String processEngineUri;
	private String jass;
	private String adminUsername;
	private String adminPassword;
	
	public FileNetPropertiesFromXML(String location) {
		xml.loadFrom(location);
		this.load();
	}
	
	private void load() {
		try {

			contextFactory = xml.getNode("context-factory");
			uri = xml.getNode("uri");
			objectStore = xml.getNode("object-store");
			stanza = xml.getNode("stanza");
			processEngineUri = xml.getNode("pe-uri");
			jass = xml.getNode("jass");
			adminUsername = xml.getNode("fn-admin-user");
			adminPassword = xml.getNode("fn-admin-password");

		} catch (Exception e) {
			System.err.println("Got an exception in load()!: " + e.getMessage());
		} finally {
			xml.closeDocument();
		}
		
	}

	public String getContextFactory() {
		return contextFactory;
	}

	public String getUri() {
		return uri;
	}

	public String getUri2() {
		return uri2;
	}

	public String getObjectStore() {
		return objectStore;
	}

	public String getStanza() {
		return stanza;
	}

	public String getProcessEngineUri() {
		return processEngineUri;
	}

	public String getJass() {
		return jass;
	}

	public String getAdminUsername() {
		return adminUsername;
	}

	public String getAdminPassword() {
		return adminPassword;
	}
}
