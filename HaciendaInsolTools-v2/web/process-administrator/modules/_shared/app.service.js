(function(){
	'use-strict';
	
	angular.module('app').service('ApplicationService', ApplicationService);
	
	ApplicationService.$inject = ['$cookies', '$http'];
	
	function ApplicationService($cookies, $http){
				
		var service = this;
		var root = window.location.pathname.split('/')[1];
		var apiUrl = '/' + root + '/api/';
		
		// Service Declaration
		
		service.setSession = setSession;
		service.getSession = getSession;
		service.getUser = getUser;
		service.getProcess = getProcess;
		
		service.getQueues = getQueues;
		service.getRosters = getRosters;
		service.getUsers = getUsers;
		
		service.getFields = getFields;
		service.getIndexes = getIndexes;
		
		service.search = search;
		service.setElement = setElement;
		service.unlockElement = unlockElement;
		
		service.getFormDefaults = getFormDefaults;
		service.getViewObjectDefaults = getViewObjectDefaults;
		service.operatorsList = operatorsList;
		
		// Promise Holders
		
		var queuesPromise;
		var rostersPromise;
		var operatorsPromise;
		var usersPromise;
			
		// Service Implementations
		
		function setSession(user, process){
			$cookies.put('_u', user);
			$cookies.put('_p', process);
		}
		
		function getSession(){
			var u = $cookies.get('_u');
			var p = $cookies.get('_p');
			if(u !== undefined && p !== undefined){
				return {user: u, process : p};				
			}
			return {};
		}
		
		function getUser(){
			var u = $cookies.get('_u');
			if(u !== undefined){
				return u;
			}
			return '';
		}
		
		function getProcess(){
			var p = $cookies.get('_p');
			if(p !== undefined){
				return p;
			}
			return '';
		}
		
				
		function getQueues(){
			if(!queuesPromise){	
				queuesPromise = $http.get(apiUrl + 'pa/queues/');
			}
			return queuesPromise;
		}
		
		function getRosters(){		
			if(!rostersPromise){	
				rostersPromise = $http.get(apiUrl + 'pa/rosters/');
			} 
			return rostersPromise;
		}
		
		function getUsers(){			
			if(!usersPromise){	
				usersPromise = $http.get(apiUrl + 'pa/users/');
			} 
			return usersPromise;
		}
		
		function getFields(type, item){		
			return $http.get(apiUrl + 'pa/'+ type + 's/fields/?item=' + item);
		}

		function getIndexes(type, item){		
			return $http.get(apiUrl + 'pa/'+ type + 's/indexes/?item=' + item);
		}
		
		
		function search(type, form){
			return $http.post(apiUrl + 'pa/search/' + type + 's', form);
		}
		
		function setElement(type, forms){
			//console.log('element list', forms);
			return $http.post(apiUrl + 'pa/set/' + type, forms);
		}
		
		function unlockElement(type, forms){
			//console.log('element list', forms);
			return $http.post(apiUrl + 'pa/unlock/' + type, forms);
		}
		
		///////////////////////////////////////////////////////////////////////
		//
		//	Helpers
		//
		///////////////////////////////////////////////////////////////////////
		
		
		function getFormDefaults(){
			
			var form = {};
			form.quantity = 50;
			form.year = new Date().getFullYear() - 1;
			form.loadingMsg = '';
			form.submitted = false;
			form.operatorsList = operatorsList();
			form.operator = 'equal';
		
			return form;
		}
		
		function operatorsList(){
			
			var operators = [
				{ key : 'equal', label : 'sea igual que (=)' }, 
				{ key : 'slower_than', label : 'sea menor que (<)' }, 
				{ key : 'lower_or_equal', label : 'sea menor o igual que (<=)' }, 
				{ key : 'greater_than', label : 'sea mayor que (>)' }, 
				{ key : 'greater_or_equal', label : 'sea mayor o igual que (>=)' }, 
				{ key : 'not', label : 'que no sea (<>)' }, 
				{ key : 'contains', label : 'que contenga' }, 
				{ key : 'starts_with', label : 'que empiece con' }, 
				{ key : 'ends_with', label : 'que termine con' }, 
			];
			
			return operators;
		}
	
		function getViewObjectDefaults(name){
			
			var obj = {};
			obj.loading = false;
			obj.error = false;
			obj.list = [];
			if(name !== undefined){
				obj.get = 'get' + name.charAt(0).toUpperCase() + name.slice(1) + 's';
				obj.save = 'save' + name.charAt(0).toUpperCase() + name.slice(1) + 's';	
				obj.remove = 'remove' + name.charAt(0).toUpperCase() + name.slice(1) + 's';	
			}
			
			return obj;
		}
	}
	
})();