'use strict';

angular.module('parkswiftApp')
    .controller('PaypallAccountController', function ($scope, PaypallAccount, User) {
        $scope.paypallAccounts = [];
        $scope.users = User.query();
        $scope.loadAll = function() {
            PaypallAccount.query(function(result) {
               $scope.paypallAccounts = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            PaypallAccount.get({id: id}, function(result) {
                $scope.paypallAccount = result;
                $('#savePaypallAccountModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.paypallAccount.id != null) {
                PaypallAccount.update($scope.paypallAccount,
                    function () {
                        $scope.refresh();
                    });
            } else {
                PaypallAccount.save($scope.paypallAccount,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            PaypallAccount.get({id: id}, function(result) {
                $scope.paypallAccount = result;
                $('#deletePaypallAccountConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PaypallAccount.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePaypallAccountConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#savePaypallAccountModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.paypallAccount = {email: null, isDefault: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
