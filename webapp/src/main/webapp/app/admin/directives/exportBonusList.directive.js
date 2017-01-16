(function() {
  'use strict';

  angular
    .module('app.admin')
    .directive('exportBonusList', exportBonusList);

  function exportBonusList() {
    return {
      templateUrl: 'app/admin/directives/exportBonusList.html',
      restrict: 'E',
      controller: ExportBonusListController,
      controllerAs: 'vm',
      bindToController: true,
      scope: {
        groups: '='
      }
    };
  }

  ExportBonusListController.$inject = ['exportService'];

  function ExportBonusListController(exportService) {
    var vm = this;

    vm.users = exportService.getBonusStatus();
    vm.exportBonusList = exportBonusList;

    function exportBonusList() {
      var users = vm.users["$$state"]["value"];
      var csvContent = "user;attendance;presentation\n";
      for(var userKey in users){
        var user = users[userKey];
        if(user != null){
          csvContent = csvContent + user.email + ";" +
                  user.attendanceCount + ";" + user.presented + "\n";
        }
      }
      createCSVDownload(csvContent);
    }

    function createCSVDownload(csvContent){
        var pom = document.createElement('a');
        var blob = new Blob([csvContent],{type: 'text/csv;charset=utf-8;'});
        var url = URL.createObjectURL(blob);
        pom.href = url;
        pom.setAttribute('download', 'ASE-Student-Bonus.csv');
        pom.click();
    }
  }

})();