'use strict';

angular.module('parkswiftApp')
    .controller('AvailableParkingRepeatOnDetailController', function ($scope, $stateParams, AvailableParkingRepeatOn, AvailableParking) {
        $scope.availableParkingRepeatOn = {};
        $scope.load = function (id) {
            AvailableParkingRepeatOn.get({id: id}, function(result) {
              $scope.availableParkingRepeatOn = result;
            });
        };
        $scope.load($stateParams.id);
    });
