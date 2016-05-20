var app = angular.module('myApp', []);

app.controller('UserAccountCtrl', function($scope, $http, $location) {

  $http.defaults.headers.post.Authorization = localStorage.getItem("auth"); 
  
  //$scope.host = "http://192.168.23.65:9091"; //Junaid
  //$scope.host = "http://192.168.23.1:9082"; //Dev
  $scope.host = "http://192.168.23.1:9081"; //Testing
  //$scope.host = "http://192.168.23.1:9080"; //Production
  //$scope.host = ""; //Lucky
  //$scope.host = ""; //Kingsley

  var config = {headers:  {
    'Authorization': localStorage.getItem("auth"),
    'Content-Type': 'application/x-www-form-urlencoded'
    }
  };

  // HTTP Get Request example with header
  // User profile appears on every page
  $http.get($scope.host+"/portfolio/details?username=" + localStorage.getItem("username"), config
    ).success(function(response) {
      $scope.userprofile = response;
      console.log("userprofile");
      console.log($scope.userprofile);
      
    }).error(function(response) {
      localStorage.removeItem("auth") ;
      window.location = "./login.html";
      console.log(response);
    });
  
  $scope.logout = function(){
		localStorage.removeItem("auth") ;
		window.location = "./login.html";
	};
  
});

