'use strict';

angular.module('parkswiftApp')
    .controller('ParkingSpaceController', function ($scope, ParkingSpace, ParkingSpaceVehicleType, ParkingSpacePriceEntry, AvailableParking, ReservedParking, ParkingSpaceImage, ParkingLocation) {
        $scope.parkingSpaces = [];
        $scope.parkingspacevehicletypes = ParkingSpaceVehicleType.query();
        $scope.parkingspacepriceentrys = ParkingSpacePriceEntry.query();
        $scope.availableparkings = AvailableParking.query();
        $scope.reservedparkings = ReservedParking.query();
        $scope.parkingspaceimages = ParkingSpaceImage.query();
        $scope.parkinglocations = ParkingLocation.query();
        $scope.loadAll = function() {
            ParkingSpace.query(function(result) {
               $scope.parkingSpaces = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            ParkingSpace.get({id: id}, function(result) {
                $scope.parkingSpace = result;
                $('#saveParkingSpaceModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.parkingSpace.id != null) {
                ParkingSpace.update($scope.parkingSpace,
                    function () {
                        $scope.refresh();
                    });
            } else {
                ParkingSpace.save($scope.parkingSpace,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            ParkingSpace.get({id: id}, function(result) {
                $scope.parkingSpace = result;
                $('#deleteParkingSpaceConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ParkingSpace.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteParkingSpaceConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveParkingSpaceModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.parkingSpace = {description: null, numberOfSpaces: null, groupRecord: null, groupNumber: null, fullReserved: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
