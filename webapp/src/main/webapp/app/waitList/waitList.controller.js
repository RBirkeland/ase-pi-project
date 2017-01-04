(function() {
  'use strict';

  angular
    .module('app.waitList')
    .controller('WaitListController', WaitListController);

  WaitListController.$inject = ['partyService', 'user'];

  function WaitListController(partyService, user) {
    var vm = this;
    vm.user = user.providerData[0].uid;
    vm.parties  = partyService.getPartiesByUser(user.uid);
  }

})();