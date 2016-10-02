app.controller('ProfileAuctionsParticipatingController', function ($scope, $http, $interval, AuthService) {
    $scope.$parent.view_tab = "participating";
    $scope.resourcesLoaded = false;

    $scope.maxSize = 5;  // pagination size
    $scope.itemsPerPage = 5;
    $scope.currentPage = 1;
    $scope.totalItems =  0;
    $scope.items = [];
    $scope.filteredItems = [];

    var getItems = function() {
        $http.get('/api/items/participating/' + AuthService.user.user, {
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

    var confirmBid = function (name, price) {
        return confirm("You are buying " + name + " for " + price + "$. Are you sure?");
    };

    $scope.buyNow = function (item) {
        if (confirmBid(item.name, item.buyprice)) {
            var amount = Math.floor(item.buyprice * 100);
            $http.post('/api/bids/' + item.id, amount).then(function () {
                item.bidError = null;
                getItems();
            }, function(err) {
                item.bidError = err.data.message;
            });
        }
    };
});