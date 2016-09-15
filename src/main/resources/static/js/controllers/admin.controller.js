app.controller('AdminController', function ($scope, $http, $state, AdminService, Upload) {
    $scope.currentPage = 1;
    $scope.maxSize = 5;  // number for pagination size
    $scope.itemsPerPage = 10;
    $scope.awaitingUsers = [];

    var getData = function () {
        AdminService.getNotValidated($scope.currentPage, $scope.itemsPerPage).then(function (response) {
            $scope.awaitingUsers = response.content;
            $scope.totalItems = response.totalElements;
        });
    };

    getData();

    $scope.needPagination = function () {
        return $scope.totalItems > $scope.itemsPerPage;
    };

    var confirmValidation = function () {
        return confirm("Accepting a user means you cannot delete them anymore!");
    };

    $scope.acceptUser = function (id) {
        if (confirmValidation()) {
            AdminService.validateUser(id).then(function () {
                getData();
            });
        }
    };

    var confirmDeletion = function () {
        return confirm("Once you delete a user, there is no going back!");
    };

    $scope.deleteUser = function (id) {
        if (confirmDeletion()) {
            AdminService.deleteUser(id).then(function () {
                getData();
            });
        }
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