(function() {
    'use strict';

    angular.module('app.auth')
        .controller('emailVerifyController', ['$scope', 'authService', 'firebaseDataService', '$routeParams', '$location',
            function($scope, authService, firebaseDataService, $routeParams, $location) {
            var vm = this;

            console.log($routeParams.oobCode);
                $scope.doVerify = function() {
                    firebase.auth()
                        .applyActionCode($routeParams.oobCode)
                        .then(function(data) {
                            /*
                            if (authService.firebaseAuthObject.$getAuth() != null) {
                                vm.user = authService.firebaseAuthObject.$getAuth().providerData[0].uid;
                                vm.userId = authService.firebaseAuthObject.$getAuth().uid;
                            } else {
                                return;
                            }
                            var userMailRef = firebase.database().ref('user/'+vm.userId+'/');
                            userMailRef.update({ emailVerified: true });
                            // the above is assuming you have root/users/uid/
                            */
                            bootbox.alert({
                                message: "Verification complete!",
                                callback: function () {
                                    $location.path('/groupSelection');
                                }
                            });
                        })
                        .catch(function(error) {
                            $scope.error = error.message;
                            bootbox.confirm({
                                message: $scope.error + "\nDo you want a new Authentification Code?",
                                buttons: {
                                    confirm: {
                                        label: 'Yes',
                                        className: 'btn-success'
                                    },
                                    cancel: {
                                        label: 'No',
                                        className: 'btn-danger'
                                    }
                                },
                                callback: function (result) {
                                    if(result == true){
                                        vm.user.sendEmailVerification().then(function() {
                                            bootbox.alert('Email sent!\nPlease verify your email address!');
                                            // Email sent.
                                        }, function(error) {
                                            bootbox.alert('An error during the email sending process happened!\nPlease contact us!');
                                            // An error happened.
                                        });
                                    }
                                }
                            });
                        })
                };
            }
    ]);

})();