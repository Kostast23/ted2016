app.factory('AdminService', AdminService);

AdminService.$inject = ['$http', '$q'];
function AdminService($http, $q) {
    var service = {
        getUser: getUser,
        validateUser: validateUser,
        deleteUser: deleteUser,
        getValidated: getValidated,
        getNotValidated: getNotValidated
    };

    return service;

    function getUser(id) {
        id = parseInt(id, 10);
        return $http.get('/api/admin/users/' + id).then(function success(response) {
            return $q.resolve(response.data);
        }, function error(response) {
            return $q.reject(response.data.message);
        });
    }

    function validateUser(id) {
        id = parseInt(id, 10);
        return $http.get('/api/admin/users/not_validated/' + id + '/validate').then(function success() {
            return $q.resolve();
        });
    }

    function deleteUser(id) {
        id = parseInt(id, 10);
        return $http.delete('/api/admin/users/not_validated/' + id).then(function success() {
            return $q.resolve();
        });
    }

    function getValidated(page, itemsPerPage) {
        return $http.get('/api/admin/users/validated', {
            params: {
                page: page - 1,
                size: itemsPerPage
            }
        }).then(function success(response) {
            return $q.resolve(response.data);
        });
    }

    function getNotValidated(page, itemsPerPage) {
        return $http.get('/api/admin/users/not_validated', {
            params: {
                page: page - 1,
                size: itemsPerPage
            }
        }).then(function success(response) {
            return $q.resolve(response.data);
        });
    }
}