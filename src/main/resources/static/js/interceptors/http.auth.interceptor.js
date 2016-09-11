app.factory('httpAuthInterceptor', function ($injector) {
    return {
        request: function (config) {
            var AuthService = $injector.get('AuthService');
            var jwt = AuthService.user.jwt;
            if (jwt) {
                config.headers['Authorization'] = 'Bearer ' + jwt;
            }
            return config;
        }
    };
});