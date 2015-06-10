'use strict';

angular.module('parkswiftApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('parkingSpace', {
                parent: 'entity',
                url: '/parkingSpace',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.parkingSpace.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/parkingSpace/parkingSpaces.html',
                        controller: 'ParkingSpaceController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('parkingSpace');
                        return $translate.refresh();
                    }]
                }
            })
            .state('parkingSpaceDetail', {
                parent: 'entity',
                url: '/parkingSpace/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.parkingSpace.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/parkingSpace/parkingSpace-detail.html',
                        controller: 'ParkingSpaceDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('parkingSpace');
                        return $translate.refresh();
                    }]
                }
            });
    });
