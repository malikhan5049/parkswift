'use strict';

angular.module('parkswiftApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('reservedParkingRepeatOn', {
                parent: 'entity',
                url: '/reservedParkingRepeatOn',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.reservedParkingRepeatOn.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/reservedParkingRepeatOn/reservedParkingRepeatOns.html',
                        controller: 'ReservedParkingRepeatOnController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('reservedParkingRepeatOn');
                        return $translate.refresh();
                    }]
                }
            })
            .state('reservedParkingRepeatOnDetail', {
                parent: 'entity',
                url: '/reservedParkingRepeatOn/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.reservedParkingRepeatOn.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/reservedParkingRepeatOn/reservedParkingRepeatOn-detail.html',
                        controller: 'ReservedParkingRepeatOnDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('reservedParkingRepeatOn');
                        return $translate.refresh();
                    }]
                }
            });
    });
