'use strict';

angular.module('parkswiftApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('bookedParkingSpace', {
                parent: 'entity',
                url: '/bookedParkingSpace',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.bookedParkingSpace.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/bookedParkingSpace/bookedParkingSpaces.html',
                        controller: 'BookedParkingSpaceController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('bookedParkingSpace');
                        return $translate.refresh();
                    }]
                }
            })
            .state('bookedParkingSpaceDetail', {
                parent: 'entity',
                url: '/bookedParkingSpace/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.bookedParkingSpace.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/bookedParkingSpace/bookedParkingSpace-detail.html',
                        controller: 'BookedParkingSpaceDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('bookedParkingSpace');
                        return $translate.refresh();
                    }]
                }
            });
    });
