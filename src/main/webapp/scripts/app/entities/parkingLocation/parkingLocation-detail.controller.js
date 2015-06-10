'use strict';

angular.module('parkswiftApp')
    .controller('ParkingLocationDetailController', function ($scope, $stateParams, ParkingLocation, ParkingLocationContactInfo, PaypallAccount, User, ParkingLocationFacility, ParkingSpace) {
        $scope.parkingLocation = {};
        $scope.load = function (id) {
            ParkingLocation.get({id: id}, function(result) {
              $scope.parkingLocation = result;
            });
        };
        $scope.load($stateParams.id);
    });
