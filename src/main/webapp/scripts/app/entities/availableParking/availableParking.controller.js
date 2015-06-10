'use strict';

angular.module('parkswiftApp')
    .controller('AvailableParkingController', function ($scope, AvailableParking, ParkingSpace, AvailableParkingRepeatOn) {
        $scope.availableParkings = [];
        $scope.parkingspaces = ParkingSpace.query();
        $scope.availableparkingrepeatons = AvailableParkingRepeatOn.query();
        $scope.loadAll = function() {
            AvailableParking.query(function(result) {
               $scope.availableParkings = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            AvailableParking.get({id: id}, function(result) {
                $scope.availableParking = result;
                $('#saveAvailableParkingModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.availableParking.id != null) {
                AvailableParking.update($scope.availableParking,
                    function () {
                        $scope.refresh();
                    });
            } else {
                AvailableParking.save($scope.availableParking,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            AvailableParking.get({id: id}, function(result) {
                $scope.availableParking = result;
                $('#deleteAvailableParkingConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            AvailableParking.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAvailableParkingConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveAvailableParkingModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.availableParking = {dateStart: null, dateEnd: null, timeStart: null, timeEnd: null, repeatBasis: null, repeatOccurrences: null, description: null, createdAt: null, modifiedAt: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
