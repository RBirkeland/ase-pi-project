(function() {
  'use strict';

  angular
    .module('app.groupSelection')
    .controller('GroupSelectionController', GroupSelectionController);

    GroupSelectionController.$inject = ['groupService', 'user'];

  function GroupSelectionController(groupService, user) {
    var vm = this;
    vm.user = user.providerData[0].uid;
    vm.groups  = groupService.getGroups();
  }

})();