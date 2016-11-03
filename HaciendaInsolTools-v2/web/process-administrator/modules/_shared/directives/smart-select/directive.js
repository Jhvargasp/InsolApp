(function(){
	'use-strict';
	
	angular.module('app').directive('smartSelect', remoteSelect);
	
	remoteSelect.$inject = [];
	
	function remoteSelect(){
		
		var directive = this;
		
		directive.restrict = "A";
	    directive.templateUrl = 'modules/_shared/directives/smart-select/view.html';
		directive.replace = true;
		directive.controller = controller;
	    directive.scope = {
	    	name : '@smartSelect',
	    	list : '=smList',
	    	loading : '=smLoading',
	    	label : '@smLabel',
	    	model : '=smModel',
	    	change : '@smChange',
	    	error : '=smError',
	    	errorMsg : '@smErrorMsg'
	    }
		
		return directive;
	    
	    function controller($scope){
	    	console.log('controller scope', $scope);
	    	
	    	if($scope.label === undefined){
	    		$scope.label = 'Smart Select';
	    	}
	    	
	    	if($scope.errorMsg === undefined){
	    		$scope.errorMsg = "Lo sentimos, no se pudo obtener información en estos momentos. Por favor intentarlo de nuevo más tarde.";
	    	}
	    	
	    	if($scope.name === undefined){
	    		$scope.name = 'smart-select';
	    	}
	    }
	}
	
	
})();