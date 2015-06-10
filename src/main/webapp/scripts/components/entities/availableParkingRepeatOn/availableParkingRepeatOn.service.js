'use strict';

angular.module('parkswiftApp')
    .factory('AvailableParkingRepeatOn', function ($resource, DateUtils) {
        return $resource('api/availableParkingRepeatOns/:id', {}, {
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
