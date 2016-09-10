app.controller('StoreController', function ($scope, $http) {
    $http.get('/api/categories/').then(function (response) {
        var categories = response.data;

        categories.sort(function (a, b) {
            return b.count - a.count;
        });
        $scope.categories = categories;
    });
});