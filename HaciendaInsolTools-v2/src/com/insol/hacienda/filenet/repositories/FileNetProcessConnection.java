package com.insol.hacienda.filenet.repositories;

import com.insol.hacienda.filenet.FileNetPropertiesFromXML;
import com.insol.utilities.SecureUser;

import RLib2.CEConnection;
import RLib2.PE2;
import RLib2.PEConnectionProperties;
import filenet.vw.api.VWSession;

public abstract class FileNetProcessConnection {
	
	protected SecureUser securedUser;
	protected FileNetPropertiesFromXML properties;
	protected PE2 processEngine;
	protected CEConnection contentEngine;
	protected VWSession session;
	
	public FileNetProcessConnection() {
		properties = new FileNetPropertiesFromXML(configLocation());
		processEngine = new PE2();
		contentEngine = new CEConnection();
	}
	
	public FileNetProcessConnection(String encodedUser) {
		properties = new FileNetPropertiesFromXML(configLocation());
		processEngine = new PE2();
		contentEngine = new CEConnection();
		
		session = getSession(encodedUser);
	}

	public abstract String configLocation();
	
	public VWSession getSession(String encodedUser) {
		securedUser = new SecureUser(encodedUser);
		return getSession(securedUser.getUsername(), securedUser.getPassword());
	}
	
	public VWSession getSession(String user, String password) {
		try {
			if (contentEngine.isConnected()) {
				return startProcessEngineSession(contentEngine);
			} else {
				contentEngine.establishConnection(user, password, properties.getStanza(), properties.getUri(), properties.getObjectStore());
				if (contentEngine.isConnected()) {
					return startProcessEngineSession(contentEngine);
				} else {
					System.out.println("No connection to Content Engine available on FileNetProcessConnection.getSession");
				}
			}
		} catch (Exception e) {
			System.err.println("Exception in FileNetProcessConnection.getSession: " + e.getMessage());
		}
		return null;
	}

	private VWSession startProcessEngineSession(CEConnection ceConnection) {
		try {
			
			if(session != null && session.isLoggedOn()){
				return session;
			} else {
				PEConnectionProperties peProps = new PEConnectionProperties();
				peProps.setJassPath(properties.getJass());
				peProps.setPassword(securedUser.getPassword());
				peProps.setpeURI(properties.getUri());
				peProps.setUserName(securedUser.getUsername());
				peProps.setRouterPath(properties.getProcessEngineUri());
				
				session = processEngine.logon(ceConnection.sub, peProps);
				
				if (session.isLoggedOn()) {
					return session;
				} else {
					System.err.println("No session started on FileNetProcessConnection.startProcessEngineSession");
				}
			}
			
		} catch (Exception e) {
			System.err.println("Exception in FileNetProcessConnection.startSessionFromProcessEngine: " + e.getMessage());
		}
		return null;
	}
}
