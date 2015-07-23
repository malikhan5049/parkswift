'use strict';

angular.module('parkswiftApp')
    .controller('BookedParkingSpaceDetailController', function ($scope, $stateParams, BookedParkingSpace, ParkingSpace, ReservedParking) {
        $scope.bookedParkingSpace = {};
        $scope.load = function (id) {
            BookedParkingSpace.get({id: id}, function(result) {
              $scope.bookedParkingSpace = result;
            });
        };
        $scope.load($stateParams.id);
    });
