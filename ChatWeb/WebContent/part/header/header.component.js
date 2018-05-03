'use strict';

angular.module('header')
	.component('myHeader', {
		templateUrl: 'part/header/header.template.html',
		controller: function( $rootScope, $state, wsService) {
			
			this.logOut = () => {
				console.log("klik");
				wsService.logout();
			};
		}
	});
