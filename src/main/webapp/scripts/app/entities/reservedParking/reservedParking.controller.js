'use strict';

angular.module('parkswiftApp')
    .controller('ReservedParkingController', function ($scope, ReservedParking, Payment, ReservedParkingRepeatOn, ParkingSpace, ParseLinks) {
        $scope.reservedParkings = [];
        $scope.payments = Payment.query();
        $scope.reservedparkingrepeatons = ReservedParkingRepeatOn.query();
        $scope.parkingspaces = ParkingSpace.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            ReservedParking.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.reservedParkings.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.reservedParkings = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            ReservedParking.get({id: id}, function(result) {
                $scope.reservedParking = result;
                $('#saveReservedParkingModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.reservedParking.id != null) {
                ReservedParking.update($scope.reservedParking,
                    function () {
                        $scope.refresh();
                    });
            } else {
                ReservedParking.save($scope.reservedParking,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            ReservedParking.get({id: id}, function(result) {
                $scope.reservedParking = result;
                $('#deleteReservedParkingConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ReservedParking.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteReservedParkingConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#saveReservedParkingModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.reservedParking = {startDate: null, endDate: null, startTime: null, endTime: null, repeatOn: null, repeatOccurrences: null, status: null, reservedOn: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
