'use strict';

angular.module('parkswiftApp')
    .controller('ParkingLocationFacilityDetailController', function ($scope, $stateParams, ParkingLocationFacility, ParkingLocation) {
        $scope.parkingLocationFacility = {};
        $scope.load = function (id) {
            ParkingLocationFacility.get({id: id}, function(result) {
              $scope.parkingLocationFacility = result;
            });
        };
        $scope.load($stateParams.id);
    });
