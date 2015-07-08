'use strict';

angular.module('parkswiftApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('feedback', {
                parent: 'entity',
                url: '/feedback',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.feedback.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/feedback/feedbacks.html',
                        controller: 'FeedbackController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('feedback');
                        return $translate.refresh();
                    }]
                }
            })
            .state('feedbackDetail', {
                parent: 'entity',
                url: '/feedback/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.feedback.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/feedback/feedback-detail.html',
                        controller: 'FeedbackDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('feedback');
                        return $translate.refresh();
                    }]
                }
            });
    });
