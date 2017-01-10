(function() {
    'use strict';

    angular
        .module('app.core')
        .factory('groupSelectionService', groupSelectionService);

    groupSelectionService.$inject = ['$firebaseArray', 'firebaseDataService'];

    function groupSelectionService($firebaseArray, firebaseDataService) {

        var groups = null;

        var service = {
            Group: Group,
            getGroups: getGroups
        };

        return service;

        ////////////

        function Group() {
            this.name = '';
            this.time = '';
            this.weekday = '';
            this.students = '';
        }

        function getGroups() {
            if (!groups) {
                groups = $firebaseArray(firebaseDataService.groups);
            }
            console.log(groups);
            return groups;
        }

    }

})();
