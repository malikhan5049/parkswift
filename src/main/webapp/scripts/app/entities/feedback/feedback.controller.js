'use strict';

angular.module('parkswiftApp')
    .controller('FeedbackController', function ($scope, Feedback, User, ParkingLocation, ParseLinks) {
        $scope.feedbacks = [];
        $scope.users = User.query();
        $scope.parkinglocations = ParkingLocation.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Feedback.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.feedbacks.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.feedbacks = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Feedback.get({id: id}, function(result) {
                $scope.feedback = result;
                $('#saveFeedbackModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.feedback.id != null) {
                Feedback.update($scope.feedback,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Feedback.save($scope.feedback,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Feedback.get({id: id}, function(result) {
                $scope.feedback = result;
                $('#deleteFeedbackConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Feedback.delete({id: id},
                function () {
                    $scope.reset();
                    $('#deleteFeedbackConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.reset();
            $('#saveFeedbackModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.feedback = {rating: null, comments: null, posted_on: null, feedback_by: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
