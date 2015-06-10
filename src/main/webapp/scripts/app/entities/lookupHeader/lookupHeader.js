'use strict';

angular.module('parkswiftApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('lookupHeader', {
                parent: 'entity',
                url: '/lookupHeader',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.lookupHeader.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/lookupHeader/lookupHeaders.html',
                        controller: 'LookupHeaderController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('lookupHeader');
                        return $translate.refresh();
                    }]
                }
            })
            .state('lookupHeaderDetail', {
                parent: 'entity',
                url: '/lookupHeader/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.lookupHeader.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/lookupHeader/lookupHeader-detail.html',
                        controller: 'LookupHeaderDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('lookupHeader');
                        return $translate.refresh();
                    }]
                }
            });
    });
