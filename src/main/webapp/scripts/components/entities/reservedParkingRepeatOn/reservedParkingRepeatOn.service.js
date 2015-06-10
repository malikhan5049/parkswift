'use strict';

angular.module('parkswiftApp')
    .factory('ReservedParkingRepeatOn', function ($resource, DateUtils) {
        return $resource('api/reservedParkingRepeatOns/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
