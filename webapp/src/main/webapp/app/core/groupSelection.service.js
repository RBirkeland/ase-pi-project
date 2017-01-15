(function() {
    'use strict';

    angular
        .module('app.core')
        .factory('groupSelectionService', groupSelectionService);

    groupSelectionService.$inject = ['$firebaseArray', 'firebaseDataService'];

    function groupSelectionService($firebaseArray, firebaseDataService) {

        var groups = null;
        var sUser = null;
        var sGroup = null;
        var usersForGroup = null;
        var sUserWeek = null;
        var userStatus = null;
        var groupStatus = null;

        var service = {
            JoinGroup: JoinGroup,
            JoinGroupStudentInformationWeek: JoinGroupStudentInformationWeek,
            JoinGroupStudentInformation: JoinGroupStudentInformation,
            getGroup: getGroup,
            getGroups: getGroups,
            getUsersForGroup: getUsersForGroup,
            getUserInformation: getUserInformation,
            getUserGroupStatus: getUserGroupStatus,
            getUserInformationWeek: getUserInformationWeek
        };

        return service;

        ////////////

        function JoinGroup(name) {
            this.email = name;
        }

        function JoinGroupStudentInformationWeek(week, uid){
            //this.week = week;
            this.token = week + "." + uid + "." + Math.random().toString(36);
            this.verified_status = false;
        }

        function JoinGroupStudentInformation(number){
            this.groupAssigned = true;
            this.groupNumber = number;
        }

        function getUserInformation(uid) {
            if (!sUser) {
                sUser = $firebaseArray(firebaseDataService.user.child(uid));
            }
            return sUser;
        }

        function getUserGroupStatus(uid) {
            if (!groupStatus) {
                groupStatus = $firebaseArray(firebaseDataService.user.child(uid).child('groupStatus'));
            }
            return groupStatus;
        }

        function getUserInformationWeek(uid) {
            if (!sUserWeek) {
                sUserWeek = $firebaseArray(firebaseDataService.user.child(uid).child('week'));
            }
            return sUserWeek;
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
