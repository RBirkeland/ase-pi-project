(function() {
  'use strict';

  angular
    .module('app.admin')
    .config(configFunction);

  configFunction.$inject = ['$routeProvider'];

  function configFunction($routeProvider) {
    $routeProvider.when('/admin', {
      templateUrl: 'app/admin/admin.html',
      controller: 'AdminController',
      controllerAs: 'vm',
      resolve: {user: resolveUser}
    });
  }

  resolveUser.$inject = ['authService', 'firebaseDataService', '$firebaseArray', '$location'];

  function resolveUser(authService, firebaseDataService, $firebaseArray, $location) {
    var isAdmin;
    if(authService.isLoggedIn){
      var admin = $firebaseArray(firebaseDataService.admin);
      return admin.$loaded().then(function () {
          for(var userIdKey in admin){
              if(!isNaN(userIdKey)){
                if(authService.firebaseAuthObject.$getAuth().uid == admin[userIdKey].$id){
                  isAdmin = true;
                  return authService.firebaseAuthObject.$requireSignIn();
                }
              }
          }
          if(!isAdmin){
              $location.path('/');
          }
      }).catch(function(error) {
          return authService.firebaseAuthObject.$requireSignIn();
        });
    }
   // return authService.firebaseAuthObject.$requireSignIn();
  }

})();
