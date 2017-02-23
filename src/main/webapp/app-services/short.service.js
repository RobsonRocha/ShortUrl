(function () {
    'use strict';

    angular
        .module('app')
        .factory('ShortService', ShortService);

    ShortService.$inject = ['$http'];
    function ShortService($http) {
       var service = {};

       service.Create = Create;
       service.Decode = Decode;
       service.getTop10 = getTop10;
        
       return service;
        
       function Create(shortUrl, callback) {
        	var path = shortUrl.originalUrl;
        	if(shortUrl.alias.trim() != '')
        		path += "&alias="+shortUrl.alias.trim();
        	$http({
            method : 'PUT',
            url : 'http://localhost:8080/encode?originalurl='+path,            
            headers : {
                'Content-Type' : 'application/json',
                'charset' : 'UTF-8'
            }
        }).success(function (response) {
        	response.success = true;        	
        	callback(response);
          }).error(function (response){
        	  response.success = false;
        	  callback(response);
          });
        }
       
       function Decode(shortUrl, callback) {
       	var path = shortUrl.originalUrl;       	
       	$http({
           method : 'PUT',
           url : 'http://localhost:8080/decode?shorturl='+path,            
           headers : {
               'Content-Type' : 'application/json',
               'charset' : 'UTF-8'
           }
       }).success(function (response) {
       	response.success = true;        	
       	callback(response);
         }).error(function (response){
       	  response.success = false;
       	  callback(response);
         });
       }
       
       function getTop10(callback) {
       	$http({
           method : 'GET',
           url : 'http://localhost:8080/top10',            
           headers : {
               'Content-Type' : 'application/json',
               'charset' : 'UTF-8'
           }
       }).success(function (response) {
       	response.success = true;        	
       	callback(response);
         }).error(function (response){
       	  response.success = false;
       	  callback(response);
         });
       }
       
    }    

})();
