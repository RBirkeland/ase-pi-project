(function() {
  'use strict';

  angular
    .module('app.core')
    .factory('firebaseDataService', firebaseDataService);

  function firebaseDataService() {
    var root = firebase.database().ref();

    var service = {
      root: root,
      groups: root.child('groups'),
      users: root.child('users'),
      user: root.child('user'),
      emails: root.child('emails'),
      textMessages: root.child('textMessages')
    };

    return service;
  }

})();
