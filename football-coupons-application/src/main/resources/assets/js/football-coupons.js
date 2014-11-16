var app = angular.module('football-coupons', ['ui.router', 'ngCookies', 'ngRoute']);

app.config(function($routeProvider) {
    $routeProvider

        .when('/', {
            templateUrl : 'view/home.html',
            controller  : 'FootballCouponsController'
        })
        .when('/create-game', {
            templateUrl : 'view/create-game.html',
            controller  : 'FootballCouponsAdminController'
        })

});

app.run();

$(document).on('click','.navbar-collapse.in',function(e) {
    if( $(e.target).is('a') ) {
        $(this).collapse('hide');
    }
});
