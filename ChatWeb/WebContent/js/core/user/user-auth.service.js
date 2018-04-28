'use strict';

angular.module('core.user')
	.service('UserAuthService', function($http) {
		this.register = (data) => {
			return $http.post('/UserWeb/rest/User/register', data);
		};
		this.logIn = (data) => {
			return $http.put('/ChatWeb/rest/User/login', data);
		};
		this.logOut = () => {
			return $http.delete('/ChatWeb/rest/User/logout');
		};
		this.getUser = () => {
			return $http.get('/ChatWeb/rest/User/getUser');
		};
		this.editUser = (data) => {
			return $http.put('/ChatWeb/rest/User/editUser', data);
		};
	});
