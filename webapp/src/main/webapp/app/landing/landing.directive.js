(function() {
  'use strict';

  angular
    .module('app.layout')
    .directive('gzLanding', gzLanding);

  function gzLanding() {
    return {
      templateUrl: 'app/landing/landing.html',
      restrict: 'E',
      scope: {},
      controller: LandingController,
      controllerAs: 'vm'
    };
  }

  LandingController.$inject = ['$location', 'authService'];

  function LandingController($location, authService) {
    var vm = this;

    vm.isLoggedIn = authService.isLoggedIn;
    vm.logout = logout;

    function logout() {
      authService.logout();
      $location.path('/');
    }
  }

})();