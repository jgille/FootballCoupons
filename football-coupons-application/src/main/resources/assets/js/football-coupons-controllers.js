var app = angular.module('football-coupons');

app.controller('FootballCouponsController', ['$scope', '$cookies', '$http', '$location',
    function ($scope, $cookies, $http, $location) {

        $scope.isLoggedIn = false;
    }]
);