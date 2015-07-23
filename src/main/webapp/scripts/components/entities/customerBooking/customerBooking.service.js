'use strict';

angular.module('parkswiftApp')
    .factory('CustomerBooking', function ($resource, DateUtils) {
        return $resource('api/customerBookings/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.bookingDateTime = DateUtils.convertDateTimeFromServer(data.bookingDateTime);
                    data.nextPaymentDateTime = DateUtils.convertDateTimeFromServer(data.nextPaymentDateTime);
                    data.cancelationDateTime = DateUtils.convertDateTimeFromServer(data.cancelationDateTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
