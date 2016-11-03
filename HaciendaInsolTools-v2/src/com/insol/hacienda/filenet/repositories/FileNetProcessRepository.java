package com.insol.hacienda.filenet.repositories;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.insol.hacienda.processAdministrator.repositories.HaciendaDatabaseRepository;
import com.insol.utilities.ReFormat;

import filenet.vw.api.VWAttachment;
import filenet.vw.api.VWException;
import filenet.vw.api.VWParameter;
import filenet.vw.api.VWParticipant;
import filenet.vw.api.VWRosterElement;
import filenet.vw.api.VWSession;
import filenet.vw.api.VWStepElement;

public abstract class FileNetProcessRepository extends FileNetProcessConnection{

	private HaciendaDatabaseRepository haciendaRepo;
	private ArrayList<HashMap<String, String>> elementsList = new ArrayList<>();
	private String elementFilter = "";
		
	public static enum ITEM_TYPES {ROSTERS, QUEUES}
	
	public ArrayList<String> getItems(ITEM_TYPES type, String encodedUser) {

		try {
			
			session = getSession(encodedUser);
			String[] _list = null;
			
			if (session != null) {				
				switch (type) {
				
				case QUEUES:	
					_list = processEngine.getUserQueue(session, false);
					break;
	
				case ROSTERS:
					_list = processEngine.getUserRoster(session, false);
					break;
				}	
				
				if (_list != null && _list.length > 0) {
					return new ArrayList<String>(Arrays.asList(_list));
				}
			}

		} catch (Exception e) {
			System.err.println("Exception in FileNetRepository.getItems (" + type + "): " + e.getMessage());
		}

		return null;
	}
	
	public ArrayList<String> getFieldsBy(ITEM_TYPES type, String encodedUser, String item) {
			
		try {
			session = getSession(encodedUser);
			List<String> fieldList = new ArrayList<>();
			
			if (session != null) {
				
				switch (type) {
				case QUEUES:
					fieldList = processEngine.getQueueFields(session, item);
					break;
				case ROSTERS:
					fieldList = processEngine.getRosterFields(session, item);
					break;

				}

				if (fieldList != null && fieldList.size() > 0) {
					return (ArrayList<String>) fieldList;
				}

			}

		} catch (Exception e) {
			System.err.println("Exception in FileNetRepository.getFieldsBy (" + type +"): " + e.getMessage());
		}

		return null;
	}
	
	public ArrayList<String> getIndexesBy(ITEM_TYPES type, String encodedUser, String item) {
		
		try {
			List<String> indexes = new ArrayList<>();
			
			switch (type) {
			case QUEUES:
				indexes = (ArrayList<String>) processEngine.QueueIndexes(getSession(encodedUser), item);	
				break;

			case ROSTERS:
				indexes = (ArrayList<String>) processEngine.RosterIndexes(getSession(encodedUser), item);	
				break;
			}

			if(indexes != null && indexes.size() > 0){
				return (ArrayList<String>) indexes;
			}
			
		} catch (Exception e) {
			System.err.println("Exception in FileNetRepository.getIndexesBy (" + type +"): " + e.getMessage());
		}
		return null;
	}
	
	public ArrayList<HashMap<String,String>> getElementsBy(ITEM_TYPES type, String encodedUser, String process, HashMap<String, Object> form) {
		
		try {		
			session = getSession(encodedUser);
			haciendaRepo = new HaciendaDatabaseRepository();
			elementsList = new ArrayList<>();
			elementFilter = ""; 
			
			String index = "";
			String extraFilter = haciendaRepo.getFilterByProcess(process);
			
			if(form.containsKey("index")){
				index = form.get("index").toString();
			}
						
			switch (type) {
			case QUEUES:
			
				elementFilter = FileNetHelper.getWhereFilter(form, getFieldTypeBy(session, ITEM_TYPES.QUEUES, form));
				elementFilter = FileNetHelper.getAndFilter(elementFilter, extraFilter);
				elementFilter = FileNetHelper.getAndFilter(elementFilter, form.get("year").toString(), "AñoContributivo");
						
				for (VWStepElement element : processEngine.getQueueElementsByFilter(session, elementFilter, form.get("queue").toString(), index, FileNetHelper.getQuantity(form))) {
					elementsList.add(getSimpleQueueElement(element));
				}
				
				return elementsList;
				
			case ROSTERS:

				elementFilter = FileNetHelper.getWhereFilter(form, getFieldTypeBy(session, ITEM_TYPES.ROSTERS, form));
				elementFilter = FileNetHelper.getAndFilter(elementFilter, extraFilter);
				elementFilter = FileNetHelper.getAndFilter(elementFilter, form.get("year").toString(), "AñoContributivo");
							
				for (VWRosterElement element : processEngine.getRosterElementbyFilter(session, form.get("roster").toString(), elementFilter, index, FileNetHelper.getQuantity(form))) {
					elementsList.add(getSimpleRosterElement(element));
				}
				
				return elementsList;
			}
			
		
			
		} catch (Exception e) {
			System.err.println("Exception in FileNetRepository.getElementsBy (" + type +"): " + e.getMessage());
		}	
		 
		return null;
	}
	
	public boolean setTasksFrom(ITEM_TYPES type, String encodedUser, ArrayList<HashMap<String,Object>> formList) {

		boolean result = false;
		try {
		
			session = getSession(encodedUser);
			for (HashMap<String, Object> form : formList) {
			
				try {
					
					VWStepElement step = null;
					VWRosterElement stepRoster = null;
					
	
					String roster = form.get("roster").toString();
					String wobNum = form.get("wobNum").toString();
					String participant = form.get("user").toString();
					String queue = form.get("queue").toString();
	
					if(type.equals(ITEM_TYPES.ROSTERS)){
						stepRoster = processEngine.setRosterElementbyWOBNum(session, roster, wobNum);
						step = stepRoster.fetchStepElement(true, true);				
					} else {
						step = processEngine.setQueueElementbyWOBNum(session, queue,wobNum, true, true);
					}
	
					if (step.getCanReassign()) {
						step.doReassign(participant, false, queue);
						result = true;
					} else {
						step.doSave(true);	
					}
					
				} catch (Exception e) {
					System.err.println("Exception in iteration FileNetRepository.setTaskFrom (" + type + "): " + e.getMessage());
				}
				
			}
			
		} catch (Exception e) {
			System.err.println("Exception in FileNetRepository.setTaskFrom (" + type + "): " + e.getMessage());
		}
			
		return result;
	}
	
	public int unlockTasksFrom(ITEM_TYPES type, String encodedUser, ArrayList<HashMap<String,Object>> formList) {

		int result = 0;
		try {

			session = getSession(encodedUser);
			if (session.isLoggedOn()) {

				List<VWStepElement> stepElements = new ArrayList<VWStepElement>();
				String filter = "F_WobNum in (";
				String roster = "";

				for (int i = 0; i < formList.size(); i++) {
					HashMap<String, Object> form = formList.get(i);

					filter += "0x" + form.get("wobNum").toString();
					roster = form.get("roster").toString();

					if (formList.size() > 1 && i < formList.size() - 1) {
						filter += ", ";
					}
				}
				filter += ")";

				if (type.equals(ITEM_TYPES.ROSTERS)) {

					List<VWRosterElement> rosterElements = processEngine.getRosterElementbyFilter(session, roster, filter, 500);

					for (int i = 0; i < rosterElements.size(); i++) {
						stepElements.add(rosterElements.get(i).fetchStepElement(false, false));
					}

					VWException[] exceptions = VWStepElement.doUnlockMany(stepElements.toArray(new VWStepElement[0]),true, false);
					

					for (VWException exception : exceptions) {
						if(exception == null){
							result ++;
						} 
					}


				} else {

				}

			}

		} catch (Exception e) {
			System.err.println("Exception in FileNetRepository.unlockTasksFrom (" + type + "): " + e.getMessage());
		}

		return result;
	}
	
	///////////////////////////////////////////////////////////////////////////
	//
	//	Helpers
	//
	///////////////////////////////////////////////////////////////////////////
	
	private HashMap<String, String> getSimpleQueueElement(VWStepElement stepElement) {

		try {

			HashMap<String, String> simpleElement = new HashMap<>();

			///////////////////////////////////////////////////////////////
			//
			//	Get Values
			//
			///////////////////////////////////////////////////////////////
			
			VWParameter planilla = stepElement.getParameter("Planilla");
			VWAttachment planillaAttachment = (VWAttachment) planilla.getValue();
			VWParticipant userLock = stepElement.getLockedUserPx();
			String documentId = planillaAttachment.getId();
			String documentVersionId = planillaAttachment.getVersion();
			
			///////////////////////////////////////////////////////////////
			//
			//	Fill HashMap
			//
			///////////////////////////////////////////////////////////////
			
			simpleElement.put("documentId", documentId);
			simpleElement.put("documentVersionId", documentVersionId);
			simpleElement.put("F_WobNum", stepElement.getWorkflowNumber());
			if (userLock != null) {
				simpleElement.put("participant", userLock.getParticipantName());
			} else {
				simpleElement.put("participant", "");
			}
			
			for (String field : stepElement.getParameterNames()) {
				
				if (field.equals("F_StartTime")) {
					Date LaunchDate = stepElement.getLaunchDate();
					simpleElement.put(field,  DateFormat.getInstance().format(LaunchDate));
				}
				
				else if (field.equals("AñoContributivo")) {
					simpleElement.put("taxYear",  stepElement.getParameterValue(field).toString());
					simpleElement.put(field,  stepElement.getParameterValue(field).toString());
				}

				else {
					simpleElement.put(field, stepElement.getParameterValue(field).toString());
				}
			}
			
			return simpleElement;

		} catch (Exception e) {
			System.err.println("Exception parsing roster element in getSimpleQueueElement: " + e.getMessage());
		}

		return null;
	}

	private HashMap<String, String> getSimpleRosterElement(VWRosterElement rosterElement) {

		try {

			HashMap<String, String> simpleElement = new HashMap<>();
			VWStepElement stepElement = rosterElement.fetchStepElement(false, false);
			String queue = stepElement.getCurrentQueueName();

			if (!queue.equals("Conductor") && !queue.equals("CE_Operations")) {

				///////////////////////////////////////////////////////////////
				//
				//	Get Values
				//
				///////////////////////////////////////////////////////////////
				
				VWParameter planilla = stepElement.getParameter("Planilla");
				VWAttachment planillaAttachment = (VWAttachment) planilla.getValue();
				VWParticipant userLock = stepElement.getLockedUserPx();

				String user = stepElement.getParticipantName();
				String originator = rosterElement.getOriginator();
				String documentId = planillaAttachment.getId();
				String documentVersionId = planillaAttachment.getVersion();

				///////////////////////////////////////////////////////////////
				//
				//	Fill HashMap
				//
				///////////////////////////////////////////////////////////////
				
				simpleElement.put("documentId", (documentId != null) ? documentId : "");
				simpleElement.put("documentVersionId", (documentVersionId != null) ? documentVersionId : "");
				if (userLock != null) {
					simpleElement.put("participant", userLock.getParticipantName());
				} else {
					simpleElement.put("participant", "");
				}
				simpleElement.put("queue", queue);
				simpleElement.put("user", (user != null ) ? user : "");

				for (String field : rosterElement.getFieldNames()) {

					if (field.equals("F_Originator")) {
						simpleElement.put(field, originator);
					}

					else if (field.equals("F_StartTime")) {
						
						if(rosterElement.getFieldValue(field) != null){
							Date LaunchDate = (Date) rosterElement.getFieldValue(field);
							simpleElement.put(field,  DateFormat.getInstance().format(LaunchDate));	
						} else {
							simpleElement.put(field,  "");	
						}
					}
					
					else if (field.equals("AñoContributivo")) {
						if(rosterElement.getFieldValue(field) != null){
							simpleElement.put("taxYear", rosterElement.getFieldValue(field).toString());
							simpleElement.put(field, rosterElement.getFieldValue(field).toString());							
						} else {
							simpleElement.put(field,  "");	
						}

					}


					else {
						if(rosterElement.getFieldValue(field) != null){
							simpleElement.put(field, rosterElement.getFieldValue(field).toString());							
						} else {
							simpleElement.put(field,  "");	
						}
					}
				}

				return simpleElement;
			}

		} catch (Exception e) {
			System.err.println("Exception parsing roster element in getSimpleRosterElement: " + e.getMessage());
		}

		return null;
	}

	private String getFieldTypeBy(VWSession session, ITEM_TYPES type, HashMap<String, Object> form) {
		
		try {
			String field = form.get("field").toString();
			HashMap<String,Integer> fieldTypes = new HashMap<>();
			String itemFieldType = null;
			
			if (session != null) {
				
				switch (type) {
				case QUEUES:
					fieldTypes = processEngine.getQueueFieldsAndType(session, form.get("queue").toString());
					break;
				case ROSTERS:
					fieldTypes = processEngine.getRosterFieldsAndType(session, form.get("roster").toString());
					break;

				}
				
				if(fieldTypes != null && fieldTypes.size() > 0 && fieldTypes.get(field) != null){
					
					switch (fieldTypes.get(field)) {
					case 1:
						itemFieldType = "integer";
						break;
					case 2:
						itemFieldType = "string";
						break;
					case 4:
						itemFieldType = "boolean";
						break;
					case 8:
						itemFieldType = "float";
						break;
					case 16:
						itemFieldType = "time";
						break;
					case 256:
						itemFieldType = "time64";
						break;

					}
					
					return itemFieldType;
				}

			}

		} catch (Exception e) {
			System.err.println("Exception in FileNetRepository.getFieldTypeBy (" + type + "): " + e.getMessage());
		}

		return null;
	}
				
}


