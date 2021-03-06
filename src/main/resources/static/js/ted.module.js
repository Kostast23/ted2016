var app = angular.module('tedApp', ['ui.router', 'ui.bootstrap', 'ngMessages', 'leaflet-directive', 'angularCSS', 'angularFileUpload']);

app.config(function($httpProvider, $stateProvider, $urlRouterProvider) {
    $httpProvider.interceptors.push('httpAuthInterceptor');
    $urlRouterProvider.when('', '/').when('/login', '/').when('/register', '/').otherwise("/page_not_found");

    /* prepare the ui router routes */
    $stateProvider
        .state('main', {
            abstract: true,
            templateUrl: 'partials/main.html',
            controller: 'MainController'
        })
        .state('main.index', {
            url: '/',
            templateUrl: 'partials/index.html',
            controller: 'IndexController',
            css: 'css/index.css',
            resolve: { redirect: redirectIfLoggedIn }
        })
        .state('main.profile', {
            abstract: true,
            templateUrl: 'partials/profile/profile.html',
            controller: 'ProfileController',
            css: 'css/profile.css',
            resolve: { authenticate: authenticateUser }
        })
        .state('main.profile.active', {
            url: '/profile/auctions/active',
            templateUrl: 'partials/profile/auctions_active.html',
            controller: 'ProfileAuctionsActiveController',
            css: 'css/profile.css'
        })
        .state('main.profile.closed', {
            url: '/profile/auctions/closed',
            templateUrl: 'partials/profile/auctions_closed.html',
            controller: 'ProfileAuctionsClosedController',
            css: 'css/profile.css'
        })
        .state('main.profile.participating', {
            url: '/profile/auctions/participating',
            templateUrl: 'partials/profile/auctions_participating.html',
            controller: 'ProfileAuctionsParticipatingController',
            css: 'css/profile.css'
        })
        .state('main.profile.bought', {
            url: '/profile/auctions/bought',
            templateUrl: 'partials/profile/auctions_bought.html',
            controller: 'ProfileAuctionsBoughtController',
            css: 'css/profile.css'
        })
        .state('main.profile.new_message', {
            url: '/profile/messages/new',
            templateUrl: 'partials/profile/new_message.html',
            params: { to: null, subject: null },
            controller: 'NewMessageController',
            css: 'css/profile.css'
        })
        .state('main.profile.messages_received', {
            url: '/profile/messages/received',
            templateUrl: 'partials/profile/messages.html',
            controller: 'MessagesController',
            css: 'css/profile.css'
        })
        .state('main.profile.messages_sent', {
            url: '/profile/messages/sent',
            templateUrl: 'partials/profile/messages.html',
            controller: 'MessagesController',
            css: 'css/profile.css'
        })
        .state('main.profile.message', {
            url: '/profile/messages/:messageId',
            templateUrl: 'partials/profile/message.html',
            controller: 'MessageController',
            css: 'css/profile.css'
        })
        .state('main.store', {
            url: '/store',
            templateUrl: 'partials/store.html',
            controller: 'StoreController',
            css: 'css/store.css'
        })
        .state('main.category', {
            url: '/category/:categoryId/:categoryName',
            templateUrl: 'partials/category.html',
            controller: 'CategoryController',
            css: 'css/category.css'
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
            controller: 'UserProfileController',
            css: 'css/user_profile.css'
        })
        .state('main.search', {
            url: '/search',
            templateUrl: 'partials/search.html',
            params: { name: null, categoryId: null },
            controller: 'SearchController',
            css: 'css/category.css'
        })
        .state('main.new_item', {
            url: '/new_item',
            templateUrl: 'partials/edit_item.html',
            controller: 'EditItemController',
            params: { category: null },
            resolve: { authenticate: authenticateUser }
        })
        .state('main.edit_item', {
            url: '/item/edit/:itemId/:itemName',
            templateUrl: 'partials/edit_item.html',
            controller: 'EditItemController',
            resolve: { authenticate: authenticateUser }
        })
        .state('main.admin', {
            url: '/admin',
            templateUrl: 'partials/admin.html',
            controller: 'AdminController',
            resolve: { authenticate: authenticateAdmin }
        })
        .state('main.admin_user', {
            url: '/admin/users/:userId',
            templateUrl: 'partials/admin_user.html',
            controller: 'AdminUserController',
            css: 'css/admin_user.css',
            resolve: { authenticate: authenticateAdmin }
        })
        .state('main.suggestions', {
            url: '/suggestions',
            templateUrl: 'partials/suggestions.html',
            controller: 'SuggestionsController',
            css: 'css/category.css'
        })
        .state('page_not_found', {
            url: '/page_not_found',
            templateUrl: 'partials/page_not_found.html',
            css: 'css/page_not_found.css'
        })
        .state('main.signup_success', {
            url: '/signup_success',
            templateUrl: 'partials/signup_success.html'
        });

    function authenticateAdmin($q, $state, $timeout, AuthService) {
        if (AuthService.isAdmin()) {
            return $q.when();
        } else {
            $timeout(function() {
                // This code runs after the authentication promise has been rejected.
                $state.go('page_not_found');
            });

            // Reject the authentication promise to prevent the state from loading
            return $q.reject();
        }
    }

    function authenticateUser($q, $state, $timeout, AuthService) {
        if (AuthService.isLoggedIn()) {
            return $q.when();
        } else {
            $timeout(function() {
                $state.go('main.index');
            });
            return $q.reject();
        }
    }

    function redirectIfLoggedIn($q, $state, $timeout, AuthService) {
        if (AuthService.isLoggedIn()) {
            $timeout(function() {
                $state.go('main.store')
            });
            return $q.reject();
        } else {
            return $q.when();

        }
    }
});

app.run(['$window', 'AuthService', function($window, AuthService) {
    // keep user logged in after page refresh
    var jwt = $window.localStorage.getItem('jwt');
    if (jwt) {
        AuthService.setUser(jwt);
    }
}]);
