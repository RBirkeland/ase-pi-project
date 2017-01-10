(function() {
  'use strict';

  angular
    .module('app.groupSelection')
    .directive('groupChooserTable', groupChooserTable);

  function groupChooserTable() {
    return {
      templateUrl: 'app/groupSelection/directives/groupTable.html',
      restrict: 'E',
      controller: GroupTableController,
      controllerAs: 'vm',
      bindToController: true,
      scope: {
        groups: '='
      }
    };
  }

  GroupTableController.$inject = ['authService','groupSelectionService'];

  function GroupTableController(authService,groupSelectionService) {
    var vm = this;
      if(authService.firebaseAuthObject.$getAuth() != null){
          vm.user = authService.firebaseAuthObject.$getAuth().providerData[0].uid;
      } else {
          return;
      }

    vm.joinGroup = joinGroup;
    vm.joinNewGroup = new groupSelectionService.JoinGroup(vm.user);

    console.log("vmUser:" + vm.user);

    function joinGroup(group) {
      console.log("joiningGroup: " + group + " (user): "+ vm.user);
      console.log(group);
      //vm.usersForGroups = groupSelectionService.getUsersForGroup(group.$id);
      var userFound  = false;
      for(var student in group.students){
        console.log(group.students[student].email);
        if(group.students[student].email == vm.user){
          userFound = true;
          console.log("userFound");
        }
      }
      if(!userFound){
          console.log("userNotFound therefore add it to the group");
          vm.group = groupSelectionService.getGroup(group.$id);
          vm.group.$add(vm.joinNewGroup);
          vm.JoinNewGroup = new groupSelectionService.JoinGroup(vm.user);
      }
    }
  }

})();