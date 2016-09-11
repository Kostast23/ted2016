app.controller('CategoryController', function ($scope, $http, $stateParams) {
    $scope.category = { name: $stateParams.categoryName };
    $http.get('/api/categories/' + $stateParams.categoryId).then(function(response) {
        $scope.category = response.data;
    });
});