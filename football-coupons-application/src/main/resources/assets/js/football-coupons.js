var app = angular.module('football-coupons', ['ui.router', 'ngCookies', 'ngRoute']);

app.config(function($routeProvider) {
    $routeProvider

        .when('/', {
            templateUrl : 'view/home.html',
            controller  : 'FootballCouponsController'
        })
        .when('/admin/games/create-game', {
            templateUrl : 'view/admin/create-game.html',
            controller  : 'FootballCouponsAdminController'
        })
        .when('/admin/games/list-games', {
            templateUrl : 'view/admin/list-games.html',
            controller  : 'FootballCouponsAdminController'
        })
        .when('/admin/games/edit-game/:game_id', {
            templateUrl : 'view/admin/edit-game.html',
            controller  : 'GameDetailController'
        })

});

app.run();

$(document).on('click','.navbar-collapse.in',function(e) {
    if( $(e.target).is('a') ) {
        $(this).collapse('hide');
    }
});
