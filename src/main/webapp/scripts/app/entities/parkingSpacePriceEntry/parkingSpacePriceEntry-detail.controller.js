'use strict';

angular.module('parkswiftApp')
    .controller('ParkingSpacePriceEntryDetailController', function ($scope, $stateParams, ParkingSpacePriceEntry, ParkingSpace) {
        $scope.parkingSpacePriceEntry = {};
        $scope.load = function (id) {
            ParkingSpacePriceEntry.get({id: id}, function(result) {
              $scope.parkingSpacePriceEntry = result;
            });
        };
        $scope.load($stateParams.id);
    });
