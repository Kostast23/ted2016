/* fetch the items and map them to a user-friendly form */
app.controller('SuggestionsController', function ($scope, $http) {
    $scope.items = null;
    $http.get('api/items/suggestions').then(function(response) {
        $scope.items = response.data.map(function (item) {
            if (item.currentbid) {
                item.currentbid = +item.currentbid / 100;
            }
            if (item.buyprice) {
                item.buyprice = +item.buyprice / 100;
            }

            if (!item.finished) {
                item.finished = new Date() > new Date(item.endDate);
            }
            item.categories = [];
            var category = item.category;
            while (category) {
                item.categories.unshift(category);
                category = category.parent;
            }
            item.end = (item.finished ? "Closed" : "Ends:");
            item.endOffset = moment(item.endDate).fromNow();
            return item;
        });
    });
});
