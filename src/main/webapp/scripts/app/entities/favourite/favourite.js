'use strict';

angular.module('parkswiftApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('favourite', {
                parent: 'entity',
                url: '/favourite',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.favourite.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/favourite/favourites.html',
                        controller: 'FavouriteController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('favourite');
                        return $translate.refresh();
                    }]
                }
            })
            .state('favouriteDetail', {
                parent: 'entity',
                url: '/favourite/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'parkswiftApp.favourite.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/favourite/favourite-detail.html',
                        controller: 'FavouriteDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('favourite');
                        return $translate.refresh();
                    }]
                }
            });
    });
