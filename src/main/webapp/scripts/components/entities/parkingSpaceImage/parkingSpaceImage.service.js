'use strict';

angular.module('parkswiftApp')
    .factory('ParkingSpaceImage', function ($resource, DateUtils) {
        return $resource('api/parkingSpaceImages/:id', {}, {
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
