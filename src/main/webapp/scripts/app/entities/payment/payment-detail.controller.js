'use strict';

angular.module('parkswiftApp')
    .controller('PaymentDetailController', function ($scope, $stateParams, Payment, User, ReservedParking) {
        $scope.payment = {};
        $scope.load = function (id) {
            Payment.get({id: id}, function(result) {
              $scope.payment = result;
            });
        };
        $scope.load($stateParams.id);
    });
