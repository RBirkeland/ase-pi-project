(function () {
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
            if (emailIsValid(user.email)) {
                return authService.register(user)
                    .then(function () {
                        return vm.login(user);
                    })
                    .then(function(){
                        authService.createUserDB();
                    })
                    .then(function () {
                        var user = firebase.auth().currentUser;

                        user.sendEmailVerification().then(function () {
                            bootbox.alert('Email sent!\nPlease verify your email address!');
                            // Email sent.
                        }, function (error) {
                            bootbox.alert('An error during the email sending process happened!\nPlease contact us!');
                            // An error happened.
                        });
                    })
                    .catch(function (error) {
                        vm.error = error;
                    });
            } else {
                bootbox.alert("Please sign up with your mytum email!");
            }
        }

        function emailIsValid(email) {
            var re = new RegExp("^([a-z0-9]{7}@mytum.de)$");
            if (re.test(email)) {
                return true;
            } else {
                return false;
            }
        }

        function login(user) {
            return authService.login(user)
                .then(function () {
                    if (authService.firebaseAuthObject.$getAuth().emailVerified == true) {
                        $location.path('/groupselection');
                    } else {
                        bootbox.alert({
                            message: "Please verify your email first!",
                            callback: function () {
                                $location.path('/login');
                                return authService.logout();
                            }
                        });
                    }
                })
                .catch(function (error) {
                    vm.error = error;
                });
        }
    }

})();