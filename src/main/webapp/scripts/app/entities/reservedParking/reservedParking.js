'use strict';

angular.module('parkswiftApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('reservedParking', {
                parent: 'entity',
                url: '/reservedParking',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.reservedParking.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/reservedParking/reservedParkings.html',
                        controller: 'ReservedParkingController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('reservedParking');
                        return $translate.refresh();
                    }]
                }
            })
            .state('reservedParkingDetail', {
                parent: 'entity',
                url: '/reservedParking/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.reservedParking.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/reservedParking/reservedParking-detail.html',
                        controller: 'ReservedParkingDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('reservedParking');
                        return $translate.refresh();
                    }]
                }
            });
    });
