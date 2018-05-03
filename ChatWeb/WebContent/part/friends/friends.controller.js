(function() {
    'use strict';
  
    angular
      .module('friends')
      .controller('friendsController', friendsController);
    
    friendsController.$inject = ['wsService', '$rootScope','$state'];
    function friendsController(wsService, $rootScope, $state) {
      var friendsVm = this;
      friendsVm.message = 'Test';
      friendsVm.searchText = '';
      friendsVm.myFriends = [];
      friendsVm.friendReq = [];
      friendsVm.searchResults = [];
      friendsVm.init = init;
      friendsVm.search = search;
      friendsVm.removeFriend = removeFriend;
      friendsVm.sendReq = '';
      friendsVm.acceptReq = '';
      friendsVm.declineReq = '';

      init();

      function init(){
    	  if($rootScope.user == null) {
    	  	$state.go('home');
    	  	return;
    	  }
    	  friendsVm.myFriends = $rootScope.user.friends;
      }

      function search(){
    	var msg = {};
    	msg.search = friendsVm.searchText;
    	msg.type = 'SEARCH';
    	wsService.sendFriendReqMsg(msg);
        //alert("Search for: " + friendsVm.searchText);
      }

      function removeFriend(username){
        alert("Remove friend with username : " + username);
      }
    }
  
  })();