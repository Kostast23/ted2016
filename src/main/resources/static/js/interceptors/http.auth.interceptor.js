app.factory('httpAuthInterceptor', function ($injector) {
    return {
        request: function (config) {
            /* intercept http requests and insert our auth header */
            var AuthService = $injector.get('AuthService');
            var jwt = AuthService.user.jwt;
            if (jwt) {
                config.headers['Authorization'] = 'Bearer ' + jwt;
            }
            return config;
        }
    };
});