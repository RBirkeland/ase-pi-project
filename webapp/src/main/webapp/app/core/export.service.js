(function() {
    'use strict';

    angular
        .module('app.core')
        .factory('exportService', exportService);

    exportService.$inject = ['$firebaseArray', 'firebaseDataService', '$firebaseObject'];

    function exportService($firebaseArray, firebaseDataService, $firebaseObject) {

        var users = null;

        var service = {
            getBonusStatus: evaluateBonus
        };

        return service;

        ////////////

        function getBonusStatus(){
            return evaluateBonus();
        }

        function getUsers() {
            if (!users) {
                users = ($firebaseArray(firebaseDataService.user));
                console.log(users);
            }
            return users;
        }

        function evaluateBonus() {
            var outputData = getUsers().$loaded().then(function(data) {
                var exportData = new Array(data.size);
                var userBonus = new Array(data.size);
                for(var userKey in data){
                    var user = data[userKey];
                    userBonus[userKey] = 0;
                    for(var weekKey in user.week){
                        if(user.week[weekKey]["verified_status"]){
                            userBonus[userKey]++;
                        }
                    }
                    if(!isNaN(userKey)){
                        var userData = {
                            email: user.email,
                            attendanceCount: userBonus[userKey],
                            presented: user.bonusStatus["presented"]
                        };
                        exportData.push(userData);
                    }
                }
                return exportData;
            });
            return outputData;
        }
    }

})();
