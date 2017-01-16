(function() {
    'use strict';

    angular.module('app.auth')
        .controller('emailVerifyController', ['$scope', 'authService', 'firebaseDataService', '$routeParams', '$location',
            function($scope, authService, firebaseDataService, $routeParams, $location) {
            var vm = this;
                $scope.doVerify = function() {
                    firebase.auth()
                        .applyActionCode($routeParams.oobCode)
                        .then(function(data) {
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