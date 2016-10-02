app.controller('EditItemController', function ($scope, $http, $state, $stateParams, $window, $timeout, FileUploader, AuthService) {
    $scope.currentUser = AuthService.user.user;

    var marker = null;
    var curDate = new Date();
    curDate.setSeconds(0);
    curDate.setMilliseconds(0);
    console.log(curDate);
    $scope.markers = {};
    $scope.minDate = curDate;
    $scope.item = {
        new: !$stateParams.itemId,
        name: null,
        endDate: moment(curDate).add(1, 'months').set('second', 0).toDate()
    };

    var createTimePicker = function(date) {
        $(function () {
            var picker = $('#endDate');
            picker.datetimepicker({
                format: 'd/m/Y H:i',
                value: date
            });
        });
    };

    if ($scope.item.new) {
        createTimePicker($scope.item.endDate);
    }

    $http.get('/api/categories/all').then(function (response) {
        $timeout(function() {$scope.listCategories = response.data;}, 10);
    });

    if ($stateParams.itemId) {
        $http.get('/api/items/' + $stateParams.itemId).then(function (response) {
            var item = response.data;
            marker = {
                lat: item.lat,
                lng: item.lon,
                draggable: true,
                focus: true
            };
            $scope.markers = {marker: marker};
            $scope.item.name = item.name;
            $scope.item.description = item.description;
            $scope.item.category = item.category.id.toString();
            $scope.item.location = item.location;
            $scope.item.country = item.country;
            $scope.item.firstbid = item.currentbid / 100;
            $scope.item.buyprice = item.buyprice / 100;
            $scope.item.startDate = new Date(item.startDate);
            createTimePicker(new Date(item.endDate));
            $scope.item.images = item.images;
            $scope.item.seller = item.sellerUsername;
        }, function() {
            $state.go('page_not_found');
        });
    } else if ($stateParams.category) {
        $scope.item.category = $stateParams.category.toString();
    }

    $scope.uploader = new FileUploader({
        url: '/api/images/upload',
        removeAfterUpload: true,
        headers: { 'Authorization': 'Bearer ' + AuthService.user.jwt }
    });

    $scope.uploader.onSuccessItem = function (fileItem, response, status, headers) {
        if (!$scope.item.images) {
            $scope.item.images = [];
        }
        $scope.item.images.push(response);
    };

    $scope.removeImage = function (img) {
        var idx = $scope.item.images.indexOf(img);
        if (idx > -1) {
            $scope.item.images.splice(idx, 1);
        }
        $http.delete('/api/images/' + img);
    };

    $scope.center = {
        autoDiscover: true
    };

    /* on map click, set the marker */
    $scope.$on('leafletDirectiveMap.map.click', function (_, event) {
        marker = {
            lat: event.leafletEvent.latlng.lat,
            lng: event.leafletEvent.latlng.lng,
            draggable: true,
            focus: true
        };
        $scope.markers = {marker: marker};
    });

    /* remove the marker if it's clicked */
    $scope.$on('leafletDirectiveMarker.map.click', function () {
        marker = null;
        $scope.markers = {};
    });

    /* update the marker on drag */
    $scope.$on('leafletDirectiveMarker.map.dragend', function (_, event) {
        marker.lat = event.leafletEvent.target._latlng.lat;
        marker.lng = event.leafletEvent.target._latlng.lng;
    });

    $scope.goBack = function () {
        $window.history.back();
    };

    $scope.submitItem = function () {
        var item = angular.copy($scope.item);
        if (item.new) {
            item.startDate = new Date();
        }
        if (marker) {
            item.lat = marker.lat % 360;
            if (item.lat > 180) {
                item.lat -= 360;
            }
            item.lon = marker.lng % 360;
            if (item.lon > 180) {
                item.lon -= 360;
            }
        }
        if (item.buyprice) {
            item.buyprice *= 100;
        } else {
            item.buyprice = null;
        }
        if (item.firstbid) {
            item.firstbid *= 100;
        }
        item.endDate = $('#endDate').datetimepicker('getValue');
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
        }, function (err) {
            $scope.formError = err.data.message;
        });
    };
});