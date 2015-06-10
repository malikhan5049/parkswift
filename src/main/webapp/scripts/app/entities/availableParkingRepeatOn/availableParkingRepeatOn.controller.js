'use strict';

angular.module('parkswiftApp')
    .controller('AvailableParkingRepeatOnController', function ($scope, AvailableParkingRepeatOn, AvailableParking) {
        $scope.availableParkingRepeatOns = [];
        $scope.availableparkings = AvailableParking.query();
        $scope.loadAll = function() {
            AvailableParkingRepeatOn.query(function(result) {
               $scope.availableParkingRepeatOns = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            AvailableParkingRepeatOn.get({id: id}, function(result) {
                $scope.availableParkingRepeatOn = result;
                $('#saveAvailableParkingRepeatOnModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.availableParkingRepeatOn.id != null) {
                AvailableParkingRepeatOn.update($scope.availableParkingRepeatOn,
                    function () {
                        $scope.refresh();
                    });
            } else {
                AvailableParkingRepeatOn.save($scope.availableParkingRepeatOn,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            AvailableParkingRepeatOn.get({id: id}, function(result) {
                $scope.availableParkingRepeatOn = result;
                $('#deleteAvailableParkingRepeatOnConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            AvailableParkingRepeatOn.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAvailableParkingRepeatOnConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveAvailableParkingRepeatOnModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.availableParkingRepeatOn = {dayOfMonth: null, dateOfWeek: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
