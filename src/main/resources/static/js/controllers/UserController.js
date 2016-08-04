app.controller('UserController', function($scope, $http, $window, $state) {
    if ($window.localStorage.jwt) {
        $state.go('main.store');
    }

    $scope.doLogin = function() {
        $http.post('/api/login', $scope.login).then(function (resp) {
            console.log('Login success!');
            $scope.loginError = undefined;
            var jwt = resp.data.jwt;
            var jwtObj = JSON.parse(atob(jwt.split('.')[1]));
            $window.localStorage.jwt = jwt;
            $window.localStorage.admin = jwtObj.admin;
            $http.defaults.headers.common.Authorization = 'Bearer ' + jwt;
            $state.go('main.store');
        }, function (err) {
            $scope.loginError = err.data.message;
        });
    };

    $scope.doRegister = function(form) {
        $scope.registerSuccess = false;
        $scope.registerError = null;

        if ($scope.register.password != $scope.register.password2) {
            $scope.pwNoMatch = true;
            $('#rpassword').focus();
        } else {
            $scope.pwNoMatch = false;
            $http.post('/api/register', $scope.register).then(function (resp) {
                $scope.registerSuccess = true;
            }, function (err) {
                $scope.registerError = err.data.message;
            });
        }
    };
});