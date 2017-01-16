(function() {
  'use strict';

  angular
    .module('app.groupSelection')
    .directive('userInfo', userInfo);

  function userInfo() {
    return {
      templateUrl: 'app/groupSelection/directives/userInfo.html',
      restrict: 'E',
      controller: UserInfoController,
      controllerAs: 'vm',
      bindToController: true,
      scope: {
        groups: '='
      }
    };
  }

  UserInfoController.$inject = ['authService','groupSelectionService', 'userInfoService'];

  function UserInfoController(authService,groupSelectionService,userInfoService) {
      var vm = this;
      if (authService.firebaseAuthObject.$getAuth() != null) {
          vm.user = authService.firebaseAuthObject.$getAuth().providerData[0].uid;
          vm.userId = authService.firebaseAuthObject.$getAuth().uid;
          vm.userData = groupSelectionService.getUserInformationWeek(vm.userId);
          vm.userDataGroup = groupSelectionService.getUserInformation(vm.userId);
      } else {
          console.log('authentification did not work');
      }

      vm.info = userInfoService.getUserInformation(vm.userId);
      vm.group = userInfoService.getGroupForUser(vm.userId);
      vm.group.$loaded().then(function() {
          if(vm.group != null){
              vm.groupName = vm.group.$getRecord('groupNumber').$value;
          } else {
              console.log(vm.group + "is null :(");
          }
      });
  }

})();