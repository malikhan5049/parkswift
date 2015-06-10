'use strict';

angular.module('parkswiftApp')
    .controller('LookupEntryDetailController', function ($scope, $stateParams, LookupEntry, LookupHeader) {
        $scope.lookupEntry = {};
        $scope.load = function (id) {
            LookupEntry.get({id: id}, function(result) {
              $scope.lookupEntry = result;
            });
        };
        $scope.load($stateParams.id);
    });
