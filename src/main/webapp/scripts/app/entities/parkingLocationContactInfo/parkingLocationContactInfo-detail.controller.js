'use strict';

angular.module('parkswiftApp')
    .controller('ParkingLocationContactInfoDetailController', function ($scope, $stateParams, ParkingLocationContactInfo, ParkingLocation) {
        $scope.parkingLocationContactInfo = {};
        $scope.load = function (id) {
            ParkingLocationContactInfo.get({id: id}, function(result) {
              $scope.parkingLocationContactInfo = result;
            });
        };
        $scope.load($stateParams.id);
    });
