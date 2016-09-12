app.controller('UserController', function($scope, $http, $state, AuthService) {
    if (AuthService.user.jwt) {
        $state.go('main.store');
    }

    $scope.invalid = function(field) {
        return field.$touched && field.$invalid;
    };

    $scope.doLogin = function() {
        AuthService.login($scope.login).then(function() {
        	$scope.loginError = null;
        	$state.go('main.store');
        }, function(error) {
        	$scope.loginError = error;
        });
    };

    $scope.doRegister = function(form) {
    	$scope.pwNoMatch = false;
        $scope.registerSuccess = false;
        $scope.registerError = null;

        if ($scope.register.password != $scope.register.password2) {
            $scope.pwNoMatch = true;
            $('#rpassword').focus();
        } else {
        	AuthService.register($scope.register).then(function() {
        		$scope.registerError = null;
        		$scope.registerSuccess = true;
        	}, function(error) {
        		$scope.registerError = error;
        	});
        }
    };
});