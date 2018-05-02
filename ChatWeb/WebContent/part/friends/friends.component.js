'use strict';

angular.module('friends')
	.component('friendsComponent', {
		templateUrl: 'part/friends/friends.template.html',
        controller: 'friendsController',
        controllerAs: 'friendsVm'
	});