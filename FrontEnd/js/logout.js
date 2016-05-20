var app = angular.module('myApp', []);

function OnTimeChange($scope) {
  $scope.secondLeft -= 1;
}
function OnTimeOver() {
  window.location = './login.html';
}

app.controller('LogoutCtrl', function($scope, $timeout) {
  $scope.const_secondLeft = 3;
  $scope.secondLeft = $scope.const_secondLeft;

  $scope.msLeft = $scope.secondLeft * 1000;
  for (i=1; i<=$scope.const_secondLeft; i++) {
    $timeout(OnTimeChange, 1000*i, true, $scope);
  }
  $timeout(OnTimeOver, $scope.msLeft+100);

  $scope.JumpToLogin = function() {
    window.location = './login.html';
  };

});