'use strict';

angular.module('parkswiftApp')
    .controller('ParkingSpaceImageDetailController', function ($scope, $stateParams, ParkingSpaceImage, ParkingSpace) {
        $scope.parkingSpaceImage = {};
        $scope.load = function (id) {
            ParkingSpaceImage.get({id: id}, function(result) {
              $scope.parkingSpaceImage = result;
            });
        };
        $scope.load($stateParams.id);
    });
