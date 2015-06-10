'use strict';

angular.module('parkswiftApp')
    .controller('AvailableParkingDetailController', function ($scope, $stateParams, AvailableParking, ParkingSpace, AvailableParkingRepeatOn) {
        $scope.availableParking = {};
        $scope.load = function (id) {
            AvailableParking.get({id: id}, function(result) {
              $scope.availableParking = result;
            });
        };
        $scope.load($stateParams.id);
    });
