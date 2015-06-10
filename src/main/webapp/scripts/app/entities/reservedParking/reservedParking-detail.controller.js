'use strict';

angular.module('parkswiftApp')
    .controller('ReservedParkingDetailController', function ($scope, $stateParams, ReservedParking, ParkingSpace, ReservedParkingRepeatOn, Payment) {
        $scope.reservedParking = {};
        $scope.load = function (id) {
            ReservedParking.get({id: id}, function(result) {
              $scope.reservedParking = result;
            });
        };
        $scope.load($stateParams.id);
    });
