package com.insol.hacienda.processAdministrator.routes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.api.toolkit.cookie.CookieManager;
import com.api.toolkit.route.Route;
import com.insol.hacienda.activeDirectory.repositories.HaciendaActiveDirectoryRepository;
import com.insol.hacienda.filenet.repositories.FileNetProcessRepository.ITEM_TYPES;
import com.insol.hacienda.filenet.repositories.HaciendaFileNetRepository;
import com.insol.utilities.ReFormat;

@WebServlet("/api/pa/*")
public class DefaultRoutes extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Route route = new Route(request, response);
		CookieManager cookies = new CookieManager(request, response);
		
		String user = route.getParam("user");
		String process = route.getParam("process");
		
		String userCookie = cookies.get("_u");
		String processCookie = cookies.get("_p");
		
		HaciendaFileNetRepository filenet = new HaciendaFileNetRepository();
		
		try {
			
			switch (route.getAction()) {
				
			case "init/":
							
				if(!user.isEmpty() && !process.isEmpty()){
					
					cookies.set("_u", user);
					cookies.set("_p", process);
					
				}
				
				String home = request.getContextPath() + "/process-administrator";
				response.sendRedirect(home);
				
				break;
			case "queues/":
				
				if(!userCookie.isEmpty() && !processCookie.isEmpty()){	
		
					ArrayList<String> queues = filenet.getItems(ITEM_TYPES.QUEUES, userCookie);		
					if(queues != null){
						route.responseOk(ReFormat.toItemList(queues, false));
					}
					
				}

				break;
			case "queues/fields/":
				
				if(!userCookie.isEmpty() && !processCookie.isEmpty()){	
		
					String q = route.getParam("item");
					
					if(!q.isEmpty()){
						ArrayList<String> fields = filenet.getFieldsBy(ITEM_TYPES.QUEUES, userCookie, q);	
						if(fields != null){
							route.responseOk(ReFormat.toItemList(fields, false));
						}	
					}		
				}

				break;
			case "queues/indexes/":
				
				if(!userCookie.isEmpty() && !processCookie.isEmpty()){	
		
					String q = route.getParam("item");
					
					if(!q.isEmpty()){
						ArrayList<String> indexes = filenet.getIndexesBy(ITEM_TYPES.QUEUES, userCookie, q);
						if(indexes != null){
							route.responseOk(ReFormat.toItemList(indexes, false));						
						}						
					}
				}

				break;
			case "rosters/":
				
				if(!userCookie.isEmpty() && !processCookie.isEmpty()){	
	
					ArrayList<String> rosters = filenet.getItems(ITEM_TYPES.ROSTERS, userCookie);
					if(rosters != null){
						route.responseOk(ReFormat.toItemList(rosters));
					}
					
				}
				
				break;
			case "rosters/fields/":
				
				if(!userCookie.isEmpty() && !processCookie.isEmpty()){	
		
					String r = route.getParam("item");
					
					if(!r.isEmpty()){
						ArrayList<String> fields = filenet.getFieldsBy(ITEM_TYPES.ROSTERS, userCookie, r);
						if(fields != null){
							route.responseOk(ReFormat.toItemList(fields, false));						
						}						
					}
				}

				break;
			case "rosters/indexes/":
				
				if(!userCookie.isEmpty() && !processCookie.isEmpty()){	
		
					String r = route.getParam("item");
					
					if(!r.isEmpty()){
						ArrayList<String> indexes = filenet.getIndexesBy(ITEM_TYPES.ROSTERS, userCookie, r);
						if(indexes != null){
							route.responseOk(ReFormat.toItemList(indexes, false ));						
						}	
					}					
				}

				break;
			case "users/":
				
				if(!userCookie.isEmpty() && !processCookie.isEmpty()){	
					
					HaciendaActiveDirectoryRepository adRepository = new HaciendaActiveDirectoryRepository();
					ArrayList<HashMap<String,String>> users = adRepository.getUsersByProcess(processCookie);
					
					if(users != null){
						route.responseOk(users);		
					}
									
				}
				
				break;
			}
		
		} catch (Exception e) {
			
			System.err.println("Error in default routes GET (" + route.getAction() + "): " + e.getMessage());
			route.responseError("");
			
		} 
		
		route.responseNotFound("");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		Route route = new Route(request, response);
		CookieManager cookies = new CookieManager(request, response);
		
		ArrayList<HashMap<String, Object>> forms = null;
		
		String userCookie = cookies.get("_u");
		String processCookie = cookies.get("_p");
		
		ArrayList<HashMap<String,String>> results = new ArrayList<>();
		
		HaciendaActiveDirectoryRepository adRepository = new HaciendaActiveDirectoryRepository();
		HaciendaFileNetRepository filenetRepo = new HaciendaFileNetRepository();
		
		if(!userCookie.isEmpty() && !processCookie.isEmpty()){	
			
			try {
				
				switch (route.getAction()) {
				
				case "search/queues" :
					
					results = filenetRepo.getElementsBy(ITEM_TYPES.QUEUES, userCookie, processCookie, route.getParamsFromJsonObject());
					
					if(results != null){
						route.responseOk(results);
					} else {
						route.responseNoContent();
					}
					
					break;
					
				case "search/rosters" :
			
					results = filenetRepo.getElementsBy(ITEM_TYPES.ROSTERS, userCookie, processCookie, route.getParamsFromJsonObject());
					
					if(results != null){
						route.responseOk(results);
					} else {
						route.responseNoContent();
					}
					
					break;
				case "set/roster" :
				
					forms = route.getParamsFromJsonArray();
					
					if(forms != null && forms.size() > 0){
						boolean result = filenetRepo.setTasksFrom(ITEM_TYPES.ROSTERS, userCookie, forms);
						
						if(result){
							route.responseOk(result);
						} else {
							route.responseNoContent();
						}
					}
										
				break;
				case "unlock/roster" :
					
					forms = route.getParamsFromJsonArray();
					
					if(forms != null && forms.size() > 0){
						int result = filenetRepo.unlockTasksFrom(ITEM_TYPES.ROSTERS, userCookie, forms);

						route.responseOk(result);

					}
										
				break;
			
			}
			} catch(Exception e){
				
				System.err.println("Error in default routes POST (" + route.getAction() + "): " + e.getMessage());
				route.responseError("");
				
			} 
			
		}
		
		route.responseNotFound("");
		
	}
}
