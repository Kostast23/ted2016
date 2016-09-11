app.controller('ItemController', function ($scope, $http, $stateParams, $interval, $timeout, leafletData, AuthService) {
    $scope.item = { name: $stateParams.itemName };
    $scope.loggedIn = !!AuthService.user.user;
    $scope.submitBid = function(amount) {
        $http.post('/api/bids/' + $stateParams.itemId, amount);
    };

    $http.get('/api/items/' + $stateParams.itemId).then(function(response) {
        var item = response.data;
        $scope.item = item;
        $scope.breadcrumb = [];
        var parent = item.category;
        while (parent != null) {
            $scope.breadcrumb.unshift(parent);
            parent = parent.parent;
        }

        var endDate = new Date(item.endDate);
        var endMoment = moment(endDate);

        var updateOffset = function() {
            $scope.endOffset = (new Date() > endDate ? "Auction ended " : "Auction ends ") + endMoment.fromNow();
        };
        updateOffset();
        $interval(updateOffset, 1000);

        if (item.lat && item.lon) {
            angular.extend($scope, {
                markers: {
                    item: {
                        lat: item.lat,
                        lng: item.lon
                    }
                }
            });
            leafletData.getMap().then(function(map) {
                map.setView([item.lat, item.lon], 5);
            });
        }
    });
    $http.get('/api/bids/' + $stateParams.itemId).then(function(response) {
        $scope.bids = response.data.map(function(bid) {
            bid.date = new Date(bid.time);
            bid.timeOffset = moment(bid.date).fromNow();
            return bid;
        });
        $timeout(function() { $('[data-toggle="tooltip"]').tooltip(); }, 0);
    });
});