'use strict';

angular.module('parkswiftApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('availableParking', {
                parent: 'entity',
                url: '/availableParking',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.availableParking.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/availableParking/availableParkings.html',
                        controller: 'AvailableParkingController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('availableParking');
                        return $translate.refresh();
                    }]
                }
            })
            .state('availableParkingDetail', {
                parent: 'entity',
                url: '/availableParking/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.availableParking.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/availableParking/availableParking-detail.html',
                        controller: 'AvailableParkingDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('availableParking');
                        return $translate.refresh();
                    }]
                }
            });
    });
