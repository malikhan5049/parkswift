'use strict';

angular.module('parkswiftApp')
    .factory('LookupHeader', function ($resource, DateUtils) {
        return $resource('api/lookupHeaders/:id', {}, {
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
