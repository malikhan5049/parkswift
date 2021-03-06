'use strict';

angular.module('parkswiftApp')
    .controller('ParkingLocationController', function ($scope, ParkingLocation, ParkingLocationContactInfo, PaypallAccount, User, ParkingLocationFacility, ParkingSpace, ParseLinks) {
        $scope.parkingLocations = [];
        $scope.parkinglocationcontactinfos = ParkingLocationContactInfo.query();
        $scope.paypallaccounts = PaypallAccount.query();
        $scope.users = User.query();
        $scope.parkinglocationfacilitys = ParkingLocationFacility.query();
        $scope.parkingspaces = ParkingSpace.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            ParkingLocation.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.parkingLocations.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.parkingLocations = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
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
                    $scope.reset();
                    $('#deleteParkingLocationConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#saveParkingLocationModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.parkingLocation = {bussinessType: null, addressLine1: null, addressLine2: null, city: null, state: null, country: null, zipCode: null, longitude: null, lattitude: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
