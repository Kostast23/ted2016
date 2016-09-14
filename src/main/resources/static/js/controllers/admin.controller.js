app.controller('AdminController', function ($scope, $http, $state, Upload) {
    $scope.currentPage = 1;
    $scope.maxSize = 5;  // number for pagination size
    $scope.itemsPerPage = 10;
    $scope.awaitingUsers = [];

    var getData = function () {
        $http.get('/api/admin/users/not_validated', {
            params: {
                page: $scope.currentPage - 1,
                size: $scope.itemsPerPage
            }
        }).then(function (response) {
            $scope.awaitingUsers = response.data.content;
            $scope.totalItems = response.data.totalElements;
        });
    };

    getData();

    $scope.needPagination = function () {
        return $scope.totalItems > $scope.itemsPerPage;
    };

    $scope.acceptUser = function (id) {
        $http.get('/api/admin/users/' + id + '/validate').then(function () {
            getData();
        });
    };

    $scope.deleteUser = function (id) {
        $http.delete('/api/admin/users/' + id).then(function () {
            getData();
        });
    };

    $scope.doUpload = function (file) {
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

    $scope.downloadDump = function () {
        $http.get('/api/admin/dumpDatabase', {
            transformResponse: angular.identity
        }).then(function (response) {
            var text = response.data;
            var filename = "dump.xml";
            var blob = new Blob([text], {type: "application/xml;charset=utf-8"});
            saveAs(blob, filename);
        });
    };

    $scope.pageChanged = function () {
        getData();
    };
});