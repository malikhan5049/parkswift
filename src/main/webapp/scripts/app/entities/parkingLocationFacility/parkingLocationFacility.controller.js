'use strict';

angular.module('parkswiftApp')
    .controller('ParkingLocationFacilityController', function ($scope, ParkingLocationFacility, ParkingLocation) {
        $scope.parkingLocationFacilitys = [];
        $scope.parkinglocations = ParkingLocation.query();
        $scope.loadAll = function() {
            ParkingLocationFacility.query(function(result) {
               $scope.parkingLocationFacilitys = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            ParkingLocationFacility.get({id: id}, function(result) {
                $scope.parkingLocationFacility = result;
                $('#saveParkingLocationFacilityModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.parkingLocationFacility.id != null) {
                ParkingLocationFacility.update($scope.parkingLocationFacility,
                    function () {
                        $scope.refresh();
                    });
            } else {
                ParkingLocationFacility.save($scope.parkingLocationFacility,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            ParkingLocationFacility.get({id: id}, function(result) {
                $scope.parkingLocationFacility = result;
                $('#deleteParkingLocationFacilityConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ParkingLocationFacility.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteParkingLocationFacilityConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveParkingLocationFacilityModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.parkingLocationFacility = {facility: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
