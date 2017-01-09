(function() {
  'use strict';

  angular
    .module('app.groupSelection')
    .config(configFunction);

  configFunction.$inject = ['$routeProvider'];

  function configFunction($routeProvider) {
    $routeProvider.when('/groupselection', {
      templateUrl: 'app/groupSelection/groupSelection.html',
      controller: 'GroupSelectionController',
      controllerAs: 'vm',
      resolve: {user: resolveUser}
    });
  }

  resolveUser.$inject = ['authService'];

  function resolveUser(authService) {
    return authService.firebaseAuthObject.$requireSignIn();
  }

})();
