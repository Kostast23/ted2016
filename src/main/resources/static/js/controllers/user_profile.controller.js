app.controller('UserProfileController', function ($scope, $stateParams, $http, $interval) {
    $scope.user = {};
    $scope.neutral = 0;
    $scope.happy = 0;
    $scope.unhappy = 0;

    $scope.maxSize = 5;  // pagination size
    $scope.itemsPerPage = 5;
    $scope.currentPage = 1;
    $scope.totalItems =  0;
    $scope.items = [];
    $scope.filteredItems = [];

    $http.get('/api/user/' + $stateParams.username).then(function (response) {
        $scope.user = response.data;
    });

    var getClosedAuctions = function() {
        $http.get('/api/items/finished/' + $stateParams.username, {
            params: {
                page: $scope.currentPage - 1,
                size: $scope.itemsPerPage
            }
        }).then(function success(response) {
            $scope.closed = response.data.content;
            angular.forEach($scope.closed, function(item) {
                if (item.buyerHappy == null) {
                    $scope.neutral += 1;
                } else if (item.buyerHappy == false) {
                    $scope.unhappy += 1;
                } else if (item.buyerHappy == true) {
                    $scope.happy += 1;
                }
            });
        });
    };

    var getActiveAuctions = function() {
        $http.get('/api/items/active/' + $stateParams.username, {
            params: {
                page: $scope.currentPage - 1,
                size: $scope.itemsPerPage
            }
        }).then(function success(response) {
            $scope.items = response.data.content.map(function (item) {
                if (item.buyprice) {
                    item.buyprice = +item.buyprice / 100;
                }
                item.currentbid = +item.currentbid / 100;
                item.firstbid = +item.firstbid / 100;
                item.endOffset = moment(item.endDate).fromNow();
                return item;
            });
            $scope.items.sort(function(item1, item2) {
                return item1.endDate - item2.endDate;
            });
            $scope.totalItems = response.data.totalElements;
            $scope.filteredItems = $scope.items.slice(0, $scope.itemsPerPage);
            $scope.resourcesLoaded = true;
        });
    };

    getActiveAuctions();
    getClosedAuctions();

    var updateInterval = $interval(getActiveAuctions, 60000);

    $scope.needPagination = function() {
        return $scope.totalItems > $scope.itemsPerPage;
    };

    $scope.$watch("currentPage + itemsPerPage", function() {
        var begin = (($scope.currentPage - 1) * $scope.itemsPerPage)
            , end = begin + $scope.itemsPerPage;

        $scope.filteredItems = $scope.items.slice(begin, end);
    });

    $scope.$on('$stateChangeStart', function () {
        if (updateInterval) {
            $interval.cancel(updateInterval);
            updateInterval = null;
        }
    });
});