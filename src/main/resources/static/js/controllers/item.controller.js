app.controller('ItemController', function ($scope, $http, $stateParams) {
    $scope.item = { name: $stateParams.itemName };
    $http.get('/api/items/' + $stateParams.itemId).then(function(response) {
        $scope.item = response.data;
    });
});