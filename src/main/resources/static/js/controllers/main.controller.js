app.controller('MainController', function($scope, $state, AuthService) {
    $scope.username = AuthService.user.user;
    $scope.admin = AuthService.user.admin;

    $scope.doLogout = function() {
        AuthService.logout();
        $scope.username = undefined;
        $scope.admin = undefined;
    };

    $scope.atIndex = function() {
        return $state.current.name === 'main.index';
    };

    $scope.getHomeUrl = function() {
        return $state.current.name === 'main.index' ?
            $state.href('main.index') :  $state.href('main.store');
    }
});