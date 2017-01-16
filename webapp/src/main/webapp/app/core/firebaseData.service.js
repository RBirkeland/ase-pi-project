(function() {
  'use strict';

  angular
    .module('app.core')
    .factory('firebaseDataService', firebaseDataService);

  function firebaseDataService() {
    var root = firebase.database().ref();

    var service = {
      root: root,
      admin: root.child('admin'),
      groups: root.child('groups'),
      user: root.child('user')
    };

    return service;
  }

})();
