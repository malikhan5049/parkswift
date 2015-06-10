'use strict';

angular.module('parkswiftApp')
    .controller('ReservedParkingRepeatOnDetailController', function ($scope, $stateParams, ReservedParkingRepeatOn, ReservedParking) {
        $scope.reservedParkingRepeatOn = {};
        $scope.load = function (id) {
            ReservedParkingRepeatOn.get({id: id}, function(result) {
              $scope.reservedParkingRepeatOn = result;
            });
        };
        $scope.load($stateParams.id);
    });
