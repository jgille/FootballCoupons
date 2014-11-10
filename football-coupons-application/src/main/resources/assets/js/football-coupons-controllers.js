var app = angular.module('football-coupons');

app.controller('FootballCouponsController', ['$scope', '$cookies', '$http', '$location', 'authService',
    function ($scope, $cookies, $http, $location, authService) {

        $scope.load = function () {
            $scope.user = authService.getUser();
        };

        $scope.logout = function () {
            authService.logout();
            $scope.load();
        };

        $scope.load();

    }]
);

app.factory('authInterceptor', function ($rootScope, $q, $window, $cookies) {
    return {
        request: function (config) {
            config.headers = config.headers || {};
            config.headers.Authorization = 'Bearer ' + $cookies.jwt;
            return config;
        }
    };
});

app.config(function ($httpProvider) {
    $httpProvider.interceptors.push('authInterceptor');
});
