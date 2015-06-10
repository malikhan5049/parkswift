'use strict';

angular.module('parkswiftApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('payment', {
                parent: 'entity',
                url: '/payment',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.payment.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/payment/payments.html',
                        controller: 'PaymentController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('payment');
                        return $translate.refresh();
                    }]
                }
            })
            .state('paymentDetail', {
                parent: 'entity',
                url: '/payment/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.payment.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/payment/payment-detail.html',
                        controller: 'PaymentDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('payment');
                        return $translate.refresh();
                    }]
                }
            });
    });
