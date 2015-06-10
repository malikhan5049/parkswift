'use strict';

angular.module('parkswiftApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('paypallAccount', {
                parent: 'entity',
                url: '/paypallAccount',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.paypallAccount.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/paypallAccount/paypallAccounts.html',
                        controller: 'PaypallAccountController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('paypallAccount');
                        return $translate.refresh();
                    }]
                }
            })
            .state('paypallAccountDetail', {
                parent: 'entity',
                url: '/paypallAccount/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.paypallAccount.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/paypallAccount/paypallAccount-detail.html',
                        controller: 'PaypallAccountDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('paypallAccount');
                        return $translate.refresh();
                    }]
                }
            });
    });
