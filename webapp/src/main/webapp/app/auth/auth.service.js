(function() {
  'use strict';

  angular
    .module('app.auth')
    .factory('authService', authService);

  authService.$inject = ['$firebaseAuth', 'firebaseDataService'];

  function authService($firebaseAuth, firebaseDataService) {
    var firebaseAuthObject = $firebaseAuth();

    var service = {
      firebaseAuthObject: firebaseAuthObject,
      register: register,
      login: login,
      logout: logout,
      isLoggedIn: isLoggedIn,
      createUserDB: createUserDB
    };

    return service;

    ////////////

    function register(user) {
      return firebaseAuthObject.$createUserWithEmailAndPassword(user.email, user.password);
    }

    function login(user) {
      return firebaseAuthObject.$signInWithEmailAndPassword(user.email, user.password);
    }

    function logout() {
      firebaseAuthObject.$signOut();
    }

    function isLoggedIn() {
      return firebaseAuthObject.$getAuth();
    }

    function createUserDB() {
       var user = firebaseAuthObject.$getAuth();
       var userMailRef = firebase.database().ref('user/'+user.uid+'/');
       userMailRef.update({
           bonusStatus: {
             presented : false,
             received : false
           },
           groupStatus: {
             groupAssigned : false
           },
           email : user.email
       });
    }

  }

})();