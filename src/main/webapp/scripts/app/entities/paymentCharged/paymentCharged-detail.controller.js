'use strict';

angular.module('parkswiftApp')
    .controller('PaymentChargedDetailController', function ($scope, $stateParams, PaymentCharged, CustomerBooking) {
        $scope.paymentCharged = {};
        $scope.load = function (id) {
            PaymentCharged.get({id: id}, function(result) {
              $scope.paymentCharged = result;
            });
        };
        $scope.load($stateParams.id);
    });
