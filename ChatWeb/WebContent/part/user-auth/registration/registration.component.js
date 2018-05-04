'use strict';

angular.module('userAuth.registration')
	.component('myRegistration', {
		templateUrl: 'part/user-auth/registration/registration.template.html',
		controller: function(wsService) {
			this.send = () => {
				wsService.register(this.user);
			};
		}
	});
