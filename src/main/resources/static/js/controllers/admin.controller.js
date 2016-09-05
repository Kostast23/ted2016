app.controller('AdminController', function($scope, $http, $state, Upload) {
    $scope.currentPage = 1;
    $scope.itemsPerPage = 10;
    $scope.awaitingUsers = [];

    var getData = function() {
        $http.get('/api/admin/users/not_validated', {
            params: {
                page: $scope.currentPage-1,
                size: $scope.itemsPerPage
            }
        }).then(function (response) {
            $scope.awaitingUsers = response.data.content;
            $scope.totalItems = response.data.totalElements;
        });
    };

    getData();

    var removeFromList = function(id) {
        $scope.awaitingUsers = $scope.awaitingUsers.filter(function(user) {
            return user.id != id;
        });
    };

    $scope.acceptUser = function(id) {
        $http.get('/api/admin/users/' + id + '/validate').then(function() {
            getData();
        });
    };

    $scope.deleteUser = function(id) {
        $http.delete('/api/admin/users/' + id).then(function() {
            getData();
        });
    };

    $scope.doUpload = function(file) {
        Upload.upload({
            url: '/api/admin/uploadBackup',
            data: {file: file}
        }).then(function (resp) {
            console.log('Success ' + resp.config.data.file.name + 'uploaded. Response: ' + resp.data);
        }, function (resp) {
            console.log('Error status: ' + resp.status);
        }, function (evt) {
            var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
            console.log('progress: ' + progressPercentage + '% ' + evt.config.data.file.name);
        });
    };

    $scope.downloadDump = function() {
        $http.get('/api/admin/dumpDatabase');
    };

    $scope.pageChanged = function() {
        getData();
    };
});