app.controller('AdminController', function($scope, $http, $state) {
    $http.get('/api/admin/users/not_validated').then(function (response) {
        $scope.awaitingUsers = response.data;
    });
    $http.get('/api/admin/users/225').then(function (response) {
        alert(response);
    });
});