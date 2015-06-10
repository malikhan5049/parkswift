'use strict';

angular.module('parkswiftApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('availableParkingRepeatOn', {
                parent: 'entity',
                url: '/availableParkingRepeatOn',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.availableParkingRepeatOn.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/availableParkingRepeatOn/availableParkingRepeatOns.html',
                        controller: 'AvailableParkingRepeatOnController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('availableParkingRepeatOn');
                        return $translate.refresh();
                    }]
                }
            })
            .state('availableParkingRepeatOnDetail', {
                parent: 'entity',
                url: '/availableParkingRepeatOn/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.availableParkingRepeatOn.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/availableParkingRepeatOn/availableParkingRepeatOn-detail.html',
                        controller: 'AvailableParkingRepeatOnDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('availableParkingRepeatOn');
                        return $translate.refresh();
                    }]
                }
            });
    });
