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
    vm.isSignedToGroup = isSignedToGroup;
    vm.joinNewGroup = new groupSelectionService.JoinGroup(vm.user);
    //vm.joinNewGroupUser = new groupSelectionService.JoinGroupStudentInformation();
    vm.userInformation = groupSelectionService.getUserInformation(vm.userId);
    vm.userGroupStatus = groupSelectionService.getUserGroupStatus(vm.userId);
    //vm.userInformationGroup = vm.userInformation.$getRecord('groupAssigned').$value;
    vm.joinNewGroupUserGroup = new groupSelectionService.JoinGroupStudentInformationWeek(0);
    for(var i = 1; i < 13; i++){
        vm.joinNewGroupUserGroup[i] = new groupSelectionService.JoinGroupStudentInformationWeek(i);
    }

    console.log("vmUser:" + vm.user);
    console.log(vm.userInformation);

    function isSignedToGroup(){
        if(vm.userGroupStatus.$getRecord('groupAssigned') == null || vm.userGroupStatus.$getRecord('groupAssigned').$value == false){
            return false;
        } else {
            return true;
        }
    }

    function joinAlert(group)
    {
        if(vm.userGroupStatus.$getRecord('groupAssigned') == null || vm.userGroupStatus.$getRecord('groupAssigned').$value == false){
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
                    console.log('The result of the confirm box was: ' + result);

                    if(result == true){
                        vm.joinGroup(group);
                    }
                }
            });
        } else {
            bootbox.alert({
                message: "You are already signed to a group!",
                callback: function () {
                    console.log('Already in group box');
                }
            });
        }
    }

    function joinGroup(group) {
      console.log("joiningGroup: " + group + " (user): "+ vm.user);
      console.log(group);
      vm.usersForGroups = groupSelectionService.getUsersForGroup(group.$id);
      // TODO check if groupSelectionService.isUserInAnyGroup(uid);
      console.log(group.name);
      console.log('UserInformation');
      console.log(vm.userInformation);
      console.log('UserGroupStatus');
      console.log(vm.userGroupStatus);
      if(vm.userGroupStatus.$getRecord('groupAssigned') == null || !vm.userGroupStatus.$getRecord('groupAssigned').$value){
          console.log("userNotFound therefore add it to the group");

          vm.group = groupSelectionService.getGroup(group.$id);
          vm.group.$add(vm.joinNewGroup);
          console.log(group.name);
          //vm.userDataGroup.$add(new groupSelectionService.JoinGroupStudentInformation(group.name));
          var groupStatusRef = firebase.database().ref('user/'+vm.userId+'/groupStatus');
          groupStatusRef.set(new groupSelectionService.JoinGroupStudentInformation(group.name))
              .then(function() {
                  console.log('Synchronization succeeded');
              })
              .catch(function(error) {
                  console.log('Synchronization failed');
              });

          for(var i = 1; i < 13; i++){
              var weekRef = firebase.database().ref('user/'+vm.userId+'/week/'+i);
              weekRef.set(new groupSelectionService.JoinGroupStudentInformationWeek())
                  .then(function() {
                      console.log('Synchronization succeeded => week '+i);
                  })
                  .catch(function(error) {
                      console.log('Synchronization failed');
                  });
              // vm.userData.$add(vm.joinNewGroupUserGroup[i]);
          }
        // TODO notify the user
      }
    }
  }

})();