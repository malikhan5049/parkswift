'use strict';

angular.module('parkswiftApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('lookupEntry', {
                parent: 'entity',
                url: '/lookupEntry',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.lookupEntry.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/lookupEntry/lookupEntrys.html',
                        controller: 'LookupEntryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('lookupEntry');
                        return $translate.refresh();
                    }]
                }
            })
            .state('lookupEntryDetail', {
                parent: 'entity',
                url: '/lookupEntry/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.lookupEntry.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/lookupEntry/lookupEntry-detail.html',
                        controller: 'LookupEntryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('lookupEntry');
                        return $translate.refresh();
                    }]
                }
            });
    });
