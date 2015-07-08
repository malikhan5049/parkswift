'use strict';

angular.module('parkswiftApp')
    .factory('Feedback', function ($resource, DateUtils) {
        return $resource('api/feedbacks/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.posted_on = DateUtils.convertDateTimeFromServer(data.posted_on);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
