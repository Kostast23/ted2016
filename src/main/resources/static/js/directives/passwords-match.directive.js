app.directive('passwordsMatch', function () {
    return {
        restrict: 'A',
        require: 'ngModel',
        link: function (scope, elem, attrs, control) {
            var checker = function () {
                var e1 = scope.$eval(attrs.pass1);
                var e2 = scope.$eval(attrs.pass2);
                console.log(e1 + " " + e2);
                return e1 == e2;
            };
            scope.$watch(checker, function (n) {
                control.$setValidity("unique", n);
            });
        }
    };
});