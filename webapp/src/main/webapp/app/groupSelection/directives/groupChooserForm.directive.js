(function() {
  'use strict';

  angular
    .module('app.groupSelection')
    .directive('groupChooserForm', groupChooserForm);

  function groupChooserForm() {
    return {
      templateUrl: 'app/groupSelection/directives/groupChooserForm.html',
      restrict: 'E',
      controller: PartyFormController,
      controllerAs: 'vm',
      bindToController: true,
      scope: {
        parties: '='
      }
    };
  }

  GroupFormController.$inject = ['groupService'];

  function GroupFormController(groupService) {
    var vm = this;

    vm.newParty = new groupService.Party();
    vm.addParty = addParty;

    function addParty() {
      vm.parties.$add(vm.newParty);
      vm.newParty = new groupService.Party();
    }
  }

})();