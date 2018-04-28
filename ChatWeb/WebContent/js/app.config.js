'use strict';

angular.module('agent-chat')
	.config(function($stateProvider, $urlRouterProvider) {
		$stateProvider
			.state({
				name: 'home',
				url: '/',
				component: 'myHome'
			})
//			.state({
//				name: 'home.chart',
//				url: '^/chart/:id?monthTime',
//				component: 'myChart'
//			})
//			.state({
//				name: 'home.places',
//				url: '^/places/{placeType:theater|cinema}',
//				component: 'myPlaceList'
//			})
//			
//			.state({
//				name: 'home.projectionForm',
//				url: '^/projectionForm/:idPlace?idProjection',
//				component: 'myProjectionForm'
//			})

		$urlRouterProvider
			.when('', '/')
			.otherwise('/error');
	})
//	.run(function($rootScope, UserAuthService) {
//		UserAuthService.getUser().then(
//			(response) => {
//				$rootScope.user = response.data;
//			},
//			() => {
//				$rootScope.user = null;
//			});
//	});
