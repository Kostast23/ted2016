app.controller('ProfileController', function ($scope, $stateParams, AuthService) {
    $scope.changeTab = function(tab) {
        $scope.view_tab = tab;
    }
});