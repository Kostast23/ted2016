app.controller('StoreController', function ($scope, $http) {
    $http.get('/api/categories/').then(function (response) {
        var categories = response.data;

        for (var i = 0; i < categories.length; i++) {
            categories[i].top = categories[i].subcategories.slice(0,5);
        }

        $scope.categories = categories;
    });

    $scope.styles = [
        "collectibles", "clothing", "movies", "sports", "pottery", "toys", "antiques", "electronics",
        "computers", "photo", "home", "coins", "stamps", "tickets", "dolls", "business", "music",
        "books", "jewelry", "else"
    ]
});