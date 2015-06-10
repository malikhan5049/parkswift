'use strict';

angular.module('parkswiftApp')
    .controller('ReservedParkingRepeatOnController', function ($scope, ReservedParkingRepeatOn, ReservedParking) {
        $scope.reservedParkingRepeatOns = [];
        $scope.reservedparkings = ReservedParking.query();
        $scope.loadAll = function() {
            ReservedParkingRepeatOn.query(function(result) {
               $scope.reservedParkingRepeatOns = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            ReservedParkingRepeatOn.get({id: id}, function(result) {
                $scope.reservedParkingRepeatOn = result;
                $('#saveReservedParkingRepeatOnModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.reservedParkingRepeatOn.id != null) {
                ReservedParkingRepeatOn.update($scope.reservedParkingRepeatOn,
                    function () {
                        $scope.refresh();
                    });
            } else {
                ReservedParkingRepeatOn.save($scope.reservedParkingRepeatOn,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            ReservedParkingRepeatOn.get({id: id}, function(result) {
                $scope.reservedParkingRepeatOn = result;
                $('#deleteReservedParkingRepeatOnConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ReservedParkingRepeatOn.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteReservedParkingRepeatOnConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveReservedParkingRepeatOnModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.reservedParkingRepeatOn = {dateOfMonth: null, dayOfWeek: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
