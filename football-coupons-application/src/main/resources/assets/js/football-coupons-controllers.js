var app = angular.module('football-coupons');

app.controller('FootballCouponsController', ['$scope', '$cookies', '$http', '$location',
    function ($scope, $cookies, $http, $location) {

        $scope.isLoggedIn = false;

        var getJwt = function () {
            return $cookies.jwt;
        };

        var isUserLoggedIn = function () {
            var jwt = getJwt();
            return typeof jwt !== 'undefined' && jwt !== '';
        };

        var decodeBase64 = function (s) {
            var e = {}, i, v = [], r = '', w = String.fromCharCode;
            var n = [
                [65, 91],
                [97, 123],
                [48, 58],
                [43, 44],
                [47, 48]
            ];

            for (z in n) {
                for (i = n[z][0]; i < n[z][1]; i++) {
                    v.push(w(i));
                }
            }
            for (i = 0; i < 64; i++) {
                e[v[i]] = i;
            }

            for (i = 0; i < s.length; i += 72) {
                var b = 0, c, x, l = 0, o = s.substring(i, i + 72);
                for (x = 0; x < o.length; x++) {
                    c = e[o.charAt(x)];
                    b = (b << 6) + c;
                    l += 6;
                    while (l >= 8) {
                        r += w((b >>> (l -= 8)) % 256);
                    }
                }
            }
            return r;
        };

        $scope.load = function () {
            $scope.isLoggedIn = isUserLoggedIn();

            if ($scope.isLoggedIn === true) {
                var token = getJwt();
                var json = JSON.parse(decodeBase64(token.split('.')[1]));
                $scope.username = json.username;
                $scope.isAdmin = json.is_admin_user;
            }
        };

        $scope.logout = function () {
            $cookies.jwt = '';
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
