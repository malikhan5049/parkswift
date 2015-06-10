'use strict';

angular.module('parkswiftApp')
    .factory('ReservedParking', function ($resource, DateUtils) {
        return $resource('api/reservedParkings/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.dateStart = DateUtils.convertLocaleDateFromServer(data.dateStart);
                    data.dateEnd = DateUtils.convertLocaleDateFromServer(data.dateEnd);
                    data.timeStart = DateUtils.convertDateTimeFromServer(data.timeStart);
                    data.timeEnd = DateUtils.convertDateTimeFromServer(data.timeEnd);
                    data.reservedOn = DateUtils.convertDateTimeFromServer(data.reservedOn);
                    data.createdAt = DateUtils.convertDateTimeFromServer(data.createdAt);
                    data.modifiedAt = DateUtils.convertDateTimeFromServer(data.modifiedAt);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.dateStart = DateUtils.convertLocaleDateToServer(data.dateStart);
                    data.dateEnd = DateUtils.convertLocaleDateToServer(data.dateEnd);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.dateStart = DateUtils.convertLocaleDateToServer(data.dateStart);
                    data.dateEnd = DateUtils.convertLocaleDateToServer(data.dateEnd);
                    return angular.toJson(data);
                }
            }
        });
    });
