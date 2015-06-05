'use strict';

angular.module('parkswiftApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
