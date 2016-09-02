app.controller('AdminController', function($scope, $http, $state, Upload) {
    $http.get('/api/admin/users/not_validated').then(function (response) {
        $scope.awaitingUsers = response.data;
    });

    var removeFromList = function(id) {
        $scope.awaitingUsers = $scope.awaitingUsers.filter(function(user) {
            return user.id != id;
        });
    };

    $scope.acceptUser = function(id) {
        $http.get('/api/admin/users/' + id + '/validate');
        removeFromList(id);
    };

    $scope.deleteUser = function(id) {
        $http.delete('/api/admin/users/' + id);
        removeFromList(id);
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
});