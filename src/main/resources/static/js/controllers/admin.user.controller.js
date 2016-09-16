app.controller('AdminUserController', function ($scope, $state, $stateParams, $timeout, leafletData, AdminService) {
    $scope.resourcesLoaded = false;

    AdminService.getUser($stateParams.userId).then(function (user) {
        $scope.user = user;
        $scope.resourcesLoaded = true;
        if (user.latitude && user.longitude) {
            angular.extend($scope, {
                markers: {
                    item: {
                        lat: user.latitude,
                        lng: user.longitude
                    }
                }
            });
            $scope.$on('leafletDirectiveMap.map.layeradd', function() {
                leafletData.getMap().then(function (map) {
                    map.setView([user.latitude, user.longitude], 5);
                    $timeout(function() {
                        L.Util.requestAnimFrame(map.invalidateSize,map,!1,map._container);
                    }, 100);
                });
            });
        }
    }, function (error) {
        $state.go('main.admin');
    });

    $scope.acceptUser = function (id) {
        AdminService.validateUser(id).then(function () {
            $scope.user.validated = true;
            $state.go('main.admin');
        });
    };

    $scope.deleteUser = function (id) {
        AdminService.deleteUser(id).then(function () {
            $state.go('main.admin');
        });
    };

    $scope.ifEmpty = function(val) {
        if (angular.isUndefined(val) || val === null || val === '') {
            return true;
        }
    };
});