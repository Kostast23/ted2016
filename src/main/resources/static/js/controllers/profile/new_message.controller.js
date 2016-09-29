app.controller('NewMessageController', function ($scope, $http, $stateParams, $window, $timeout, AuthService) {
    $scope.message = {};

    if ($stateParams.to) {
        $scope.message.to = $stateParams.to;
    }

    if ($stateParams.subject) {
        $scope.message.subject = "RE: " + $stateParams.subject;
    }

    $scope.goBack = function () {
        $window.history.back();
    };

    $scope.send = function () {
        $http.post('/api/messages/', $scope.message).then(function (response) {
            $scope.goBack();
        }, function (err) {
            $scope.formError = err.data.message;
        });
    };
});