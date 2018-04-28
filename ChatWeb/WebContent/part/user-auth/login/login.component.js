'use strict';

angular.module('userAuth.login')
	.component('myLogin', {
		templateUrl: 'part/user-auth/login/login.template.html',
		controller: function(wsService, $rootScope, $state) {
			this.send = () => {
				wsService.login(this.user);
			
			}
		}
	});
