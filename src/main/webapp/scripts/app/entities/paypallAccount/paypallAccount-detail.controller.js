'use strict';

angular.module('parkswiftApp')
    .controller('PaypallAccountDetailController', function ($scope, $stateParams, PaypallAccount, User) {
        $scope.paypallAccount = {};
        $scope.load = function (id) {
            PaypallAccount.get({id: id}, function(result) {
              $scope.paypallAccount = result;
            });
        };
        $scope.load($stateParams.id);
    });
