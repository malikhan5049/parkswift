'use strict';

angular.module('parkswiftApp')
    .controller('FavouriteDetailController', function ($scope, $stateParams, Favourite, User, ParkingSpace) {
        $scope.favourite = {};
        $scope.load = function (id) {
            Favourite.get({id: id}, function(result) {
              $scope.favourite = result;
            });
        };
        $scope.load($stateParams.id);
    });
