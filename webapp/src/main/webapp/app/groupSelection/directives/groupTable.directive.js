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
          vm.userData = groupSelectionService.getUserInformationWeek(vm.userId);
          vm.userDataGroup = groupSelectionService.getUserInformation(vm.userId);
         console.log(vm.userId);
      } else {
          return;
      }

    vm.joinGroup = joinGroup;
    vm.joinAlert = joinAlert;
    vm.joinNewGroup = new groupSelectionService.JoinGroup(vm.user);
    //vm.joinNewGroupUser = new groupSelectionService.JoinGroupStudentInformation();
    vm.userInformation = groupSelectionService.getUserInformation(vm.userId);
    vm.joinNewGroupUserGroup = new groupSelectionService.JoinGroupStudentInformationWeek(0);
    for(var i = 1; i < 13; i++){
        vm.joinNewGroupUserGroup[i] = new groupSelectionService.JoinGroupStudentInformationWeek(i);
    }

    console.log("vmUser:" + vm.user);
    console.log(vm.userInformation);
    console.log(vm.userInformation.$getRecord('groupAssigned'));

    function joinAlert(group)
    {
        bootbox.confirm({
                message: "Do you really want to join: " + group.name + "?",
                buttons: {
                    confirm: {
                        label: 'Yes',
                        className: 'btn-success'
                    },
                    cancel: {
                        label: 'No',
                        className: 'btn-danger'
                    }
                },
                callback: function (result) {
                    console.log('This was logged in the callback: ' + result);

                    if(result == true){
                        vm.joinGroup(group);
                    }
                }
            });
    }

    function joinGroup(group) {
      console.log("joiningGroup: " + group + " (user): "+ vm.user);
      console.log(group);
      vm.usersForGroups = groupSelectionService.getUsersForGroup(group.$id);
      // TODO check if groupSelectionService.isUserInAnyGroup(uid);
      console.log(group.name);

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
          console.log(group.name);
          vm.userDataGroup.$add(new groupSelectionService.JoinGroupStudentInformation(group.name));
          for(var i = 1; i < 13; i++){
              vm.userData.$add(vm.joinNewGroupUserGroup[i]);
          }
      }
    }
  }

})();