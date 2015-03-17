(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("teamservice", TeamService);

	// Note: Please read dataservice.js factory before using this factory
	function TeamService($http, $rootScope, adminauth) {
		var service = {
			// Get all team
			getAllTeam: getAllTeam,
			// Edit team information
			editTeam: editTeam,
			// Get uploadURL
			getUploadURL: getUploadURL
		}

		return service;

		function getUploadURL(type, object) {
			if ('slideshow' === type)
				var result = [
					"api/upload/slideshow/teamId/",
					object.teamId,
					"?",
					adminauth.getAuthKey(),
					"=",
					adminauth.getAdminSession()
				];
				return result.join('');
		}

		function getAllTeam() {
			$rootScope.promise = $http.get("api/teams")
					.then(getData)
					.catch(function(message) {
					});

			return $rootScope.promise;
		}

		function editTeam(team) {
			var req = adminauth.getConf(team, "PUT", "api/teams/" + team.id);

			$rootScope.promise = $http(req)
					.then(process)
					.catch(process);
			return $rootScope.promise;
		}

		function getData(result) {
			return result.data;
		}

		function process(result) {
			return result;
		}
	}
})();