app.controller('MessageController', function ($scope, $http, $state, $stateParams, $window) {
    $http.get('/api/messages/' + $stateParams.messageId).then(function (response) {
        $scope.message = response.data;
        $scope.message.date = new Date($scope.message.date);
    }, function(err) {
        $scope.error = err.data.message;
    });

    $scope.reply = function() {
        $state.go("main.profile.new_message", { to: $scope.message.to, subject: $scope.message.subject});
    };

    $scope.goBack = function () {
        $window.history.back();
    };
});