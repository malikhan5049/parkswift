'use strict';

angular.module('parkswiftApp')
    .factory('PaymentCharged', function ($resource, DateUtils) {
        return $resource('api/paymentChargeds/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.transactionDateTime = DateUtils.convertDateTimeFromServer(data.transactionDateTime);
                    data.transferToOwnerAccountDateTime = DateUtils.convertDateTimeFromServer(data.transferToOwnerAccountDateTime);
                    data.transferToParkSwiftAccountDateTime = DateUtils.convertDateTimeFromServer(data.transferToParkSwiftAccountDateTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
