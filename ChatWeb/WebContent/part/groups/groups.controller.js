(function () {
    'use strict';

    angular
        .module('groups')
        .controller('groupsController', groupsController);

	    function groupsController() {
	    	
	    	var groupVm = this;
	    	groupVm.usernames = "";
	    	groupVm.name = "";
	    	groupVm.send = send;
	    	
	    	function send() {
		    	alert("send"+" "+groupVm.usernames);
//				wsService.register(this.user);
			};
	    	
	    }   
	    
})();