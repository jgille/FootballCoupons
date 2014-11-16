var app = angular.module('football-coupons');

app.controller('FootballCouponsController', ['$scope', '$cookies', '$http', '$location', 'authService',
    function ($scope, $cookies, $http, $location, authService) {

        $scope.games = [
            { id: "1", name: "Spel 1"},
            { id: "2", name: "Spel 2"},
            { id: "3", name: "Spel 3"}
        ];

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

app.controller('FootballCouponsAdminController', ['$scope', '$http',
    function ($scope, $http) {

        $scope.newGame = {
            game_name: "Nytt spel"
        };

        $scope.createGame = function () {
            $http.post('/api/games', $scope.newGame)
                .success(function () {
                    $scope.newGame = {
                        game_name: "Nytt spel"
                    };
                }).error(function () {
                    alert("Something went wrong!");
                });
        };

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
