'use strict';

angular.module('parkswiftApp')
    .controller('ParkingSpacePriceEntryController', function ($scope, ParkingSpacePriceEntry, ParkingSpace) {
        $scope.parkingSpacePriceEntrys = [];
        $scope.parkingspaces = ParkingSpace.query();
        $scope.loadAll = function() {
            ParkingSpacePriceEntry.query(function(result) {
               $scope.parkingSpacePriceEntrys = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            ParkingSpacePriceEntry.get({id: id}, function(result) {
                $scope.parkingSpacePriceEntry = result;
                $('#saveParkingSpacePriceEntryModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.parkingSpacePriceEntry.id != null) {
                ParkingSpacePriceEntry.update($scope.parkingSpacePriceEntry,
                    function () {
                        $scope.refresh();
                    });
            } else {
                ParkingSpacePriceEntry.save($scope.parkingSpacePriceEntry,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            ParkingSpacePriceEntry.get({id: id}, function(result) {
                $scope.parkingSpacePriceEntry = result;
                $('#deleteParkingSpacePriceEntryConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ParkingSpacePriceEntry.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteParkingSpacePriceEntryConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveParkingSpacePriceEntryModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.parkingSpacePriceEntry = {type: null, price: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
