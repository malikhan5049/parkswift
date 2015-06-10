'use strict';

angular.module('parkswiftApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('parkingLocation', {
                parent: 'entity',
                url: '/parkingLocation',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.parkingLocation.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/parkingLocation/parkingLocations.html',
                        controller: 'ParkingLocationController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('parkingLocation');
                        return $translate.refresh();
                    }]
                }
            })
            .state('parkingLocationDetail', {
                parent: 'entity',
                url: '/parkingLocation/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.parkingLocation.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/parkingLocation/parkingLocation-detail.html',
                        controller: 'ParkingLocationDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('parkingLocation');
                        return $translate.refresh();
                    }]
                }
            });
    });
