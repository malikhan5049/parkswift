'use strict';

angular.module('parkswiftApp')
    .controller('ParkingLocationContactInfoController', function ($scope, ParkingLocationContactInfo, ParkingLocation) {
        $scope.parkingLocationContactInfos = [];
        $scope.parkinglocations = ParkingLocation.query();
        $scope.loadAll = function() {
            ParkingLocationContactInfo.query(function(result) {
               $scope.parkingLocationContactInfos = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            ParkingLocationContactInfo.get({id: id}, function(result) {
                $scope.parkingLocationContactInfo = result;
                $('#saveParkingLocationContactInfoModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.parkingLocationContactInfo.id != null) {
                ParkingLocationContactInfo.update($scope.parkingLocationContactInfo,
                    function () {
                        $scope.refresh();
                    });
            } else {
                ParkingLocationContactInfo.save($scope.parkingLocationContactInfo,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            ParkingLocationContactInfo.get({id: id}, function(result) {
                $scope.parkingLocationContactInfo = result;
                $('#deleteParkingLocationContactInfoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ParkingLocationContactInfo.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteParkingLocationContactInfoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveParkingLocationContactInfoModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.parkingLocationContactInfo = {firstName: null, lastName: null, phone1: null, phone2: null, email1: null, email2: null, createdAt: null, modifiedAt: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
