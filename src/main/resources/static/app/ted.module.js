var app = angular.module('tedApp', ['ui.router', 'ui.bootstrap', 'ngFileUpload', 'ngMessages', 'leaflet-directive']);

app.config(function($httpProvider, $stateProvider, $urlRouterProvider) {
    $httpProvider.interceptors.push('httpAuthInterceptor');
    $urlRouterProvider.otherwise("/index");

    $stateProvider
        .state('index', {
            url: '/index',
            templateUrl: 'app/partials/index.html',
            controller: 'UserController'
        })
        .state('main', {
            abstract: true,
            templateUrl: 'app/partials/main.html',
            controller: 'MainController'
        })
        .state('main.store', {
            url: '/store',
            templateUrl: 'app/partials/store.html',
            controller: 'StoreController'
        })
        .state('main.category', {
            url: '/category/:categoryId/:categoryName',
            templateUrl: 'app/partials/category.html',
            controller: 'CategoryController'
        })
        .state('main.item', {
            url: '/item/:itemId/:itemName',
            templateUrl: 'app/partials/item.html',
            controller: 'ItemController'
        })
        .state('main.user', {
            url: '/user/:username',
            templateUrl: 'app/partials/userprofile.html',
            controller: function($scope, $http, $stateParams) {
                $scope.user = $http.get('/api/user/' + $stateParams.username);
            }
        })
        .state('main.admin', {
            url: '/admin',
            templateUrl: 'app/partials/admin.html',
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
