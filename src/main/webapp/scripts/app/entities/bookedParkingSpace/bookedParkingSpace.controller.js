'use strict';

angular.module('parkswiftApp')
    .controller('BookedParkingSpaceController', function ($scope, BookedParkingSpace, ParkingSpace, ReservedParking) {
        $scope.bookedParkingSpaces = [];
        $scope.parkingspaces = ParkingSpace.query();
        $scope.reservedparkings = ReservedParking.query();
        $scope.loadAll = function() {
            BookedParkingSpace.query(function(result) {
               $scope.bookedParkingSpaces = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            BookedParkingSpace.get({id: id}, function(result) {
                $scope.bookedParkingSpace = result;
                $('#saveBookedParkingSpaceModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.bookedParkingSpace.id != null) {
                BookedParkingSpace.update($scope.bookedParkingSpace,
                    function () {
                        $scope.refresh();
                    });
            } else {
                BookedParkingSpace.save($scope.bookedParkingSpace,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            BookedParkingSpace.get({id: id}, function(result) {
                $scope.bookedParkingSpace = result;
                $('#deleteBookedParkingSpaceConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            BookedParkingSpace.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteBookedParkingSpaceConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveBookedParkingSpaceModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.bookedParkingSpace = {id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
