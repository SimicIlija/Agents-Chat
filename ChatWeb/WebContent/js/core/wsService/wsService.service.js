'use strict';

angular.module('core.wsService')
	.service('wsService', function($http,$window,$rootScope,$state) {
		
		var url = $window.location;
		var host = "ws://" + url.hostname + ":" + url.port + "/ChatWeb/Socket";
		
		try {
			   this.socket = new WebSocket(host);
			  // $rootScope.socket = this.socket;
			   
			   this.socket.onopen = function() {
				   console.log("Socket connection opened");
			   }
			   
			   this.socket.onmessage = function(message) {
				   if(message.data == "LC"){
					   $rootScope.$broadcast('latestChats',['eee','eee','eee'] );
				   }
				   else{ 
					   this.payload = JSON.parse(message.data);
					   if(this.payload.username != undefined){
						   $rootScope.user = this.payload;
						   $state.go('home.chat');
					   }
				   }
				   alert(message.data);
				   
			   }
			   
			   socket.onclose = function() {
				   this.socket = null;
				   console.log("socket connection closed");
			   }
		   } catch(exception) {
			   console.log("Error!");
		   }
	   this.login = (data) => {
		   data.host = {};
		   // TODO dodati host name iz fajla koji se izvlaci
		   data.host.name = "iz fajla";
		   data.host.address = url.hostname + ":" + url.port;
			if(this.socket != null){
				this.socket.send(JSON.stringify(data));
			}
		};
		
		 this.getLatestChat = () => {

				if(this.socket != null){
					this.socket.send("getLatestChat");
				}
			};
		   
	});