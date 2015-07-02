'use strict';

angular.module('parkswiftApp')
    .factory('Payment', function ($resource, DateUtils) {
        return $resource('api/payments/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.transactionDateTime = DateUtils.convertDateTimeFromServer(data.transactionDateTime);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
