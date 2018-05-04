(function() {
    'use strict';
  
    angular
      .module('friends')
      .controller('friendsController', friendsController);
    
    friendsController.$inject = ['wsService', '$rootScope','$state','$scope'];
    function friendsController(wsService, $rootScope, $state, $scope) {
      var friendsVm = this;
      friendsVm.message = 'Test';
      friendsVm.user;
      friendsVm.searchText = '';
      friendsVm.myFriends = [];
      friendsVm.myRequests = [];
      friendsVm.searchResults = [];
      friendsVm.init = init;
      friendsVm.search = search;
      friendsVm.removeFriend = removeFriend;
      friendsVm.sendReq = sendReq;
      friendsVm.acceptReq = acceptReq;
      friendsVm.declineReq = declineReq;
      friendsVm.updateUser = updateUser;
      
      init();

      function init(){
    	  if($rootScope.user == null) {
    	  	$state.go('home');
    	  	return;
    	  }
    	  friendsVm.user = $rootScope.user;
    	  $scope.$on('USER_FRIENDS_RES', function (event, msg) { 
				if(msg.type == 'SEARCH') {
					friendsVm.searchResults = msg.user;
				} else if(msg.type == 'ADDED_FRIEND' || msg.type == 'REMOVED_FRIEND' || msg.type == 'DECLINED_REQUEST') {
					$rootScope.user = msg.user[0];
					friendsVm.user = msg.user[0];
				} else if(msg.type == 'ERROR') {
					alert('ERROR');
				} else if(msg.type == 'UPDATE') {
					$rootScope.user = msg.user[0];
					friendsVm.user = msg.user[0];
				}
				$scope.$apply();
    	  });
    	  var msg = {};
    	  msg.user = {};
    	  msg.user = $rootScope.user.id;
    	  msg.type = 'UPDATE';
    	  wsService.sendFriendReqMsg(msg);
      }
      
      function updateUser(){
    	  //TODO wsService.getUser(id);
      }

      function search(){
    	var msg = {};
    	msg.search = friendsVm.searchText;
    	msg.type = 'SEARCH';
    	wsService.sendFriendReqMsg(msg);
        //alert("Search for: " + friendsVm.searchText);
      }
      
      function sendReq(id){
          var msg = {};
          msg.user = $rootScope.user.id;
          msg.addRemove = id;
          msg.type = 'FRIEND_REQUEST'
          wsService.sendFriendReqMsg(msg);
        }
      
      function acceptReq(id){
          var msg = {};
          msg.user = $rootScope.user.id;
          msg.addRemove = id;
          msg.type = 'ADD_FRIEND'
          wsService.sendFriendReqMsg(msg);
        }
      
      function declineReq(id){
          var msg = {};
          msg.user = $rootScope.user.id;
          msg.addRemove = id;
          msg.type = 'FRIEND_REQUEST_DECL'
          wsService.sendFriendReqMsg(msg);
        }

      function removeFriend(id){
        var msg = {};
        msg.user = $rootScope.user.id;
        msg.addRemove = id;
        msg.type = 'REMOVE_FRIEND'
        wsService.sendFriendReqMsg(msg);
      }
    }
  
  })();