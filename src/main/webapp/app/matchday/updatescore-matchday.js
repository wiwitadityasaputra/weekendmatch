(function() {
	'use strict';
	
	angular
		.module('app.matchday')
		.controller('UpdateScoreMatchday', UpdateScoreMatchday);

	function UpdateScoreMatchday(initData, matchdayhelpwer, dataservice, $scope, 
			$rootScope, $modal) {

		$rootScope.$broadcast('state-btn', 'updatescore');
		$rootScope.$broadcast('show-phase-nav', true);

		var mh = matchdayhelpwer;
		var vm = this;

		vm.datas = [];
		vm.modalInstance = null;
		vm.score = []; // index 0 for home, 1 for away

		vm.preEditScore = preEditScore;
		vm.cancelEditScore = cancelEditScore;
		vm.doEditScore = doEditScore;

		$scope.$on('vm.model', modelChangeListener);

		activate();
		function activate() {
			convertModel(initData.matchdayModelView.model);
		}

		function modelChangeListener(event, modelViews) {
			convertModel(modelViews);
		}

		function doEditScore() {
			vm.modalInstance.dismiss('cancel');

			var matchdayId = vm.currMatch.id;
			var updatescore = {
				homeGoal: vm.score[0],
				awayGoal: vm.score[1]
			}

			dataservice.updateScore(matchdayId, updatescore)
				.then(afterSubmit);
		}

		function afterSubmit() {
			$rootScope.$broadcast('load-matchday');
		}

		function cancelEditScore() {
			vm.modalInstance.dismiss('cancel');
		}

		function preEditScore(m) {
			vm.currMatch = m;

			vm.score[0] = m.homeGoal;
			vm.score[0] = vm.score[0] < 0 ? 0 : vm.score[0];
			vm.score[1] = m.awayGoal;
			vm.score[1] = vm.score[1] < 0 ? 0 : vm.score[1];

			vm.modalInstance = $modal.open({
				templateUrl: 'editScore.html',
				size: 'lg',
				scope: $scope
			});     
		}

		function convertModel(modelViews){
			vm.datas = mh.convertModelViewToModel(modelViews);
		}
	}
	
})();
