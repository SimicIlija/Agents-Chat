(function () {
    'use strict';

    angular
        .module('groups')
        .controller('groupsController', groupsController);

    function groupsController() {
        var groupsVm = this;
        groupsVm.message = 'Test';
        groupsVm.myGroups = [];
        groupsVm.showClicked = false;
        groupsVm.showGroup = null;
        groupsVm.searchText = '';
        groupsVm.init = init;
        groupsVm.leave = leave;
        groupsVm.showDetails = showDetails;
        groupsVm.remove = remove;
        groupsVm.search = search;


        init();
        function init() {
            var group1 = {};
            var group2 = {};
            group1.name = 'gr1';
            group2.name = 'gr2';
            group1.users = ["pera", "mika"];
            groupsVm.myGroups.push(group1);
            groupsVm.myGroups.push(group2);
        }

        function leave(groupName) {
            alert("napusti grupe: " + groupName);
        }

        function showDetails(group) {
            groupsVm.showClicked = true;
            groupsVm.showGroup = group;
        }

        function remove(group, user) {
            alert("remove " + user + " from " + group.name);
        }

        function search() {

        }
    }

})();