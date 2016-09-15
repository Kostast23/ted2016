app.controller('CategoryController', function ($scope, $http, $stateParams, AuthService) {
    $scope.category = { name: $stateParams.categoryName };
    $scope.loggedIn = !!AuthService.user.user;
    $http.get('/api/categories/' + $stateParams.categoryId).then(function(response) {
        $scope.category = response.data;
        $scope.breadcrumb = [];
        var parent = $scope.category.parent;
        while (parent != null) {
            $scope.breadcrumb.unshift(parent);
            parent = parent.parent;
        }
    });
});