var app = angular.module('tedApp', ['ui.router', 'ui.bootstrap', 'ngFileUpload', 'ngMessages', 'leaflet-directive', 'angularCSS']);

app.config(function($httpProvider, $stateProvider, $urlRouterProvider) {
    $httpProvider.interceptors.push('httpAuthInterceptor');
    $urlRouterProvider.when('/login', '/').when('/register', '/').otherwise("/page_not_found");

    $stateProvider
        .state('index', {
            url: '/',
            templateUrl: 'partials/index.html',
            controller: 'UserController',
            css: 'css/index.css'
        })
        .state('main', {
            abstract: true,
            templateUrl: 'partials/main.html',
            controller: 'MainController'
        })
        .state('main.store', {
            url: '/store',
            templateUrl: 'partials/store.html',
            controller: 'StoreController'
        })
        .state('main.category', {
            url: '/category/:categoryId/:categoryName',
            templateUrl: 'catecory/category.html',
            controller: 'CategoryController'
        })
        .state('main.item', {
            url: '/item/:itemId/:itemName',
            templateUrl: 'partials/item.html',
            controller: 'ItemController',
            css: 'css/item.css'
        })
        .state('main.user', {
            url: '/user/:username',
            templateUrl: 'partials/user_profile.html',
            controller: function($scope, $http, $stateParams) {
                $scope.user = $http.get('/api/user/' + $stateParams.username);
            }
        })
        .state('main.admin', {
            url: '/admin',
            templateUrl: 'partials/admin.html',
            controller: 'AdminController'
        })
        .state('page_not_found', {
            url: '/page_not_found',
            templateUrl: 'partials/page_not_found.html',
            css: 'css/page_not_found.css'
        });
});

app.run(['$window', 'AuthService', function($window, AuthService) {
    // keep user logged in after page refresh
    var jwt = $window.localStorage.getItem('jwt');
    if (jwt) {
        AuthService.setUser(jwt);
    }
}]);
