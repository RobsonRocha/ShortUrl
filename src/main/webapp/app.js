(function () {
    'use strict';

    angular
        .module('app', ['ngRoute', 'ngCookies', 'angularUtils.directives.dirPagination', 'ngAnimate', 
                        'ui.bootstrap'])
        .config(config)
        .run(run);

    config.$inject = ['$routeProvider', '$locationProvider'];
    function config($routeProvider, $locationProvider) {
        $routeProvider
            .when('/', {
            	controller: 'ShortClientController',
                templateUrl: 'shortclient/encode.view.html',
                controllerAs: 'vm'
            })
            .when('/decode', {
                controller: 'ShortClientController',
                templateUrl: 'shortclient/decode.view.html',
                controllerAs: 'vm'
            })
            .when('/encode', {
                controller: 'ShortClientController',
                templateUrl: 'shortclient/encode.view.html',
                controllerAs: 'vm'
            })
            .when('/top10', {
                controller: 'ShortClientController',
                templateUrl: 'shortclient/top10.view.html',
                controllerAs: 'vm'
            })            
            .otherwise({ redirectTo: '/encode' });
    }        
    
    run.$inject = ['$rootScope', '$location', '$cookieStore', '$http'];
    function run($rootScope, $location, $cookieStore, $http) {
//    	$rootScope.$on('$locationChangeStart', function (event, next, current) {
//            // redireciona para tela de login se não estiver logado
//            var restrictedPage = $.inArray($location.path(), ['/register']) === -1;
//            if (restrictedPage) {
//                $location.path('/register');
//            }
//        });
    }
    
})();