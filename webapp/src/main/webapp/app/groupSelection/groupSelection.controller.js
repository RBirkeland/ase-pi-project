(function() {
  'use strict';

  angular
    .module('app.groupSelection')
    .controller('GroupSelectionController', GroupSelectionController);

    GroupSelectionController.$inject = ['partyService', 'user'];

  function GroupSelectionController(partyService, user) {
    var vm = this;
    vm.user = user.providerData[0].uid;
    vm.parties  = partyService.getPartiesByUser(user.uid);
  }

})();