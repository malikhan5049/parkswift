'use strict';

angular.module('parkswiftApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('parkingSpaceImage', {
                parent: 'entity',
                url: '/parkingSpaceImage',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.parkingSpaceImage.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/parkingSpaceImage/parkingSpaceImages.html',
                        controller: 'ParkingSpaceImageController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('parkingSpaceImage');
                        return $translate.refresh();
                    }]
                }
            })
            .state('parkingSpaceImageDetail', {
                parent: 'entity',
                url: '/parkingSpaceImage/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.parkingSpaceImage.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/parkingSpaceImage/parkingSpaceImage-detail.html',
                        controller: 'ParkingSpaceImageDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('parkingSpaceImage');
                        return $translate.refresh();
                    }]
                }
            });
    });
