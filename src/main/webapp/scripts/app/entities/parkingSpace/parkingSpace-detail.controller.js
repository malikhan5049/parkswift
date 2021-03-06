'use strict';

angular.module('parkswiftApp')
    .controller('ParkingSpaceDetailController', function ($scope, $stateParams, ParkingSpace, ParkingSpaceVehicleType, ParkingSpacePriceEntry, AvailableParking, ReservedParking, ParkingSpaceImage, ParkingLocation) {
        $scope.parkingSpace = {};
        $scope.load = function (id) {
            ParkingSpace.get({id: id}, function(result) {
              $scope.parkingSpace = result;
            });
        };
        $scope.load($stateParams.id);
    });
