(function() {
  'use strict';

  angular
    .module('app.admin')
    .directive('groupTable', groupTable);

  function groupTable() {
    return {
      templateUrl: 'app/admin/directives/groupTable.html',
      restrict: 'E',
      controller: AdminGroupTableController,
      controllerAs: 'vm',
      bindToController: true,
      scope: {
        groups: '='
      }
    };
  }

  function AdminGroupTableController() {
    var vm = this;

    vm.removeGroup = removeGroup;

    function removeGroup(group) {
      vm.groups.$remove(group);
    }
  }

})();