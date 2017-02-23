(function() {
	'use strict';

	angular.module('app').controller('ShortClientController', ShortClientController);

	ShortClientController.$inject = ['ShortService', '$window'];
	function ShortClientController(ShortService, $window) {
		var vm = this;

		vm.callShorterUrl = callShorterUrl;
		vm.callDecoderUrl = callDecoderUrl;
		vm.redirectToURL = redirectToURL;
		vm.top10 = [];
		
		initController();

		function initController() {			
			vm.insertUrl = {
					shortUrl : {
						alias : '',
						originalUrl : ''						
					}
				};	
			vm.show = false;
			vm.showError = false;
			callTop10();
		}

		function callShorterUrl(){
			vm.dataLoading = true;
			ShortService.Create(vm.insertUrl.shortUrl, function(response) {
				if (response.success) {
					vm.show = true;
					vm.showError = false;
					vm.shortUrl = response;					
				} else {
					vm.show = false;
					vm.showError = true;
					vm.shortUrl = response;					
				}
			});
			vm.dataLoading = false;
		}	
		
		function callDecoderUrl(){
			vm.dataLoading = true;
			ShortService.Decode(vm.insertUrl.shortUrl, function(response) {
				if (response.success) {
					vm.show = true;
					vm.showError = false;
					if(response.url)
					vm.shortUrl = response;					
				} else {
					vm.show = false;
					vm.showError = true;
					vm.shortUrl = response;					
				}
			});
			vm.dataLoading = false;
		}	
		
		function redirectToURL(){
			if(vm.shortUrl.url.substring(0,7) != "http://")
				$window.location.href="http://"+vm.shortUrl.url;
			else 
				$window.location.href=vm.shortUrl.url;
		}
		
		function callTop10(){
			ShortService.getTop10(function(response) {
				if (response.success) {
					vm.top10 = response;					
				}
			});			
		}	
	}

})();
