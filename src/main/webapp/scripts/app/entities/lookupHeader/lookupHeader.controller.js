'use strict';

angular.module('parkswiftApp')
    .controller('LookupHeaderController', function ($scope, LookupHeader, LookupEntry) {
        $scope.lookupHeaders = [];
        $scope.lookupentrys = LookupEntry.query();
        $scope.loadAll = function() {
            LookupHeader.query(function(result) {
               $scope.lookupHeaders = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            LookupHeader.get({id: id}, function(result) {
                $scope.lookupHeader = result;
                $('#saveLookupHeaderModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.lookupHeader.id != null) {
                LookupHeader.update($scope.lookupHeader,
                    function () {
                        $scope.refresh();
                    });
            } else {
                LookupHeader.save($scope.lookupHeader,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            LookupHeader.get({id: id}, function(result) {
                $scope.lookupHeader = result;
                $('#deleteLookupHeaderConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            LookupHeader.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteLookupHeaderConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveLookupHeaderModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.lookupHeader = {code: null, description: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
