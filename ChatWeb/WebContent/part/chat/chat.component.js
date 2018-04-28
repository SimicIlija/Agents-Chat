'use strict';

angular.module('chat')
	.component('myChat', {
		templateUrl: 'part/chat/chat.template.html',
		controller: function( $rootScope, $state) {
			
//			this.logOut = () => {
//				UserAuthService.logOut().then( () => {
//					$rootScope.user = null;
//					$state.go('home');
//				}, (response) => {
//					this.status = response.status;
//					if(this.status == 409) {
//						alert('Please change your password!');
//						$state.go('profile')
//					}
//				});
//			};
		}
	});