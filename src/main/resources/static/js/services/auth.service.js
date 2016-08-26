app.factory('AuthService', AuthService);

AuthService.$inject = ['$http', '$rootScope', '$window', '$q'];
function AuthService($http, $rootScope, $window, $q) {
	var service = {
		register: register,
		login: login,
		logout: logout,
		user: {}
	};

	angular.element($window).on('storage', function(event) {
	    if (event.key === 'jwt') {
	    	alert(2);
	    	$rootScope.$apply();
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
            var jwtObj = JSON.parse(atob(jwt.split('.')[1]));
            service.user.jwt = jwt;
            service.user.admin = jwtObj.admin;
            $window.localStorage.setItem('user', service.user);
            return $q.resolve(response.data);
        }, function error(response) {
            return $q.reject(response.data.message);
        });
	}

	function logout() {
		service.user = {};
		$window.localStorage.removeItem('user');
	}
};
