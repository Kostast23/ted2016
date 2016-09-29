app.controller('MessagesController', function ($scope, $http, $state, $window, $timeout, AuthService) {
    $scope.resourcesLoaded = false;
    $scope.maxSize = 5;  // pagination size
    $scope.itemsPerPage = 10;
    $scope.currentPage = 1;
    $scope.totalItems = 0;
    $scope.messages = [];
    $scope.messageType = null;

    var getMessages = function (messageType, page, size) {
        $http.get('/api/messages/' + messageType, {
            params: {
                page: page,
                size: size
            }
        }).then(function (response) {
            $scope.messages = response.data.content.map(function(message) {
                message.date = new Date(message.date);
                return message;
            });
            $scope.totalItems = response.data.totalElements;
        });
    };

    $scope.needPagination = function() {
        return $scope.totalItems > $scope.itemsPerPage;
    };

    $scope.$watch(function(){
        return $state.$current.name;
    }, function(newVal, oldVal){
        if (newVal === "main.profile.messages_received") {
            $scope.messageType = "received";
        } else if (newVal === "main.profile.messages_sent") {
            $scope.messageType = "sent";
        }
        $scope.$parent.view_tab = $scope.messageType;
        getMessages($scope.messageType, 0, $scope.itemsPerPage);
        resourcesLoaded = true;
    });

    var getReceivedMessages = function() {
        getMessages("received", $scope.currentPage - 1, $scope.itemsPerPage);
    };

    var getSentMessages = function() {
        getMessages("sent", $scope.currentPage - 1, $scope.itemsPerPage);
    };

    $scope.getMessages = function() {
        if ($scope.messageType == "received") {
            getReceivedMessages();
        } else if ($scope.messageType == "sent") {
            getSentMessages();
        }
    };

    $scope.tableSelection = {};

    $scope.selectAll = function() {
        var status = $scope.isAll;
        angular.forEach($scope.messages, function(message) {
            message.selected = status;
        });
    };

    $scope.optionToggled = function(){
        $scope.isAll = $scope.messages.every(function(message) {
            return message.selected;
        })
    };

    $scope.deleteSelected = function() {
        if (confirm('Are you sure you want to delete the selected message(s)?')) {
            angular.forEach($scope.messages, function (message) {
                if (message.selected) {
                    console.log(message.id);
                    $http.delete('/api/messages/' + $scope.messageType + '/' + message.id).then(function() {
                        $scope.getMessages();
                        $scope.isAll = false;
                    });
                }
            });
        }
    };
});