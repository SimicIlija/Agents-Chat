(function() {
    'use strict';
  
    angular
      .module('friends')
      .controller('friendsController', friendsController);
  
    function friendsController() {
      var friendsVm = this;
      friendsVm.message = 'Test';
      friendsVm.searchText = '';
      friendsVm.myFriends = []; 
      friendsVm.searchResults = [];
      friendsVm.init = init;
      friendsVm.search = search;
      friendsVm.removeFriend = removeFriend;
      

      init();

      function init(){
        console.log("TODO: get with WS");
        var user1 = new Object();
        user1.username = "Sima";
        friendsVm.myFriends.push(user1);
        var user2 = new Object();
        user2.username = "Horva";
        friendsVm.myFriends.push(user2);
        var user3 = new Object();
        user3.username = "Puzic";
        friendsVm.myFriends.push(user3);
      }

      function search(){
        alert("Search for: " + friendsVm.searchText);
      }

      function removeFriend(username){
        alert("Remove friend with username : " + username);
      }
    }
  
  })();