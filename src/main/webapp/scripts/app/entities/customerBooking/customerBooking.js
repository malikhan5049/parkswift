'use strict';

angular.module('parkswiftApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('customerBooking', {
                parent: 'entity',
                url: '/customerBooking',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.customerBooking.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/customerBooking/customerBookings.html',
                        controller: 'CustomerBookingController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('customerBooking');
                        return $translate.refresh();
                    }]
                }
            })
            .state('customerBookingDetail', {
                parent: 'entity',
                url: '/customerBooking/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.customerBooking.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/customerBooking/customerBooking-detail.html',
                        controller: 'CustomerBookingDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('customerBooking');
                        return $translate.refresh();
                    }]
                }
            });
    });
