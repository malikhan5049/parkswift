'use strict';

angular.module('parkswiftApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('parkingLocationContactInfo', {
                parent: 'entity',
                url: '/parkingLocationContactInfo',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.parkingLocationContactInfo.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/parkingLocationContactInfo/parkingLocationContactInfos.html',
                        controller: 'ParkingLocationContactInfoController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('parkingLocationContactInfo');
                        return $translate.refresh();
                    }]
                }
            })
            .state('parkingLocationContactInfoDetail', {
                parent: 'entity',
                url: '/parkingLocationContactInfo/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.parkingLocationContactInfo.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/parkingLocationContactInfo/parkingLocationContactInfo-detail.html',
                        controller: 'ParkingLocationContactInfoDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('parkingLocationContactInfo');
                        return $translate.refresh();
                    }]
                }
            });
    });
