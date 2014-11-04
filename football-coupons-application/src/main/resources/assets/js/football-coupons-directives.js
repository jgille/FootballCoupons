var app = angular.module('football-coupons');

app
    .directive('main', function () {
        return {
            restrict: 'E',
            templateUrl: '/view/main.html'
        };
    })
    .directive('navbar', function () {
        return {
            restrict: 'E',
            templateUrl: '/view/navbar.html'
        };
    })
;