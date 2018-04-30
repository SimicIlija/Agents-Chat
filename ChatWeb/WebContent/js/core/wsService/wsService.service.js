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
					   $rootScope.$broadcast('latestChats',['eee','ee','e'] );
				   }
				   else{ 
					   this.payload = JSON.parse(message.data);
					   if(this.payload.username != undefined){
						   $rootScope.user = this.payload;
						   $state.go('home.chat');
					   }else if(this.payload.chats != undefined){
						   $rootScope.$broadcast('latestChats',this.payload.chats );
						   
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
		   
		   
	   this.login = (user) => {
		   this.message ={
			"type" : "LOGIN",
			"content" : JSON.stringify(user)
		   };
		   
			if(this.socket != null){
				this.socket.send(JSON.stringify(this.message));
			}
		};
		
		
		 this.getLatestChat = () => {

			 this.message ={
						"type" : "LAST_CHATS",
						"content" : "5"
					   };
				if(this.socket != null){
					this.socket.send(JSON.stringify(this.message));
				}
			};
		   
		 this.sendMessage = (currentChat,content) => {

			 	this.date = new Date();
			 	this.currentTimeStamp = this.date.getTime()/1000;
				if(this.socket != null){
					
					this.contentMessage ={
							chat: currentChat,
							content: content,
							sender : "",
							timeStamp: this.currentTimeStamp
					};
					 this.message ={
								"type" : "MESSAGE",
								"content" : JSON.stringify(this.contentMessage)
					};
					 
					this.socket.send(JSON.stringify(this.message));
				}
			};
	});