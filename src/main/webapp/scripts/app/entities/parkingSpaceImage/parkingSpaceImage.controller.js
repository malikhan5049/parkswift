'use strict';

angular.module('parkswiftApp')
    .controller('ParkingSpaceImageController', function ($scope, ParkingSpaceImage, ParkingSpace) {
        $scope.parkingSpaceImages = [];
        $scope.parkingspaces = ParkingSpace.query();
        $scope.loadAll = function() {
            ParkingSpaceImage.query(function(result) {
               $scope.parkingSpaceImages = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            ParkingSpaceImage.get({id: id}, function(result) {
                $scope.parkingSpaceImage = result;
                $('#saveParkingSpaceImageModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.parkingSpaceImage.id != null) {
                ParkingSpaceImage.update($scope.parkingSpaceImage,
                    function () {
                        $scope.refresh();
                    });
            } else {
                ParkingSpaceImage.save($scope.parkingSpaceImage,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            ParkingSpaceImage.get({id: id}, function(result) {
                $scope.parkingSpaceImage = result;
                $('#deleteParkingSpaceImageConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ParkingSpaceImage.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteParkingSpaceImageConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveParkingSpaceImageModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.parkingSpaceImage = {image: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
