(function () {
    'use strict';

    angular
        .module('groups')
        .controller('groupsController', groupsController);
    
    groupsController.$inject = ['wsService', '$rootScope', '$state', '$scope'];
    function groupsController(wsService, $rootScope, $state, $scope) {
        var groupsVm = this;
        groupsVm.name = "";
        groupsVm.usernames = "";
        groupsVm.init = init;
        groupsVm.send = send;


        init();
        function init() {
        	if(!$rootScope.user)
        		$state.go('home');
        	
        	$scope.$on('GROUP_CHAT_RES', function (event, msg) { 
				if(msg.type == 'CREATED') {
					$state.go('home.chat');
				} else if(msg.type == 'ERROR') {
					alert('ERROR');
				}
    	  });
        }

        function send() {
        	var msg = {};
        	msg.chat = groupsVm.name;
        	msg.memebers = groupsVm.usernames;
        	msg.type = 'NEW_CHAT';
        	wsService.sendGroupChatReqMsg(msg);
        }
    }

})();