app.controller('StoreController', function ($scope, $http) {
    var otherCategories = [];
    var categories = [{
        name: 'Clothing',
        subcategories: ['Clothing & Accessories', 'Men', 'Women', 'Footwear'],
        results: []
    }, {
        name: 'Toys',
        subcategories: ['Toys & Hobbies', 'Electronic, Battery, Wind-Up', 'Electronic, Interactive'],
        results: []
    }, {
        name: 'Other categories',
        subcategories: [],
        results: otherCategories
    }];

    $http.get('/api/categories/').then(function (response) {
        response.data.forEach(function (subcategory) {
            var found = false;
            categories.forEach(function (category) {
                if (category.subcategories.includes(subcategory.name)) {
                    category.results.push(subcategory);
                    found = true;
                    return;
                }
            });
            if (!found)
                otherCategories.push(subcategory);
        });
        categories.forEach(function (category) {
            category.results.sort(function (a, b) {
                return b.count - a.count;
            });
        });
        $scope.categories = categories;
    });
});