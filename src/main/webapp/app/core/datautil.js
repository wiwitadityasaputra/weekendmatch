(function() {
	'use strict';

	angular
		.module('app.core')
		.factory('datautil', Datautil);

	function Datautil() {
		var isPrimed = false;
		var primePromise;

		var service = {
			getFormattedWeek: getFormattedWeek
		};

		return service;

		function getFormattedWeek(unixTimestamp, weekNumber) {
			var date = new Date(unixTimestamp);
			var m = moment(date);
			return '#' + weekNumber + ' - ' + m.format('YYYY, DD MMM');
		}
	}
})();
