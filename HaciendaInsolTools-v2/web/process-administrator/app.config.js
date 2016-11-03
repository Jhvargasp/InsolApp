(function(){
	'use-strict';
	
	//////////////////////////////////////////////////////////////////////////////
	//
	//	Helper:
	//	
	//	Version the 'get' $http calls to bust angular cache
	//	
	//
	//////////////////////////////////////////////////////////////////////////////
		
	angular.module('app').config(HttpCacheBust);
	
	HttpCacheBust.$inject = ['$provide'];
	
	function HttpCacheBust($provide) {

		return $provide.decorator("$http", delegate);

		delegate.$inject = ['$delegate'];

		function delegate($delegate) {			
			var get = $delegate.get;
			$delegate.get = function(url, config) {

				//console.log('get config', url, config);
				
				if (url.indexOf('modules/') < -1) {
					url += (url.indexOf("?") === -1 ? "?" : "&");
					url += "v=" + Math.random();
				}
				
				return get(url, config);
			};
			
			return $delegate;
		}
		
	}
	
	
})();