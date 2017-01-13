(function() {
  'use strict';

  angular
    .module('app.auth')
    .controller('AuthController', AuthController);

  AuthController.$inject = ['$location', 'authService'];

  function AuthController($location, authService) {
    var vm = this;

    vm.error = null;

    vm.register = register;
    vm.login = login;

    function register(user) {
      if(emailIsValid(user.email)){
          return authService.register(user)
              .then(function() {
                  return vm.login(user);
              })
              .then(function() {
                  var user = firebase.auth().currentUser;
                  user.sendEmailVerification().then(function() {
                      console.log('email sent');
                      // Email sent.
                  }, function(error) {
                      console.log('error during email sending');
                      // An error happened.
                  });
              })
              .catch(function(error) {
                  vm.error = error;
              });
      } else {
          bootbox.alert("Please sign up with your mytum email!");
      }
    }

    function emailIsValid(email){
      var re = new RegExp("^([a-z0-9]{7}@mytum.de)$");
      if(re.test(email)) {
        return true;
      } else {
          return false;
      }
    }

    function login(user) {
      return authService.login(user)
      .then(function () {
        if(user.emailVerified == true ){
            $location.path('/groupselection');
        } else {
            bootbox.alert({
                message: "Please verify your email first!",
                callback: function () {
                        console.log('reroute to login');
                        $location.path('/login');
                    }
            });
        }
      })
      .catch(function(error) {
          vm.error = error;
      });
    }
  }

})();