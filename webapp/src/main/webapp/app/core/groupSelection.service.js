(function() {
    'use strict';

    angular
        .module('app.core')
        .factory('groupSelectionService', groupSelectionService);

    groupSelectionService.$inject = ['$firebaseArray', 'firebaseDataService'];

    function groupSelectionService($firebaseArray, firebaseDataService) {

        var groups = null;
        var sGroup = null;
        var usersForGroup = null;
        var sUser = null;

        var service = {
            JoinGroup: JoinGroup,
            JoinGroupStudentInformation: JoinGroupStudentInformation,
            getGroup: getGroup,
            getGroups: getGroups,
            getUsersForGroup: getUsersForGroup,
            getUserInformation: getUserInformation
        };

        return service;

        ////////////

        function JoinGroup(name) {
            this.email = name;

        }

        function JoinGroupStudentInformation(week){
            this.week = week;
            this.token = Math.random().toString(36);
            this.verified_status = false;
        }

        function getUserInformation(uid) {
            if (!sUser) {
                sUser = $firebaseArray(firebaseDataService.user.child(uid).child('week'));
            }
            return sUser;
        }


        function getGroup(group) {
            if (!sGroup) {
                sGroup = $firebaseArray(firebaseDataService.groups.child(group).child('students'));
            }
            return sGroup;
        }

        function getUsersForGroup(group) {
            if (!usersForGroup) {
                usersForGroup = $firebaseArray(firebaseDataService.groups.child(group).child('students'));
            }
            return usersForGroup;
        }

        function getGroups() {
            if (!groups) {
                groups = $firebaseArray(firebaseDataService.groups);
            }
            return groups;
        }

    }

})();
