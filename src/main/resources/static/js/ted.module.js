var app = angular.module('tedApp', ['ui.router']);

app.factory('httpAuthInterceptor', function ($injector) {
    return {
        request: function (config) {
        	var AuthService = $injector.get('AuthService');
            var jwt = AuthService.user.jwt;
            if (jwt) {
                config.headers['Authorization'] = 'Bearer ' + jwt;
            }
            return config;
        }
    };
});

app.config(function($httpProvider, $stateProvider, $urlRouterProvider) {
    $httpProvider.interceptors.push('httpAuthInterceptor');
    $urlRouterProvider.otherwise("/index");

    $stateProvider
        .state('index', {
            url: '/index',
            templateUrl: 'js/partials/index.html',
            controller: 'UserController'
        })
        .state('main', {
            abstract: true,
            templateUrl: 'js/partials/main.html',
            controller: 'MainController'
        })
        .state('main.store', {
            url: '/store',
            templateUrl: 'js/partials/store.html',
            controller: 'StoreController'
        })
        .state('main.admin', {
            url: '/admin',
            templateUrl: 'js/partials/admin.html',
            controller: 'AdminController'
        });
});
