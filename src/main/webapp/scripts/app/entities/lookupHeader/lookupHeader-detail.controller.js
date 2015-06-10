'use strict';

angular.module('parkswiftApp')
    .controller('LookupHeaderDetailController', function ($scope, $stateParams, LookupHeader, LookupEntry) {
        $scope.lookupHeader = {};
        $scope.load = function (id) {
            LookupHeader.get({id: id}, function(result) {
              $scope.lookupHeader = result;
            });
        };
        $scope.load($stateParams.id);
    });
