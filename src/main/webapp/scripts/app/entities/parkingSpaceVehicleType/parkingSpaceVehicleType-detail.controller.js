'use strict';

angular.module('parkswiftApp')
    .controller('ParkingSpaceVehicleTypeDetailController', function ($scope, $stateParams, ParkingSpaceVehicleType, ParkingSpace) {
        $scope.parkingSpaceVehicleType = {};
        $scope.load = function (id) {
            ParkingSpaceVehicleType.get({id: id}, function(result) {
              $scope.parkingSpaceVehicleType = result;
            });
        };
        $scope.load($stateParams.id);
    });
