'use strict';

angular.module('parkswiftApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('parkingSpacePriceEntry', {
                parent: 'entity',
                url: '/parkingSpacePriceEntry',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.parkingSpacePriceEntry.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/parkingSpacePriceEntry/parkingSpacePriceEntrys.html',
                        controller: 'ParkingSpacePriceEntryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('parkingSpacePriceEntry');
                        return $translate.refresh();
                    }]
                }
            })
            .state('parkingSpacePriceEntryDetail', {
                parent: 'entity',
                url: '/parkingSpacePriceEntry/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.parkingSpacePriceEntry.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/parkingSpacePriceEntry/parkingSpacePriceEntry-detail.html',
                        controller: 'ParkingSpacePriceEntryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('parkingSpacePriceEntry');
                        return $translate.refresh();
                    }]
                }
            });
    });
