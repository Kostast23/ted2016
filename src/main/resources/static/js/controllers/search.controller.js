app.controller('SearchController', function($scope, $http, $stateParams, $location) {
    $scope.maxSize = 5;
    $scope.itemsPerPage = 10;

    $scope.needPagination = function() {
        return $scope.totalItems > $scope.itemsPerPage;
    };

    var doSearch = function(searchParams) {
        /*
         * if this is the same search with a different page, use the last use params,
         * otherwise get the first page
         */
        if (searchParams) {
            $scope.lastParams = angular.copy(searchParams);
        } else {
            searchParams = $scope.lastParams;
            searchParams.page = $scope.searchParams.page;
        }
        /* save the parameters in the url */
        $location.search(searchParams);
        var httpParams = angular.merge({
            size: $scope.itemsPerPage
        }, searchParams);
        if (httpParams.min) {
            httpParams.min = Math.floor(httpParams.min * 100);
        }
        if (httpParams.max) {
            httpParams.max = Math.floor(httpParams.max * 100);
        }
        $http.post('api/search/', httpParams).then(function(response) {
            $scope.totalItems = response.data.totalElements;
            $scope.items = response.data.content.map(function (item) {
                if (item.currentbid) {
                    item.currentbid = +item.currentbid / 100;
                }
                if (item.buyprice) {
                    item.buyprice = +item.buyprice / 100;
                }

                if (!item.finished) {
                    item.finished = new Date() > new Date(item.endDate);
                }
                item.categories = [];
                var category = item.category;
                while (category) {
                    item.categories.unshift(category);
                    category = category.parent;
                }
                item.end = (item.finished ? "Closed" : "Ends:");
                item.endOffset = moment(item.endDate).fromNow();
                return item;
            });
        });
    };

    /* get parameters saved in the url */
    $scope.searchParams = $location.search();
    if ($scope.searchParams.min) {
        $scope.searchParams.min = +$scope.searchParams.min;
    }
    if ($scope.searchParams.max) {
        $scope.searchParams.max = +$scope.searchParams.max;
    }

    $scope.doSearch = doSearch;

    if ($stateParams.name) {
        $scope.searchParams.name = $stateParams.name;
        $scope.searchParams.page = 1;
        if ($stateParams.categoryId) {
            $scope.searchParams.category = String($stateParams.categoryId);
        }
    }

    /* if there already are parameters, start a search */
    if (Object.keys($scope.searchParams).length) {
        doSearch($scope.searchParams);
    }

    if (!$scope.searchParams.category) {
        $scope.searchParams.category = '-1';
    }

    /* make the recursive list of categories with subcategories under their parents */
    $http.get('api/categories/all').then(function (response) {
        $scope.listCategories = response.data;
    });
});
