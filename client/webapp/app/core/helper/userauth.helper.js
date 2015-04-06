(function() {
	"use strict";

	angular
		.module("app.core")
		.factory("userauth", UserAuth);

	function UserAuth($cookieStore, $rootScope, $state) {
		var USER_SESSION_KEY = "epl-user-session";
		var EPL_AUTH_HEADER = "epl-authentication";

		var service = {
			// Save User session into cookie
			putUserSession: putUserSession,
			// Take User session from cookie 
			getUserSession: getUserSession,
			// Remove User session on cookies
			delUserSession: delUserSession,
			// Set logged user
			setLoggedUser: setLoggedUser,
			// Set user profile picture
			setProfilePicture: setProfilePicture
		};

		return service;

		function setProfilePicture(url) {
			$rootScope.profileUrl = url;
		}

		function setLoggedUser(data) {
			$rootScope.loggedUser = null;
			if (data) {
				$rootScope.loggedUser = data.userNetwork;
				$rootScope.loggedUser["userRole"] = data.role;
			}
		}

		function delUserSession() {
			$cookieStore.remove(USER_SESSION_KEY);
		}

		function getUserSession() {
			return $cookieStore.get(USER_SESSION_KEY);
		}

		function putUserSession(session, signinType) {
			var savedCookie = {
				"session": session,
				"signinType": signinType
			}
			$cookieStore.put(USER_SESSION_KEY, savedCookie);
		}
	}
	
})();