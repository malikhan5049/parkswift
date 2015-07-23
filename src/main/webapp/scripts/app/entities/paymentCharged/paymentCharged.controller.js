'use strict';

angular.module('parkswiftApp')
    .controller('PaymentChargedController', function ($scope, PaymentCharged, CustomerBooking, ParseLinks) {
        $scope.paymentChargeds = [];
        $scope.customerbookings = CustomerBooking.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            PaymentCharged.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.paymentChargeds.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.paymentChargeds = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            PaymentCharged.get({id: id}, function(result) {
                $scope.paymentCharged = result;
                $('#savePaymentChargedModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.paymentCharged.id != null) {
                PaymentCharged.update($scope.paymentCharged,
                    function () {
                        $scope.refresh();
                    });
            } else {
                PaymentCharged.save($scope.paymentCharged,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            PaymentCharged.get({id: id}, function(result) {
                $scope.paymentCharged = result;
                $('#deletePaymentChargedConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PaymentCharged.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deletePaymentChargedConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#savePaymentChargedModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.paymentCharged = {amountCharged: null, transactionDateTime: null, status: null, paypallPaymentResponse: null, paymentReferenceNumber: null, transferToOwnerAccountDateTime: null, transferToParkSwiftAccountDateTime: null, amountToTransferOwnerAccount: null, amountToTransferParkSwiftAccount: null, todel: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
