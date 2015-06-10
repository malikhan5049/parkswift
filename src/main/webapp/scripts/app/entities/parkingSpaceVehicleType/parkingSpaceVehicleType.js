'use strict';

angular.module('parkswiftApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('parkingSpaceVehicleType', {
                parent: 'entity',
                url: '/parkingSpaceVehicleType',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.parkingSpaceVehicleType.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/parkingSpaceVehicleType/parkingSpaceVehicleTypes.html',
                        controller: 'ParkingSpaceVehicleTypeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('parkingSpaceVehicleType');
                        return $translate.refresh();
                    }]
                }
            })
            .state('parkingSpaceVehicleTypeDetail', {
                parent: 'entity',
                url: '/parkingSpaceVehicleType/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.parkingSpaceVehicleType.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/parkingSpaceVehicleType/parkingSpaceVehicleType-detail.html',
                        controller: 'ParkingSpaceVehicleTypeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('parkingSpaceVehicleType');
                        return $translate.refresh();
                    }]
                }
            });
    });
