(function() {
  'use strict';

  angular
    .module('app.core')
    .factory('groupService', groupService);

  groupService.$inject = ['$firebaseArray', 'firebaseDataService'];

  function groupService($firebaseArray, firebaseDataService) {

    var groups = null;
    
    var service = {
      Group: Group,
      getGroups: getGroups,
      reset: reset
    };

    return service;

    ////////////

    function Group() {
      this.name = '';
      this.time = '';
      this.weekday = '';
      this.students = '';
    }

    function getGroups() {
      if (!groups) {
        groups = $firebaseArray(firebaseDataService.groups);
      }
      console.log(groups);
      return groups;
    }

    function reset() {
      if (groups) {
        groups.$destroy();
        groups = null;
      }
    }

  }

})();
