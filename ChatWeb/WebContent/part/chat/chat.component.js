'use strict';

angular.module('chat')
	.component('myChat', {
		templateUrl: 'part/chat/chat.template.html',
		controller: function( $rootScope, $state,wsService,$scope,$element) {
			
			wsService.getLatestChat();
			this.display = $element.find('textarea'); 
			$rootScope.chats = ['aaa','bbb','ccc'];
			
			$scope.$on('latestChats', function (event, arg) { 
				$rootScope.chats = arg;
				$scope.$apply();
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
				this.a = 3;
			}
			this.changeChat = (chat) =>
			{
				this.display.append(chat + "\n")
			}
		}
	});