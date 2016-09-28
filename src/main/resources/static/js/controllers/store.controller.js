app.controller('StoreController', function ($scope, $http, $state, $timeout) {
    $http.get('/api/categories/').then(function (response) {
        var categories = response.data;

        for (var i = 0; i < categories.length; i++) {
            categories[i].top = categories[i].subcategories.slice(0,5);
        }

        $scope.categories = categories;
    });

    $scope.doSearch = function (name) {
        if (name) {
            $state.go('main.search', {name: name});
        } else {
            $('#search').popover('show');
            $timeout(function() {$('#search').popover('hide');}, 2000);
        }
    };

    $scope.styles = [
        "collectibles", "clothing", "movies", "sports", "pottery", "toys", "antiques", "electronics",
        "computers", "photo", "home", "coins", "stamps", "tickets", "dolls", "business", "music",
        "books", "jewelry", "else"
    ];

    $(function() {
        $('#search').popover({
            trigger: 'manual',
            placement: 'bottom'
        });
    });
});