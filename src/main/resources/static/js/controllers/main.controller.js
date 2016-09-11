app.controller('MainController', function($scope, $http, $state, AuthService) {
    $scope.username = AuthService.user.user;
    $scope.admin = AuthService.user.admin;
    $scope.doLogout = AuthService.logout;
});