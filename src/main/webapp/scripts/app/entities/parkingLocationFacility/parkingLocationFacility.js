'use strict';

angular.module('parkswiftApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('parkingLocationFacility', {
                parent: 'entity',
                url: '/parkingLocationFacility',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.parkingLocationFacility.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/parkingLocationFacility/parkingLocationFacilitys.html',
                        controller: 'ParkingLocationFacilityController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('parkingLocationFacility');
                        return $translate.refresh();
                    }]
                }
            })
            .state('parkingLocationFacilityDetail', {
                parent: 'entity',
                url: '/parkingLocationFacility/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.parkingLocationFacility.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/parkingLocationFacility/parkingLocationFacility-detail.html',
                        controller: 'ParkingLocationFacilityDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('parkingLocationFacility');
                        return $translate.refresh();
                    }]
                }
            });
    });
