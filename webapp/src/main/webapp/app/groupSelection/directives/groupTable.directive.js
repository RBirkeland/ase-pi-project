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
          vm.userId = authService.firebaseAuthObject.$getAuth().uid;
          vm.userData = groupSelectionService.getUserInformation(vm.userId);
         console.log(vm.userId);
      } else {
          return;
      }

    vm.joinGroup = joinGroup;
    vm.joinNewGroup = new groupSelectionService.JoinGroup(vm.user);
    vm.joinNewGroupUser = new groupSelectionService.JoinGroupStudentInformation(0);
    for(var i = 1; i < 13; i++){
        vm.joinNewGroupUser[i] = new groupSelectionService.JoinGroupStudentInformation(i);
    }

    console.log("vmUser:" + vm.user);

    function joinGroup(group) {
      console.log("joiningGroup: " + group + " (user): "+ vm.user);
      console.log(group);
      vm.usersForGroups = groupSelectionService.getUsersForGroup(group.$id);

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

          for(var i = 1; i < 13; i++){
              vm.userData.$add(vm.joinNewGroupUser[i]);
          }
      }
    }
  }

})();