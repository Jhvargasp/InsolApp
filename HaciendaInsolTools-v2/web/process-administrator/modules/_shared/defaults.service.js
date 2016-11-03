(function(){
	'use-strict';
	
	angular.module('app').service('DefaultsService', DefaultsService);
	
	DefaultsService.$inject = ['ApplicationService'];
	
	function DefaultsService(s){
		
		var service = this;
			
		service.vmForm = getFormDefaults;
		service.vmObject = getViewObjectDefaults;
		service.PAForm = getPAFormDefaults;
				
		//
			
		function getFormDefaults(){
			
			var form = {};
			form.loading = false;
			form.submitted = false;
			form.error = false;		
			return form;
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
		
		
		function getPAFormDefaults(){
			
			var form = getFormDefaults();
			form.quantity = 50;
			form.year = new Date().getFullYear() - 1;
			form.operatorsList = s.operatorsList();
			form.operator = 'equal';
			
			return form;
		}
		
		
	}
	
})();