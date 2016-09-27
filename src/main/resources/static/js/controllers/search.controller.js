app.controller('SearchController', function($scope, $http, $stateParams, $location) {
    $scope.maxSize = 5;
    $scope.itemsPerPage = 10;

    $scope.needPagination = function() {
        return $scope.totalItems > $scope.itemsPerPage;
    };

    var doSearch = function(searchParams) {
        if (searchParams) {
            $scope.lastParams = angular.copy(searchParams);
        } else {
            searchParams = $scope.lastParams;
            searchParams.page = $scope.searchParams.page;
        }
        $location.search(searchParams);
        var httpParams = angular.merge({
            size: $scope.itemsPerPage
        }, searchParams);
        $http.post('/api/search/', httpParams).then(function(response) {
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

    $scope.searchParams = $location.search();
    $scope.doSearch = doSearch;

    if ($stateParams.name) {
        $scope.searchParams.name = $stateParams.name;
        $scope.searchParams.page = 1;
    }

    if (Object.keys($scope.searchParams).length) {
        doSearch($scope.searchParams);
    }

    $http.get('/api/categories/all').then(function (response) {
        var makeRecursiveCategories = function (depth, cat) {
            var subcats = cat.subcategories ? [].concat.apply([],
                cat.subcategories.map(
                    makeRecursiveCategories.bind(this, depth + 1)
                )
            ) : [];
            return [{
                name: '-'.repeat(depth) + ' ' + cat.name,
                id: cat.id
            }].concat(subcats);
        };
        var listCategories = response.data.map(makeRecursiveCategories.bind(this, 1));
        $scope.listCategories = [].concat.apply([], listCategories);
    });
});