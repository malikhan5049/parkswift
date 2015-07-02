'use strict';

angular.module('parkswiftApp')
    .controller('PaymentController', function ($scope, Payment, User, ReservedParking, ParseLinks) {
        $scope.payments = [];
        $scope.users = User.query();
        $scope.reservedparkings = ReservedParking.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Payment.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.payments.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.payments = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Payment.get({id: id}, function(result) {
                $scope.payment = result;
                $('#savePaymentModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.payment.id != null) {
                Payment.update($scope.payment,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Payment.save($scope.payment,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Payment.get({id: id}, function(result) {
                $scope.payment = result;
                $('#deletePaymentConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Payment.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deletePaymentConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#savePaymentModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.payment = {amountPaid: null, transactionDateTime: null, status: null, paypallPaymentResponse: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
