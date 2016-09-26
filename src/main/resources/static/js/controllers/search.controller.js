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
});