var app = angular.module('tedApp', ['ui.router', 'ui.bootstrap', 'ngFileUpload']);

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
        .state('main.category', {
            url: '/category/:categoryId/:categoryName',
            templateUrl: 'js/partials/category.html',
            controller: 'CategoryController'
        })
        .state('main.item', {
            url: '/item/:itemId/:itemName',
            templateUrl: 'js/partials/item.html',
            controller: 'ItemController'
        })
        .state('main.admin', {
            url: '/admin',
            templateUrl: 'js/partials/admin.html',
            controller: 'AdminController'
        });
});

app.run(['$window', 'AuthService', function($window, AuthService) {
    // keep user logged in after page refresh
    var jwt = $window.localStorage.getItem('jwt');
    if (jwt) {
        AuthService.setUser(jwt);
    }
}]);
