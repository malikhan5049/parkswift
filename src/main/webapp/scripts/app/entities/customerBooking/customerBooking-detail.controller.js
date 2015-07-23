'use strict';

angular.module('parkswiftApp')
    .controller('CustomerBookingDetailController', function ($scope, $stateParams, CustomerBooking, BookedParkingSpace, Payment, User, ReservedParking) {
        $scope.customerBooking = {};
        $scope.load = function (id) {
            CustomerBooking.get({id: id}, function(result) {
              $scope.customerBooking = result;
            });
        };
        $scope.load($stateParams.id);
    });
