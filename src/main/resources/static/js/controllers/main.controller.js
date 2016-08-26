app.controller('MainController', function($scope, $http, $state, AuthService) {
    $scope.admin = AuthService.user.admin;
    $scope.doLogout = AuthService.logout;
});