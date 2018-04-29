'use strict';

angular.module('userAuth.registration')
	.component('myRegistration', {
		templateUrl: 'part/user-auth/registration/registration.template.html',
		controller: function(UserAuthService) {
			this.send = () => {
				UserAuthService.register(this.user).then(
					() => {
						this.status = 'Registered successfully. Please confirm your email';
					},
					() => {
						this.status = 'Error';
					});
			};
		}
	});
