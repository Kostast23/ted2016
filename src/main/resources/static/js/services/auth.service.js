app.factory('AuthService', AuthService);

AuthService.$inject = ['$http', '$window', '$q'];
function AuthService($http, $window, $q) {
	var service = {
		register: register,
		login: login,
		logout: logout,
        setUser: setUser,
		user: {}
	};

	angular.element($window).on('storage', function(event) {
	    if (event.key === 'jwt') {
	        $window.location.reload();
	    }
	});

	return service;
	
	function register(registerInfo) {
		return $http.post('/api/register', registerInfo).then(function success(response) {
            return $q.resolve(response.data);
        }, function error(response) {
            return $q.reject(response.data.message);
        });
	}

	function login(loginInfo) {
		return $http.post('/api/login', loginInfo).then(function success(response) {
            console.log('Login success!');
            var jwt = response.data.jwt;
            service.setUser(jwt);
            // Do not store the user object as objects get stringified.
            $window.localStorage.setItem('jwt', jwt);
            return $q.resolve(response.data);
        }, function error(response) {
            return $q.reject(response.data.message);
        });
	}

	function logout() {
		service.user = {};
		$window.localStorage.removeItem('jwt');
	}

	function setUser(jwt) {
        var jwtObj = JSON.parse(atob(jwt.split('.')[1]));
        service.user.jwt = jwt;
		service.user.user = jwtObj.user;
        service.user.admin = jwtObj.admin;
    }
}
