'use strict';

angular.module('parkswiftApp')
    .controller('ParkingLocationController', function ($scope, ParkingLocation, ParkingLocationContactInfo, PaypallAccount, User, ParkingLocationFacility, ParkingSpace) {
        $scope.parkingLocations = [];
        $scope.parkinglocationcontactinfos = ParkingLocationContactInfo.query();
        $scope.paypallaccounts = PaypallAccount.query();
        $scope.users = User.query();
        $scope.parkinglocationfacilitys = ParkingLocationFacility.query();
        $scope.parkingspaces = ParkingSpace.query();
        $scope.loadAll = function() {
            ParkingLocation.query(function(result) {
               $scope.parkingLocations = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            ParkingLocation.get({id: id}, function(result) {
                $scope.parkingLocation = result;
                $('#saveParkingLocationModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.parkingLocation.id != null) {
                ParkingLocation.update($scope.parkingLocation,
                    function () {
                        $scope.refresh();
                    });
            } else {
                ParkingLocation.save($scope.parkingLocation,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            ParkingLocation.get({id: id}, function(result) {
                $scope.parkingLocation = result;
                $('#deleteParkingLocationConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ParkingLocation.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteParkingLocationConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveParkingLocationModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.parkingLocation = {propertyType: null, numberOfSpaces: null, addressLine1: null, addressLine2: null, city: null, state: null, country: null, zipCode: null, longitude: null, lattitude: null, createdAt: null, modifiedAt: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
