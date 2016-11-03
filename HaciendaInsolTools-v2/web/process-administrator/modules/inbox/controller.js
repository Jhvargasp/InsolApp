(function(){
	'use-strict';
	
	angular.module('app').controller('InboxController', InboxController);
	
	InboxController.$inject = ['ApplicationService', 'DefaultsService'];
	
	function InboxController(s, defaults){
		
		//Declarations 
		
		var vm = this;
		
		vm.users 	= defaults.vmObject('user');
		vm.fields 	= defaults.vmObject('field');
		vm.indexes 	= defaults.vmObject('index');
		vm.elements = defaults.vmObject('element');
		
		vm.form = defaults.PAForm();
		
		// Init
		
		init();
		
		// Implementations
		
		function init(){
		
			getUsers();

		}
		
		
		function getUsers(){
			vm.users.loading = true;
			s.getUsers().then(function(response){
				if(response.data != undefined){
					vm.users.loading = false;
					vm.users.list = response.data;
				}
			});
		}

	}
	
})();