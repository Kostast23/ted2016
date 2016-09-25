app.controller('SearchController', function($scope, $http, $stateParams, $location) {
    var doSearch = function(searchParams) {
        $location.search(searchParams);
        $http.post('/api/search', searchParams).then(function(response) {
            $scope.results = response.data;
        });
    };

    $scope.searchParams = $location.search();
    $scope.doSearch = doSearch;

    if ($stateParams.name) {
        $scope.searchParams.name = $stateParams.name;
    }

    if (Object.keys($scope.searchParams).length) {
        doSearch($scope.searchParams);
    }
});