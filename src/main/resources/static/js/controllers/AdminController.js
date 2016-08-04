app.controller('AdminController', function($scope, $http, $state) {
    $http.get('/api/admin/awaitingValidation').then(function (response) {
        $scope.awaitingUsers = response.data;
    });
});