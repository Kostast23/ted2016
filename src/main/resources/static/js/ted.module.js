var app = angular.module('tedApp', ['ui.router']);

app.factory('httpAuthInterceptor', function ($window) {
    return {
        request: function (config) {
            var jwt = $window.localStorage.jwt;
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
            controller: function($scope, $window) {
                $scope.admin = $window.localStorage.admin;
                $scope.doLogout = function() {
                    delete $window.localStorage.jwt;
                }
            }
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