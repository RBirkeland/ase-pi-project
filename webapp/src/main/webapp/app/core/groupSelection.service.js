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

        var service = {
            Group: Group,
            JoinGroup: JoinGroup,
            getGroup: getGroup,
            getGroups: getGroups,
            getUsersForGroup: getUsersForGroup
        };

        return service;

        ////////////

        function Group() {
            this.name = '';
            this.time = '';
            this.weekday = '';
            this.students = '';
        }

        function JoinGroup(uid) {
            this.email = uid;
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
