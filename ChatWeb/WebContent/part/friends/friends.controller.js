(function() {
    'use strict';
  
    angular
      .module('friends')
      .controller('friendsController', friendsController);
  
    function friendsController() {
      var friendsVm = this;
      friendsVm.message = 'Test';
      friendsVm.myFriends = []; 
      friendsVm.searchResults = [];
      friendsVm.init = init;
      friendsVm.search = search;

      function init(){
        console.log("TODO: get with WS");
        var user1 = new Object();
        user1.name = "Sima";
        friendsVm.myFriends.push(user1);
        var user2 = new Object();
        user2.name = "Horva";
        friendsVm.myFriends.push(user2);
        var user3 = new Object();
        user3.name = "Sima";
        friendsVm.myFriends.push(user3);
      }

      function search(){
        console.log("TODO: get with WS");
      }
    }
  
  })();