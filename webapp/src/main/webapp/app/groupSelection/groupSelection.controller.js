(function() {
  'use strict';

  angular
    .module('app.groupSelection')
    .controller('GroupSelectionController', GroupSelectionController);

    GroupSelectionController.$inject = ['groupSelectionService', 'user'];

  function GroupSelectionController(groupSelectionService, user) {
    var vm = this;
    vm.user = user.providerData[0].uid;
    vm.userId = user.uid;
    vm.groups  = groupSelectionService.getGroups();
    vm.userGroupStatus = groupSelectionService.getUserGroupStatus(vm.userId);
    vm.isSignedToGroup = isSignedToGroup;

    function isSignedToGroup(){
        vm.userGroupStatus.$loaded().then(function () {
            if(vm.userGroupStatus.$getRecord('groupAssigned') == null || vm.userGroupStatus.$getRecord('groupAssigned').$value == false){
                return false;
            } else {
                return true;
            }
        });
    }

  }

})();