'use strict';

angular.module('parkswiftApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


