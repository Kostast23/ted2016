var app = angular.module('tedApp', ['ui.router']);

app.config(function($stateProvider, $urlRouterProvider) {
    //
    // For any unmatched url, redirect to /state1
    $urlRouterProvider.otherwise("/index");
    //
    // Now set up the states
    $stateProvider
        .state('index', {
            url: '/index',
            templateUrl: 'js/partials/index.html',
            controller: 'UserController'
        })
        .state('main', {
            abstract: true,
            templateUrl: 'js/partials/main.html',
            controller: function($window) {
                delete $window.sessionStorage.jwt;
            }
        })
        .state('main.store', {
            url: '/store',
            templateUrl: 'js/partials/store.html',
            controller: 'StoreController'
        });
});