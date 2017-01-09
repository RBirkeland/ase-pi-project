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
        parties: '='
      }
    };
  }

  GroupTableController.$inject = ['textMessageService'];

  function GroupTableController(textMessageService) {
    var vm = this;

    vm.removeParty = removeParty;
    vm.sendTextMessage = sendTextMessage;
    vm.toggleDone = toggleDone;

    function removeParty(party) {
      vm.parties.$remove(party);
    }

    function sendTextMessage(party) {
      textMessageService.sendTextMessage(party, vm.parties);
    }

    function toggleDone(party) {
      vm.parties.$save(party);
    }
  }

})();