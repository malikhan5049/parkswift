'use strict';

angular.module('parkswiftApp')
    .factory('ParkingLocation', function ($resource, DateUtils) {
        return $resource('api/parkingLocations/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.createdAt = DateUtils.convertDateTimeFromServer(data.createdAt);
                    data.modifiedAt = DateUtils.convertDateTimeFromServer(data.modifiedAt);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
