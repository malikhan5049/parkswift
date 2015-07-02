'use strict';

angular.module('parkswiftApp')
    .controller('AvailableParkingController', function ($scope, AvailableParking, ParkingSpace, AvailableParkingRepeatOn, ParseLinks) {
        $scope.availableParkings = [];
        $scope.parkingspaces = ParkingSpace.query();
        $scope.availableparkingrepeatons = AvailableParkingRepeatOn.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            AvailableParking.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.availableParkings.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.availableParkings = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
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
                    $scope.reset();
                    $('#deleteAvailableParkingConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#saveAvailableParkingModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.availableParking = {startDate: null, endDate: null, startTime: null, endTime: null, repeatOn: null, repeatOccurrences: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
