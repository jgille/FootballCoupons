var app = angular.module('football-coupons', ['ui.router', 'ngCookies', 'ngRoute']);

app.config(function($routeProvider) {
    $routeProvider

        .when('/', {
            templateUrl : 'view/home.html',
            controller  : 'FootballCouponsController'
        })

});

app.run();

$(document).on('click','.navbar-collapse.in',function(e) {
    if( $(e.target).is('a') ) {
        $(this).collapse('hide');
    }
});
