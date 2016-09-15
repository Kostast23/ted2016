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
    $http.get('/api/categories/all').then(function(response) {
        var makeRecursiveCategories = function(depth, cat) {
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
        $scope.listCategories = response.data.map(function(category) {
            var subcatsNested = category.subcategories.map(makeRecursiveCategories.bind(this, 0));
            category.subcategories = [].concat.apply([], subcatsNested);
            return category;
        });
        console.log($scope.listCategories);
    });
});