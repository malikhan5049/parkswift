'use strict';

angular.module('parkswiftApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('paymentCharged', {
                parent: 'entity',
                url: '/paymentCharged',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.paymentCharged.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/paymentCharged/paymentChargeds.html',
                        controller: 'PaymentChargedController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('paymentCharged');
                        return $translate.refresh();
                    }]
                }
            })
            .state('paymentChargedDetail', {
                parent: 'entity',
                url: '/paymentCharged/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.paymentCharged.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/paymentCharged/paymentCharged-detail.html',
                        controller: 'PaymentChargedDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('paymentCharged');
                        return $translate.refresh();
                    }]
                }
            });
    });
