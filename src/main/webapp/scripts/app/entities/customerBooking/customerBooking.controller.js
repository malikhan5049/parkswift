'use strict';

angular.module('parkswiftApp')
    .controller('CustomerBookingController', function ($scope, CustomerBooking, BookedParkingSpace, Payment, User, ReservedParking, ParseLinks) {
        $scope.customerBookings = [];
        $scope.bookedparkingspaces = BookedParkingSpace.query();
        $scope.payments = Payment.query();
        $scope.users = User.query();
        $scope.reservedparkings = ReservedParking.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            CustomerBooking.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.customerBookings.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.customerBookings = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            CustomerBooking.get({id: id}, function(result) {
                $scope.customerBooking = result;
                $('#saveCustomerBookingModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.customerBooking.id != null) {
                CustomerBooking.update($scope.customerBooking,
                    function () {
                        $scope.refresh();
                    });
            } else {
                CustomerBooking.save($scope.customerBooking,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            CustomerBooking.get({id: id}, function(result) {
                $scope.customerBooking = result;
                $('#deleteCustomerBookingConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            CustomerBooking.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteCustomerBookingConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#saveCustomerBookingModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.customerBooking = {bookingReferenceNumber: null, bookingDateTime: null, numberOfSpacesBooked: null, totalAmount: null, paymentRecursive: null, paymentFrequency: null, numberOfPayments: null, firstPaymentAmount: null, regularPaymentAmount: null, lastPaymentAmount: null, numberOfPaymentsMade: null, nextPaymentDateTime: null, vehicleMake: null, vehicleModel: null, modelYear: null, licencePlateNumber: null, status: null, cancelationDateTime: null, cancelledBy: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
