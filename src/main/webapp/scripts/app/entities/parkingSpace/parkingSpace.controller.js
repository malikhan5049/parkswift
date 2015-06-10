'use strict';

angular.module('parkswiftApp')
    .controller('ParkingSpaceController', function ($scope, ParkingSpace, ParkingLocation, ParkingSpaceVehicleType, ParkingSpacePriceEntry, ParkingSpaceImage, ReservedParking, AvailableParking, ParseLinks) {
        $scope.parkingSpaces = [];
        $scope.parkinglocations = ParkingLocation.query();
        $scope.parkingspacevehicletypes = ParkingSpaceVehicleType.query();
        $scope.parkingspacepriceentrys = ParkingSpacePriceEntry.query();
        $scope.parkingspaceimages = ParkingSpaceImage.query();
        $scope.reservedparkings = ReservedParking.query();
        $scope.availableparkings = AvailableParking.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            ParkingSpace.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.parkingSpaces.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.parkingSpaces = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
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
                    $scope.reset();
                    $('#deleteParkingSpaceConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#saveParkingSpaceModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.parkingSpace = {description: null, partOfBatch: null, batchNumber: null, fullReserved: null, createdAt: null, modifiedAt: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
