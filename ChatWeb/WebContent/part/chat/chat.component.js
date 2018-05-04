'use strict';

angular.module('chat')
	.component('myChat', {
		templateUrl: 'part/chat/chat.template.html',
		controller: function( $rootScope, $state,wsService,$scope,$element) {
			if(!$rootScope.user)
				$state.go('home');
			
			this.currentChat = -1;
			
			wsService.getLatestChat();
			this.display = $element.find('textarea'); 
			$rootScope.chats = ['aaa','bbb','ccc'];
			
			$scope.$on('latestChats', function (event, arg) { 
				$rootScope.chats = arg.chats;
				$scope.$apply();
			  });
			$scope.$on('MESSAGE', function (event, arg) { 
				$element.find('textarea').val($element.find('textarea').val() +arg.sender+" : "+ arg.content + "\n");
				//$scope.$apply();
			  });
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
			this.send = () =>{
				wsService.sendMessage(this.currentChat,this.content);
				$element.find('textarea').val($element.find('textarea').val() +$rootScope.user.username+" : "+ this.content + "\n");
			}
			this.changeChat = (chat) =>
			{
				this.currentChat = chat.id;
				this.display.val("");
				chat.messages.forEach(function(element){
					$element.find('textarea').val($element.find('textarea').val() +element.sender+" : "+ element.content + "\n");
				});
			}
			
			this.getOther = (usernames) => {
				if(usernames.indexOf($rootScope.user.username) == 0)
					return usernames[1];
				else return usernames[0];
			}
			
			this.isAdnim = (data) => {
				if(data)
					return true;
				return false;
			}
		}
	});