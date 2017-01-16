(function() {
    'use strict';

    angular
        .module('app.core')
        .factory('userInfoService', userInfoService);

    userInfoService.$inject = ['$firebaseArray', 'firebaseDataService'];

    function userInfoService($firebaseArray, firebaseDataService) {

        var sGroup = null;

        var service = {
            getGroupForUser: getGroupForUser,
            getUserInformation: getUserInformation
        };

        return service;

        ////////////

        function getGroupForUser(uid) {
            if (!sGroup) {
                sGroup = $firebaseArray(firebaseDataService.user.child(uid).child('groupStatus'));
            }
            return sGroup;
        }

        function getUserInformation(uid) {
            return ($firebaseArray(firebaseDataService.user.child(uid))).$loaded().then(function(data) {
                var weekArray = [];
                var weekData = data.$getRecord("week");
                for(var weekKey in weekData) {
                    if(!isNaN(weekKey)) {
                        var weekObject = {
                            weekNumber: weekKey,
                            status: weekData[weekKey]["verified_status"]
                        };
                        weekArray.push(weekObject);
                    }
                }
                var userInformation = {
                    bonusStatus:  data.$getRecord("bonusStatus").received,
                    presented: data.$getRecord("bonusStatus").presented,
                    week: weekArray
                };
                return userInformation;
            });


        }

    }

})();
