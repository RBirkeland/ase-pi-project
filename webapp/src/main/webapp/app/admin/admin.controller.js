(function() {
  'use strict';

  angular
    .module('app.admin')
    .controller('AdminController', AdminController);

    AdminController.$inject = ['groupService', 'user'];

  function AdminController(groupService, user) {
    var vm = this;
    vm.user = user.providerData[0].uid;
    vm.groups  = groupService.getGroups();
  }

})();