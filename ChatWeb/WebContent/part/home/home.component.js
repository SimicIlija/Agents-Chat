'use strict';

angular.module('home')
	.component('myHome', {
		templateUrl: 'part/home/home.template.html',
		controller: function ($stateParams,$window,$rootScope){
			var url = $window.location;
			var host = "ws://" + url.hostname + ":" + url.port + "/ChatWeb/Socket";
			
			try {
				   this.socket = new WebSocket(host);
				   $rootScope.socket = this.socket;
				   this.socket.onopen = function() {
					   console.log("Socket connection opened");
				   }
				   
				   this.socket.onmessage = function(message) {
//					   if(message.data === 'INVALID_CREDENTIALS') {
//						   alert('Wrong username or password!');
//					   } else if(message.data === 'ALREADY_LOGGED') {
//						   alert('You are already logged on');
//					   } else {
//						   var payload = JSON.parse(message.data);
//						   sessionStorage.setItem('loggedUser', JSON.stringify(payload.user));
//						   $rootScope.$apply(function() {
//							   $location.path('/messaging');
//						   });
//					   }
					   alert(message.data);
				   }
				   
				   socket.onclose = function() {
					   this.socket = null;
					   console.log("socket connection closed");
				   }
			   } catch(exception) {
				   console.log("Error!");
			   }
		}
	});
