(function() {
	'use-strict';

	angular.module('app').config(RoutesConfiguration);

	RoutesConfiguration.$inject = ['$stateProvider', '$urlRouterProvider'];

	function RoutesConfiguration($stateProvider, $urlRouterProvider) {

		$urlRouterProvider.otherwise('/queue');

		$stateProvider
				
		///////////////////////////////////////////////////////////////////////
		//
		//	Application Root (abstract parent)
		//
		///////////////////////////////////////////////////////////////////////
		
		.state('app', {
			abstract: true,
			templateUrl: 'modules/_shared/layout/tabLayout.view.html'
		})
		
		
		///////////////////////////////////////////////////////////////////////
		//
		//	Inbox
		//
		///////////////////////////////////////////////////////////////////////
		
		.state('app.inbox', {
			url: '/inbox',
			templateUrl: 'modules/inbox/index.view.html',
			controller: "InboxController",
			controllerAs: 'vm',
			resolve: { 
				
			}
		})	
		
		///////////////////////////////////////////////////////////////////////
		//
		//	Queue
		//
		///////////////////////////////////////////////////////////////////////
		
		.state('app.queue', {
			url: '/queue',
			templateUrl: 'modules/queue/index.view.html',
			controller: "QueueController",
			controllerAs: 'vm',
			resolve: { 
				
			}
		})		
		
		///////////////////////////////////////////////////////////////////////
		//
		//	Roster
		//
		///////////////////////////////////////////////////////////////////////
		
		.state('app.roster', {
			url: '/roster',
			templateUrl: 'modules/roster/index.view.html',
			controller: "RosterController",
			controllerAs: 'vm',
			resolve: { 
				
			}
		});
		
		
		///////////////////////////////////////////////////////////////////////
		//
		//	Errors
		//
		///////////////////////////////////////////////////////////////////////
		
/*		.state('error', {
			url: '/error',
			template: 'modules/shared/errors/general.view.html'
		})
		
		.state('error.no-info', {
			url: '/no-info',
			templateUrl: 'modules/shared/errors/no-info.view.html'
		})
		
		.state('error', {
			url: '/roster',
			templateUrl: 'modules/shared/errors/general.view.html'
		});
*/		

		
	}

})();
