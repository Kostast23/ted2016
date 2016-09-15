app.controller('EditItemController', function ($scope, $http, $state, $stateParams, $window) {
    var marker = null;
    var curDate = new Date();
    curDate.setSeconds(0);
    curDate.setMilliseconds(0);
    $scope.minDate = curDate;
    $scope.item = {
        startDate: curDate,
        endDate: moment(curDate).add(1, 'months').toDate()
    };
    $scope.markers = {};
    $scope.$on('leafletDirectiveMap.map.click', function (_, event) {
        marker = {
            lat: event.leafletEvent.latlng.lat,
            lng: event.leafletEvent.latlng.lng,
            draggable: true,
            focus: true
        };
        $scope.markers = {marker: marker};
    });
    $scope.$on('leafletDirectiveMarker.map.click', function () {
        marker = null;
        $scope.markers = {};
    });
    $scope.goBack = function () {
        $window.history.back();
    };
    $scope.submitItem = function () {
        var item = angular.copy($scope.item);
        if (item.startDate > item.endDate) {
            $scope.bidError = 'The auction must end after it starts!';
            return;
        }
        if (marker) {
            item.lat = marker.lat;
            item.lon = marker.lng
        }
        if (item.buyprice) {
            item.buyprice *= 100;
        }
        if (item.firstbid) {
            item.firstbid *= 100;
        }
        var httpPromise;
        if ($stateParams.itemId) {
            httpPromise = $http.put('/api/items/' + $stateParams.itemId, item);
        } else {
            httpPromise = $http.post('/api/items/', item);
        }
        httpPromise.then(function (response) {
            $state.go('main.item', {
                itemId: response.data,
                itemName: $scope.item.name
            });
        }, function(err) {
            $scope.bidError = err.data.message;
        });
    };
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
        $scope.listCategories = response.data.map(function (category) {
            var subcatsNested = category.subcategories.map(makeRecursiveCategories.bind(this, 0));
            category.subcategories = [].concat.apply([], subcatsNested);
            return category;
        });
        console.log($scope.listCategories);
    });

    if ($stateParams.itemId) {
        $http.get('/api/items/' + $stateParams.itemId).then(function (response) {
            var item = response.data;
            marker = {lat: item.lat, lng: item.lon};
            $scope.markers = {marker: marker};
            $scope.item.name = item.name;
            $scope.item.description = item.description;
            $scope.item.category = item.category.id.toString();
            $scope.item.location = item.location;
            $scope.item.country = item.country;
            $scope.item.firstbid = item.currentbid / 100;
            $scope.item.buyprice = item.buyprice / 100;
            $scope.item.startDate = new Date(item.startDate);
            $scope.item.endDate = new Date(item.endDate);
        });
    }
});