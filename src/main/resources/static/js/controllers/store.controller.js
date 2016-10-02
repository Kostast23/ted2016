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

    /* map categories to their styles and images */
    $scope.styles = {
        "Clothing & Accessories": "clothing",
        "Movies & Television": "movies",
        "Toys & Hobbies": "toys",
        "Jewelry, Gems & Watches": "jewelry",
        "Collectibles": "collectibles",
        "Antiques & Art": "antiques",
        "Everything Else": "else",
        "Computers": "computers",
        "Books": "books",
        "Business, Office & Industrial": "business",
        "Music": "music",
        "Stamps": "stamps",
        "Pottery & Glass": "pottery",
        "Consumer Electronics": "electronics",
        "Dolls & Bears": "dolls",
        "Home & Garden": "home",
        "Photo": "photo",
        "Coins": "coins",
        "Tickets & Travel": "tickets",
        "Sports": "sports"
    };

    $(function() {
        $('#search').popover({
            trigger: 'manual',
            placement: 'bottom'
        });
    });
});