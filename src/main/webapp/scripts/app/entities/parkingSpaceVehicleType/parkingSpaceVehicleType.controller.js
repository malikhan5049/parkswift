'use strict';

angular.module('parkswiftApp')
    .controller('ParkingSpaceVehicleTypeController', function ($scope, ParkingSpaceVehicleType, ParkingSpace) {
        $scope.parkingSpaceVehicleTypes = [];
        $scope.parkingspaces = ParkingSpace.query();
        $scope.loadAll = function() {
            ParkingSpaceVehicleType.query(function(result) {
               $scope.parkingSpaceVehicleTypes = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            ParkingSpaceVehicleType.get({id: id}, function(result) {
                $scope.parkingSpaceVehicleType = result;
                $('#saveParkingSpaceVehicleTypeModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.parkingSpaceVehicleType.id != null) {
                ParkingSpaceVehicleType.update($scope.parkingSpaceVehicleType,
                    function () {
                        $scope.refresh();
                    });
            } else {
                ParkingSpaceVehicleType.save($scope.parkingSpaceVehicleType,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            ParkingSpaceVehicleType.get({id: id}, function(result) {
                $scope.parkingSpaceVehicleType = result;
                $('#deleteParkingSpaceVehicleTypeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ParkingSpaceVehicleType.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteParkingSpaceVehicleTypeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveParkingSpaceVehicleTypeModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.parkingSpaceVehicleType = {type: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
