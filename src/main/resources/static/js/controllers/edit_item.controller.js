app.controller('EditItemController', function($scope, $http) {
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
    $scope.$on('leafletDirectiveMap.map.click', function(_, event) {
        marker = {
            lat: event.leafletEvent.latlng.lat,
            lng: event.leafletEvent.latlng.lng,
            draggable: true,
            focus: true
        };
        $scope.markers = {marker: marker};
    });
    $scope.$on('leafletDirectiveMarker.map.click', function() {
        marker = null;
        $scope.markers = {};
    });
    $scope.submitItem = function() {
        var item = $scope.item;
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
        $http.post('/api/items/', item);
    };
});