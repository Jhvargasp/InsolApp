package com.insol.utilities;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class XMLConfiguration {

	// Filenet config variables
	private String context_factory;
	private String puri;
	private String puri2;// for jboss
	private String active_directory_adminuser;
	private String active_directory_adminpass;
	private String OBJStore;
	private String Stanza;
	private String PEUri;
	private String Jass;
	private String fnet_admin_user;
	private String fnet_admin_pass;
	private String AppServer;

	// database config variables

	private String db_conexion_user;
	private String db_conexion_pass;
	private String db_conexion_server;
	private String db_conexion_name;
	private String db_conexion_driver;
	private String db_conexion_process_url;
	private String db_conexion_content_url;
	private String db_conexion_content_url2;

	// Miscellaneous settings
	// StepProcess
	private String save_buttonAndDispatch_name = "";
	private String save_close_button_name = "";
	private String close_notsave_button_name = "";
	private String open_attach_name = "";

	// queues and inbox

	private String QueuesColumns = "";
	private String ModalQueuesColumns = "";
	private String InitialGridVars = "";

	// For anotations and planillas pages

	private String parent_path = "";
	private String planillas_folder = "";

	// For returnToSource permissions

	private String PD_Groups_To_ReturnTS = "";
	private String DB_Groups_To_ReturnTS = "";
	private String AC_Groups_To_ReturnTS = "";

	// For user full data manipulation in NPPSupervisors

	private String NPPSupervisors_AdminGroup = "";

	public XMLConfiguration() {
		LoadFnetParams();
		LoadDBParams();
		LoadOtherSettings();

	}

	public XMLConfiguration(String Col) {

		LoadQueuesColumns();

	}

	public XMLConfiguration(int command) {

		switch (command) {
		case 1:
			LoadPlanillasSettings();
			break;

		case 2:
			LoadPlanillasSettings();
			LoadFnetParams();
			break;

		case 3:
			LoadPlanillasSettings();
			LoadFnetParams();
			LoadDBParams();
			break;
		case 4:
			LoadPlanillasSettings();
			LoadFnetParams();
			LoadDBParams();
			LoadOtherSettings();
			break;
		case 5:
			LoadGroupsPermissionToReturnToSource();

			break;
		case 6:
			NPPSupervisors_AdminGroup();
			break;

		}

	}

	public String getParent_path() {
		return parent_path;
	}

	public void setParent_path(String parent_path) {
		this.parent_path = parent_path;
	}

	public String getPlanillas_folder() {
		return planillas_folder;
	}

	public void setPlanillas_folder(String planillas_folder) {
		this.planillas_folder = planillas_folder;
	}

	public String getPD_Groups_To_ReturnTS() {
		return PD_Groups_To_ReturnTS;
	}

	public void setPD_Groups_To_ReturnTS(String pD_Groups_To_ReturnTS) {
		PD_Groups_To_ReturnTS = pD_Groups_To_ReturnTS;
	}

	public String getDB_Groups_To_ReturnTS() {
		return DB_Groups_To_ReturnTS;
	}

	public void setDB_Groups_To_ReturnTS(String dB_Groups_To_ReturnTS) {
		DB_Groups_To_ReturnTS = dB_Groups_To_ReturnTS;
	}

	public String getAC_Groups_To_ReturnTS() {
		return AC_Groups_To_ReturnTS;
	}

	public void setAC_Groups_To_ReturnTS(String aC_Groups_To_ReturnTS) {
		AC_Groups_To_ReturnTS = aC_Groups_To_ReturnTS;
	}

	public String getQueuesColumns() {

		return QueuesColumns;
	}

	public String getModalQueuesColumns() {
		return ModalQueuesColumns;
	}

	public String getInitialGridVars() {
		return InitialGridVars;
	}

	public void setNPPSupervisors_AdminGroup(String NPPSupervisors_AdminGroup) {
		NPPSupervisors_AdminGroup = NPPSupervisors_AdminGroup;
	}

	public String getSave_buttonAndDispatch_name() {
		return save_buttonAndDispatch_name;
	}

	public String getNPPSupervisors_AdminGroup() {
		return NPPSupervisors_AdminGroup;
	}

	private void LoadQueuesColumns() {

		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			DocumentBuilder builder = domFactory.newDocumentBuilder();

			Document doc = builder.parse("C:\\ConfigXML\\OtherSettings.xml");

			QueuesColumns = extractNodeValue(doc, "//queuescolumns/text()");
			ModalQueuesColumns = extractNodeValue(doc, "//modalqueuescolumns/text()");
			InitialGridVars = extractNodeValue(doc, "//initialgridvars/text()");

			domFactory = null;
			doc = null;

		} catch (Exception e) {
			System.out.println("Got an exception in LoadQueuesColumns()!: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void LoadGroupsPermissionToReturnToSource() {

		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			DocumentBuilder builder = domFactory.newDocumentBuilder();

			Document doc = builder.parse("C:\\ConfigXML\\OtherSettings.xml");

			PD_Groups_To_ReturnTS = extractNodeValue(doc, "//PD_Groups_To_ReturnTS/text()");
			DB_Groups_To_ReturnTS = extractNodeValue(doc, "//DB_Groups_To_ReturnTS/text()");
			AC_Groups_To_ReturnTS = extractNodeValue(doc, "//AC_Groups_To_ReturnTS/text()");
		} catch (Exception ex) {
			System.out.println("Got an exception in LoadGroupsPermissionToReturnToSource()!: " + ex.getMessage());
			ex.printStackTrace();

		}

	}

	private void LoadPlanillasSettings() {

		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			DocumentBuilder builder = domFactory.newDocumentBuilder();

			Document doc = builder.parse("C:\\ConfigXML\\OtherSettings.xml");

			parent_path = extractNodeValue(doc, "//parentpath/text()");
			planillas_folder = extractNodeValue(doc, "//planillasfolder/text()");

			domFactory = null;
			doc = null;

		} catch (Exception e) {
			System.out.println("Got an exception in LoadQueuesColumns()!: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void NPPSupervisors_AdminGroup() {

		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			DocumentBuilder builder = domFactory.newDocumentBuilder();

			Document doc = builder.parse("C:\\ConfigXML\\OtherSettings.xml");

			NPPSupervisors_AdminGroup = extractNodeValue(doc, "//NPPSupervisors_AdminGroup/text()");

			domFactory = null;
			doc = null;

		} catch (Exception e) {
			System.out.println("Got an exception in LoadQueuesColumns()!: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public String getParentPath() {
		return parent_path;
	}

	public void setParentPath(String parent_path) {
		this.parent_path = parent_path;
	}

	public String getPlanillasFolder() {
		return planillas_folder;
	}

	public void setPlanillasFolder(String planillas_folder) {
		this.planillas_folder = planillas_folder;
	}

	public void setQueuesColumns(String queuesColumns) {
		QueuesColumns = queuesColumns;
	}

	public void setModalQueuesColumns(String modalQueuesColumns) {
		ModalQueuesColumns = modalQueuesColumns;
	}

	public void setInitialGridVars(String initialGridVars) {
		InitialGridVars = initialGridVars;
	}

	private void LoadOtherSettings() {

		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			DocumentBuilder builder = domFactory.newDocumentBuilder();

			Document doc = builder.parse("C:\\ConfigXML\\OtherSettings.xml");

			save_buttonAndDispatch_name = extractNodeValue(doc, "//save_buttonAndDispatch_name/text()");
			;
			save_close_button_name = extractNodeValue(doc, "//save_close_button_name/text()");
			close_notsave_button_name = extractNodeValue(doc, "//close_notsave_button_name/text()");
			open_attach_name = extractNodeValue(doc, "//open_attach_name/text()");

			domFactory = null;
			doc = null;

		} catch (Exception e) {
			System.out.println("Got an exception in LoadOtherSettings()!: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void LoadFnetParams() {

		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			DocumentBuilder builder = domFactory.newDocumentBuilder();

			Document doc = builder.parse("C:\\ConfigXML\\FilenetVars.xml");

			context_factory = extractNodeValue(doc, "//context_factory/text()");
			puri = extractNodeValue(doc, "//puri/text()");
			puri2 = extractNodeValue(doc, "//puri2/text()");
			active_directory_adminuser = extractNodeValue(doc, "//active_directory_adminuser/text()");
			active_directory_adminpass = extractNodeValue(doc, "//active_directory_adminpass/text()");
			OBJStore = extractNodeValue(doc, "//OBJStore/text()");
			Stanza = extractNodeValue(doc, "//Stanza/text()");
			PEUri = extractNodeValue(doc, "//PEUri/text()");
			Jass = extractNodeValue(doc, "//Jass/text()");
			fnet_admin_user = extractNodeValue(doc, "//fnet_admin_user/text()");
			fnet_admin_pass = extractNodeValue(doc, "//fnet_admin_pass/text()");
			AppServer = extractNodeValue(doc, "//AppServer/text()");

			domFactory = null;
			doc = null;

		} catch (Exception e) {
			System.out.println("Got an exception in LoadFnetParams()!: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void LoadDBParams() {

		try {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
			DocumentBuilder builder = domFactory.newDocumentBuilder();

			Document doc = builder.parse("C:\\ConfigXML\\DataBaseVars.xml");
			db_conexion_user = extractNodeValue(doc, "//db_conexion_user/text()");
			db_conexion_pass = extractNodeValue(doc, "//db_conexion_pass/text()");
			db_conexion_server = extractNodeValue(doc, "//db_conexion_server/text()");
			db_conexion_name = extractNodeValue(doc, "//db_conexion_name/text()");
			db_conexion_driver = extractNodeValue(doc, "//db_conexion_driver/text()");
			db_conexion_process_url = extractNodeValue(doc, "//db_conexion_process_url/text()");
			db_conexion_content_url = extractNodeValue(doc, "//db_conexion_content_url/text()");
			db_conexion_content_url2 = extractNodeValue(doc, "//db_conexion_content_url2/text()");

			// fDomainOrigen = extractNodeValue(doc, "//fDomainOrigen/text()");

			domFactory = null;
			doc = null;

		} catch (Exception e) {
			System.out.println("Got an exception in LoadDBParams()!: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// verificar esta parte
	private String extractNodeValue(Document doc, String expression) {

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		Node theNode = null;

		try {
			XPathExpression xpex = xpath.compile(expression);
			theNode = (Node) xpex.evaluate(doc, XPathConstants.NODE);
			xpex = null;
		} catch (XPathExpressionException xpee) {
			System.out.println("Got an exception!: " + xpee.getMessage());
			xpee.printStackTrace();
		}

		factory = null;
		xpath = null;

		if (theNode != null) {
			return theNode.getNodeValue();
		} else {
			return "";
		}
	}

	public void setSave_buttonAndDispatch_name(String save_buttonAndDispatch_name) {
		this.save_buttonAndDispatch_name = save_buttonAndDispatch_name;
	}

	public String getSave_close_button_name() {
		return save_close_button_name;
	}

	public void setSave_close_button_name(String save_close_button_name) {
		this.save_close_button_name = save_close_button_name;
	}

	public String getClose_notsave_button_name() {
		return close_notsave_button_name;
	}

	public void setClose_notsave_button_name(String close_notsave_button_name) {
		this.close_notsave_button_name = close_notsave_button_name;
	}

	public String getOpen_attach_name() {
		return open_attach_name;
	}

	public void setOpen_attach_name(String open_attach_name) {
		this.open_attach_name = open_attach_name;
	}

	public String getContext_factory() {
		return context_factory;
	}

	public void setContext_factory(String context_factory) {
		this.context_factory = context_factory;
	}

	public String getPuri() {
		return puri;
	}

	public void setPuri(String puri) {
		this.puri = puri;
	}

	public String getPuri2() {
		return puri2;
	}

	public void setPuri2(String puri2) {
		this.puri2 = puri2;
	}

	public String getADAdminUsername() {
		return active_directory_adminuser;
	}

	public void setActive_directory_adminuser(String active_directory_adminuser) {
		this.active_directory_adminuser = active_directory_adminuser;
	}

	public String getADAdminPassword() {
		return active_directory_adminpass;
	}

	public void setActive_directory_adminpass(String active_directory_adminpass) {
		this.active_directory_adminpass = active_directory_adminpass;
	}

	public String getOBJStore() {
		return OBJStore;
	}

	public void setOBJStore(String oBJStore) {
		OBJStore = oBJStore;
	}

	public String getStanza() {
		return Stanza;
	}

	public void setStanza(String stanza) {
		Stanza = stanza;
	}

	public String getPEUri() {
		return PEUri;
	}

	public void setPEUri(String pEUri) {
		PEUri = pEUri;
	}

	public String getJass() {
		return Jass;
	}

	public void setJass(String jass) {
		Jass = jass;
	}

	public String getFnet_admin_user() {
		return fnet_admin_user;
	}

	public void setFnet_admin_user(String fnet_admin_user) {
		this.fnet_admin_user = fnet_admin_user;
	}

	public String getFnet_admin_pass() {
		return fnet_admin_pass;
	}

	public void setFnet_admin_pass(String fnet_admin_pass) {
		this.fnet_admin_pass = fnet_admin_pass;
	}

	public String getDb_conexion_user() {
		return db_conexion_user;
	}

	public void setDb_conexion_user(String db_conexion_user) {
		this.db_conexion_user = db_conexion_user;
	}

	public String getDb_conexion_pass() {
		return db_conexion_pass;
	}

	public void setDb_conexion_pass(String db_conexion_pass) {
		this.db_conexion_pass = db_conexion_pass;
	}

	public String getDb_conexion_server() {
		return db_conexion_server;
	}

	public void setDb_conexion_server(String db_conexion_server) {
		this.db_conexion_server = db_conexion_server;
	}

	public String getDb_conexion_name() {
		return db_conexion_name;
	}

	public void setDb_conexion_name(String db_conexion_name) {
		this.db_conexion_name = db_conexion_name;
	}

	public String getDb_conexion_driver() {
		return db_conexion_driver;
	}

	public void setDb_conexion_driver(String db_conexion_driver) {
		this.db_conexion_driver = db_conexion_driver;
	}

	public String getDb_conexion_process_url() {
		return db_conexion_process_url;
	}

	public void setDb_conexion_process_url(String db_conexion_process_url) {
		this.db_conexion_process_url = db_conexion_process_url;
	}

	public String getDb_conexion_content_url() {
		return db_conexion_content_url;
	}

	public void setDb_conexion_content_url(String db_conexion_content_url) {
		this.db_conexion_content_url = db_conexion_content_url;
	}

	public String getDb_conexion_content_url2() {
		return db_conexion_content_url2;
	}

	public void setDb_conexion_content_url2(String db_conexion_content_url2) {
		this.db_conexion_content_url2 = db_conexion_content_url2;
	}

	public String getAppServer() {
		return AppServer;
	}

	public void setAppServer(String appServer) {
		AppServer = appServer;
	}

}
