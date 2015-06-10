'use strict';

angular.module('parkswiftApp')
    .factory('ParkingSpaceVehicleType', function ($resource, DateUtils) {
        return $resource('api/parkingSpaceVehicleTypes/:id', {}, {
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
