'use strict';

angular.module('parkswiftApp')
    .controller('FeedbackDetailController', function ($scope, $stateParams, Feedback, User, ParkingLocation) {
        $scope.feedback = {};
        $scope.load = function (id) {
            Feedback.get({id: id}, function(result) {
              $scope.feedback = result;
            });
        };
        $scope.load($stateParams.id);
    });
