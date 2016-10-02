app.controller('MainController', function($scope, $state, $http, $interval, AuthService) {
    $scope.username = AuthService.user.user;
    $scope.admin = AuthService.user.admin;

    $scope.doLogout = function() {
        AuthService.logout();
        $scope.username = undefined;
        $scope.admin = undefined;
        $scope.newMessageCount = undefined;
        $scope.hasSuggestions = undefined;
    };

    $scope.atIndex = function() {
        return $state.current.name === 'main.index';
    };

    $scope.getHomeUrl = function() {
        return $state.current.name === 'main.index' ?
            $state.href('main.index') :  $state.href('main.store');
    };

    var getNewMessagesCount = function() {
        if (AuthService.isLoggedIn()) {
            $http.get('api/messages/new').then(function (response) {
                $scope.newMessageCount = parseInt(response.data, 10);
            });
        }
    };

    var checkSuggestions = function() {
        if (AuthService.isLoggedIn()) {
            $http.get('api/items/suggestions').then(function (response) {
                $scope.hasSuggestions = !!response.data.length;
            });
        }
    };

    var checkNewMessageCountInterval = $interval(getNewMessagesCount, 2000);
    $scope.$on('$destroy', function() {
        $interval.cancel(checkNewMessageCountInterval);
    });

    var checkSuggestionsInterval = $interval(checkSuggestions, 60000);
    $scope.$on('$destroy', function() {
        $interval.cancel(checkSuggestionsInterval);
    });

    getNewMessagesCount();
    checkSuggestions();
});
