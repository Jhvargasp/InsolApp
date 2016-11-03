(function(){
	'use-strict';
	
	angular.module('app').controller('RosterController', RosterController);
	
	RosterController.$inject = ['ApplicationService', 'DefaultsService', '$filter'];
	
	function RosterController(s, defaults, $filter){
		
		// Declarations
		
		var vm = this;
		
		vm.rosters = defaults.vmObject('roster');
		vm.fields = defaults.vmObject('field');
		vm.indexes = defaults.vmObject('index');
		
		vm.elements = defaults.vmObject('element');
		vm.elements.updateList = [];
		vm.elements.allUpdate = false;
		vm.elements.addAllToUpdate = addAllElementsToUpdate;
		vm.elements.addToUpdateList = addElementToUpdateList;
		vm.elements.updateListShowQuantity = 5;
		
		vm.users = defaults.vmObject('user');
		vm.getUsername = getUsername;
		
		vm.form = defaults.PAForm();
		
		vm.modal = {};
		vm.modal.element = {};
		
		vm.modal.element.assign = {};
		vm.modal.element.assign.set = setElement;
		vm.modal.element.assign.user = {};
		vm.modal.element.assign.error = false;
		vm.modal.element.assign.success = false;
		vm.modal.element.assign.loading = false;
		vm.modal.element.assign.noUser = false;
		
		vm.modal.element.unlock = {};
		vm.modal.element.unlock.set = unlockElement;
		vm.modal.element.unlock.loading = false;
		
		vm.modal.results = {};
		vm.modal.results.success = false;
		vm.modal.results.error = false;
		vm.modal.results.warning = false;
		vm.modal.results.msg = "";
		
		vm.modal.element.complete = {};

		vm.modal.open = openModal;
		vm.modal.setElement = setElement;
		
		vm.loadAdditional = loadAdditional; 
		vm.searchRosters = search;
		
		// Inititation
		
		init();
		
		// Declarations
		
		
		function init(){
			
			getRosters();	
	
		}
		
		function loadAdditional(queue){
			getFields(queue);
			getIndexes(queue);
		}
		
		function getRosters(){
			vm.rosters.loading = true;
			vm.rosters.error = false;
			
			vm.form.loading = true;
			
			getUsers();
			
			s.getRosters().then(function(response){
				vm.rosters.loading = false;
				vm.form.loading = false;
				if(response.data != undefined && response.data.length > 0){
					vm.rosters.list = response.data;
					
				} else {
					vm.rosters.error = true;
				}
			}, function error(eror){
				console.log('getRosters error', error);
				
				vm.form.loading = false;
				
				vm.rosters.loading = false;
				vm.rosters.error = true;
			});
		}
		
		function getFields(roster){
			vm.fields.loading = true;
			
			vm.form.loading = true;
			
			s.getFields('roster', roster).then(function(response){
				if(response.data !== undefined){
					
					vm.form.loading = false;
					
					vm.fields.loading = false;
					vm.fields.list = response.data;
				}
			});
		}
		
		function getIndexes(roster){
			vm.indexes.loading = true;
			
			vm.form.loading = true;
			
			s.getIndexes('roster', roster).then(function(response){
				if(response.data !== undefined){
					
					vm.form.loading = false;
					
					vm.indexes.loading = false;
					vm.indexes.list = response.data;
				}
			});
		}
		
		function search(){
			vm.form.loading = true;			
			vm.elements.success = false;
			vm.elements.loading = true;
			vm.elements.error = false;
			s.search('roster', vm.form).then(function(response){

				vm.form.loading = false;
				vm.elements.loading = false;
				
				if(response.data != undefined && response.data.length > 0){
					vm.elements.success = true;
					vm.elements.list = response.data;
				} else {
					vm.elements.error = true;
				}
				
			}, function(error){
				console.log('search error', error);
				vm.form.loading = false;
				vm.elements.loading = false;
				vm.elements.error = true;
			});
			
		}
			
		function getUsers(){
			vm.users.loading = true;
			vm.users.error = false;
			s.getUsers().then(function(response){
				vm.users.loading = false;
				if(response.data != undefined){
					vm.users.list = response.data;
				} else {
					vm.users.error = true
				}
			}, function(){
				vm.users.loading = false;
				vm.users.error = true;
			});
		}
		
		function getUsername(key){
			var name = [];
			if(vm.users.list != undefined && vm.users.list.length > 0){
				name = vm.users.list.filter(function(u){
					if(u.key == key){
						return u;
					}
				})[0];
				
				if(name.value !== undefined && name.value !== ''){
					return name.value;
				}
			}

			return key;
		}
		
		function addElementToUpdateList(item){

			var index = vm.elements.updateList.indexOf(item);
			
			if(index > -1){
				item.selected = false;
				vm.elements.updateList.splice(index, 1);
			} else {
				item.selected = true;
				vm.elements.updateList.push(item);
			}
		}
		
		function addAllElementsToUpdate(filter){
			
			if(vm.elements.allUpdate){
				vm.elements.list.forEach(function(element){
					element.checked = false;
				});
				
				vm.elements.updateList = [];
				vm.elements.allUpdate = false;	
				console.log('all update true', vm.elements);
			} else {
				
				var filtered = $filter('filter')(vm.elements.list, filter);
				
				filtered.forEach(function(element){
					element.checked = true;
				});
				
				vm.elements.updateList = filtered;
				vm.elements.allUpdate = true;	
				
				console.log('all update false', vm.elements);
			}
		}
		
		function openModal(item){		
			
			vm.modal.element.assign.error = false;
			vm.modal.element.assign.success = false;
			vm.modal.element.assign.loading = false;
			vm.modal.element.assign.noUser = false;
			vm.modal.element.assign.user = {};
			
			vm.modal.element.unlock.loading = false;
			
			vm.modal.results.error = false;
			vm.modal.results.success = false;
			vm.modal.results.msg = "";
			
			vm.elements.updateList.forEach(function(item){
				item.trackerUrl = '/Workplace/eprocess/WcmTracker.jsp?wobNum=' + item.F_WobNum + '&setWindowId=mainWindow&queueName=' + item.queue;
				item.planillaUrl = '/Workplace/WcmJavaViewer.jsp?vsId=' +item.documentId + '&objectType=document&objectStoreName=HaciendaOS&docName=' + item.NumeroSerie;
			});
		}
		
		
		function setElement(){
			
			vm.modal.element.assign.noUser = false;
			vm.modal.element.assign.error = false;
			vm.modal.element.assign.loading = true;
			
			if(vm.modal.element.assign.user === undefined || vm.modal.element.assign.user === '' || vm.modal.element.assign.user === {}){
				vm.modal.element.assign.noUser = true;
				return;
			}
			
			if(vm.modal.element.assign.user.description.key !== undefined){

				var elements = [];
				vm.elements.updateList.forEach(function(element){
					var e = {};
					
					e.user = vm.modal.element.assign.user.description.key;
					e.roster = vm.form.roster;
					e.queue = 'Inbox';
					e.wobNum = element.F_WobNum;
					
					elements.push(e);
				});
				
				s.setElement('roster', elements).then(function success(response){
					console.log('response', response);
					
					vm.modal.element.assign.loading = false;
					
					if(response.data !== undefined && response.data.result !== undefined && response.data.result == true){
						vm.modal.element.assign.success = true;
						search();
						addAllElementsToUpdate();
					} else {
						vm.modal.element.assign.error = true;
					}
				}, function error (){
					
					vm.modal.element.assign.loading = false;
					vm.modal.element.assign.error = true;
					
				});
				
			}			
		}
		
		function unlockElement(){
			
			vm.modal.results.error = false;
			vm.modal.results.success = false;
			vm.modal.results.msg = "";
			vm.modal.element.unlock.loading = true;
			
			var form = [];
			
			vm.elements.updateList.forEach(function(element){
				var e = {};
				e.wobNum = element.F_WobNum;
				e.roster = vm.form.roster;
				form.push(e);
			});
			
			s.unlockElement('roster', form).then(function success(response){
				vm.modal.element.unlock.loading = false;
				if(response.data !== undefined && response.data.result !== undefined){
					
					//	one task of one not unblocked
					if(response.data.result === 0 && form.length === 1){
						vm.modal.results.warning = true;
						vm.modal.results.msg = 'La tarea no fue desbloqueada';
					}
						
					//	one task of one unblocked
					else if(response.data.result === form.length && form.length === 1){
						vm.modal.results.success = true;
						vm.modal.results.msg = 'La tarea fue desbloqueada';
					}
						
					//	no task of many unblocked
					else if(response.data.result === 0 && form.length > 1){
						vm.modal.results.warning = true;
						vm.modal.results.msg = 'Ninguna de las tareas fueron desbloqueadas';
					}
						
					//	some tasks of many unblocked
					else if(response.data.result > 0 && form.length > 1){
						vm.modal.results.warning = true;
						vm.modal.results.msg = 'Algunas de las tareas (' + response.data.result + '/' + form.length + ') fueron desbloqueadas';
					}
						
					//	all tasks unlocked
					else if(response.data.result === form.length){
						vm.modal.results.success = true;
						vm.modal.results.msg = 'Todas las tareas fueron desbloqueadas';
					}
								 

				} else {
					vm.modal.results.error = true;
					vm.modal.results.msg = 'Hubo un error desbloqueando las tareas, por favor inténtelo mas tárde.';
				}
			}, function error(){
				vm.modal.element.unlock.loading = false;
				vm.modal.results.error = true;
				vm.modal.results.msg = 'Hubo un error desbloqueando las tareas, por favor inténtelo mas tárde.';
			});
		}
	}
	
})();