app.factory('AdminService', AdminService);

AdminService.$inject = ['$http', '$q'];
function AdminService($http, $q) {
    var service = {
        getUser: getUser,
        validateUser: validateUser,
        deleteUser: deleteUser,
        getNotValidated: getNotValidated
    };

    return service;

    function getUser(id) {
        return $http.get('/api/admin/users/' + id).then(function success(response) {
            return $q.resolve(response.data);
        }, function error(response) {
            return $q.reject(response.data.message);
        });
    }

    function validateUser(id) {
        return $http.get('/api/admin/users/' + id + '/validate').then(function success() {
            return $q.resolve();
        });
    }

    function deleteUser(id) {
        return $http.delete('/api/admin/users/' + id).then(function success() {
            return $q.resolve();
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