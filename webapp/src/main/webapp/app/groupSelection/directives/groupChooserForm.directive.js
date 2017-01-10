(function() {
  'use strict';

  angular
    .module('app.groupSelection')
    .directive('groupChooserForm', groupChooserForm);

  function groupChooserForm() {
    return {
      templateUrl: 'app/groupSelection/directives/groupChooserForm.html',
      restrict: 'E',
      controller: GroupFormController,
      controllerAs: 'vm',
      bindToController: true,
      scope: {
        groups: '='
      }
    };
  }

  GroupFormController.$inject = ['groupService'];

  function GroupFormController(groupService) {
    var vm = this;

    vm.newGroup = new groupService.Group();
    vm.addGroup = addGroup;

    function addGroup() {
      vm.groups.$add(vm.newGroup);
      vm.newGroup = new groupService.Group();
    }
  }

})();