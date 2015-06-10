'use strict';

angular.module('parkswiftApp')
    .controller('FavouriteController', function ($scope, Favourite, User, ParkingSpace) {
        $scope.favourites = [];
        $scope.users = User.query();
        $scope.parkingspaces = ParkingSpace.query();
        $scope.loadAll = function() {
            Favourite.query(function(result) {
               $scope.favourites = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Favourite.get({id: id}, function(result) {
                $scope.favourite = result;
                $('#saveFavouriteModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.favourite.id != null) {
                Favourite.update($scope.favourite,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Favourite.save($scope.favourite,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Favourite.get({id: id}, function(result) {
                $scope.favourite = result;
                $('#deleteFavouriteConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Favourite.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteFavouriteConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveFavouriteModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.favourite = {id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
