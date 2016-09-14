app.controller('AdminUserController', function ($scope, $state, $stateParams, leafletData, AdminService) {
    $scope.user = {username: "User Not Found"};

    AdminService.getUser($stateParams.userId).then(function (user) {
        $scope.user = user;
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
                });
            });
        }
    }, function (error) {
        $state.go('main.admin');
    });

    $scope.acceptUser = function (id) {
        AdminService.validateUser(id).then(function () {
            $scope.user.validated = true;
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
    }
});