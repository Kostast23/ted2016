app.controller('ProfileAuctionsBoughtController', function ($scope, $http, $interval, AuthService) {
    $scope.$parent.view_tab = "bought";
    $scope.resourcesLoaded = false;

    $scope.maxSize = 5;  // pagination size
    $scope.itemsPerPage = 5;
    $scope.currentPage = 1;
    $scope.totalItems =  0;
    $scope.items = [];
    $scope.filteredItems = [];

    var getItems = function() {
        $http.get('/api/items/bought/' + AuthService.user.user, {
            params: {
                page: $scope.currentPage - 1,
                size: $scope.itemsPerPage
            }
        }).then(function success(response) {
            $scope.items = response.data.content.map(function (item) {
                item.currentbid = +item.currentbid / 100;
                item.endOffset = moment(item.endDate).fromNow();
                return item;
            });
            $scope.items.sort(function(item1, item2) {
                return item2.endDate - item1.endDate;
            });
            $scope.totalItems = response.data.totalElements;
            $scope.filteredItems = $scope.items.slice(0, $scope.itemsPerPage);
            $scope.resourcesLoaded = true;
        });
    };

    getItems();
    var updateInterval = $interval(getItems, 5000);

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

    $scope.happy = function(itemId) {
        $http.get('/api/ratings/' + itemId + '/buyer/happy').then(function() {
            getItems();
        });
    };

    $scope.unhappy = function(itemId) {
        $http.get('/api/ratings/' + itemId + '/buyer/unhappy').then(function() {
            getItems();
        });
    };
});