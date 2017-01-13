(function() {
    'use strict';

    angular
        .module('app.core')
        .factory('userInfoService', userInfoService);

    userInfoService.$inject = ['$firebaseArray', 'firebaseDataService'];

    function userInfoService($firebaseArray, firebaseDataService) {

        var sGroup = null;

        var service = {
            getGroupForUser: getGroupForUser
        };

        return service;

        ////////////

        function getGroupForUser(uid) {
            if (!sGroup) {
                sGroup = $firebaseArray(firebaseDataService.user.child(uid).child('groupStatus'));
            }
            return sGroup;
        }

    }

})();
