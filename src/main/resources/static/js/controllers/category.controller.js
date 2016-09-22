app.controller('CategoryController', function ($scope, $http, $stateParams, AuthService) {
    $scope.resourcesLoaded = false;
    $scope.maxSize = 5;  // pagination size
    $scope.itemsPerPage = 10;
    $scope.currentPage = 1;
    $scope.totalItems =  0;

    $scope.loggedIn = AuthService.isLoggedIn();
    $scope.category = { name: $stateParams.categoryName };
    $scope.items = [];
    $scope.submenuSize = 5;

    $http.get('/api/categories/' + $stateParams.categoryId).then(function(response) {
        $scope.category = response.data;
        $scope.breadcrumb = [];
        var parent = $scope.category.parent;
        while (parent != null) {
            $scope.breadcrumb.unshift(parent);
            parent = parent.parent;
        }
    });

    $scope.getItems = function() {
        $http.get('/api/categories/' + $stateParams.categoryId + '/items', {
            params: {
                page: $scope.currentPage - 1,
                size: $scope.itemsPerPage
            }
        }).then(function success(response) {
            $scope.items = response.data.content;
            $scope.totalItems = response.data.totalElements;

        });
    };

    $scope.needPagination = function() {
        return $scope.totalItems > $scope.itemsPerPage;
    };

    $scope.getItems();
});