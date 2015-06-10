'use strict';

angular.module('parkswiftApp')
    .controller('LookupEntryController', function ($scope, LookupEntry, LookupHeader, ParseLinks) {
        $scope.lookupEntrys = [];
        $scope.lookupheaders = LookupHeader.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            LookupEntry.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.lookupEntrys.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.lookupEntrys = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            LookupEntry.get({id: id}, function(result) {
                $scope.lookupEntry = result;
                $('#saveLookupEntryModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.lookupEntry.id != null) {
                LookupEntry.update($scope.lookupEntry,
                    function () {
                        $scope.refresh();
                    });
            } else {
                LookupEntry.save($scope.lookupEntry,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            LookupEntry.get({id: id}, function(result) {
                $scope.lookupEntry = result;
                $('#deleteLookupEntryConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            LookupEntry.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteLookupEntryConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#saveLookupEntryModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.lookupEntry = {value: null, description: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
