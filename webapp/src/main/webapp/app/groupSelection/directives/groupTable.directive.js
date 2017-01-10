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
        groups: '='
      }
    };
  }

  function GroupTableController() {
    var vm = this;

    vm.joinGroup = joinGroup;

    function joinGroup(group) {
      console.log("joiningGroup: " + group);
      //vm.groups.$remove(group);
    }
  }

})();