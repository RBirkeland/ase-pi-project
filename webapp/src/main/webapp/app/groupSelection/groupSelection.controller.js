(function() {
  'use strict';

  angular
    .module('app.groupSelection')
    .controller('GroupSelectionController', GroupSelectionController);

    GroupSelectionController.$inject = ['groupSelectionService', 'user'];

  function GroupSelectionController(groupSelectionService, user) {
    var vm = this;
    vm.user = user.providerData[0].uid;
    vm.groups  = groupSelectionService.getGroups();
  }

})();