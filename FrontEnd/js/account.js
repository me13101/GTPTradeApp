var app = angular.module('myApp', []);

app.controller('AccountCtrl', function($scope, $http, $location) {
 
  $scope.newuser = null; // The model that stores register info
  
  //$scope.host = "http://192.168.23.65:9091"; //Junaid
  //$scope.host = "http://192.168.23.1:9082"; //Dev
  $scope.host = "http://192.168.23.1:9081"; //Testing
  //$scope.host = "http://192.168.23.1:9080"; //Production
  //$scope.host = ""; //Lucky
  //$scope.host = ""; //Kingsley

  $scope.goHome = function() {
    $scope.status=[1,0,0,0,0];
  };


  $scope.loading = function() {
    var $btn = $(this);
    $btn.button('loading');

    setTimeout(function () {
        $btn.button('reset');
    }, 1000);
  };

  $scope.login = function() {
    $scope.user = new Object();
    $scope.user.username = $scope.email;
    $scope.user.password = $scope.password;
    $scope.user.grant_type = 'password';

    var config = {headers:  {
      'Authorization': 'Basic RGF0YVNlcnZpY2VzOldlQXJlQXdlc29tZQ==',
      'Content-Type': 'application/x-www-form-urlencoded'
      }
    };  

    $http.post($scope.host+"/oauth/token", 
      'password='+$scope.password+'&username=' +$scope.email+'&grant_type=password',
      config
      ).success(function(response) {
        console.log(response.access_token);
        auth =  "Bearer "+response.access_token ;
        username = $scope.email;
        $http.defaults.headers.post.Authorization ="Bearer "+response.access_token ;
        // Check browser support
        if (typeof(Storage) !== "undefined") {
          // Store
          localStorage.setItem("auth", "Bearer "+response.access_token);
          localStorage.setItem("username", $scope.email); 
          window.location = "./dashboard.html";          
        } else {
          document.getElementById("result").innerHTML = "Sorry, your browser does not support Web Storage...";
        }   
      
      }).error(function(response) {
        alert('Invalid credients');
        console.log(response);
      });
  };
  
  $scope.signup = function(isValid) {
    // Do validation on the user info before sending
    if (!isValid) {
      alert("Error in the given information, please check again.");
      return false;
    }

    // Send data to the backend
    $http.post($scope.host+'/user/create', this.newuser).then(OnRegistrationSuccess, OnRegistrationError);

    // Fake function for debugging
    //OnRegistrationSuccess({});
    // OnRegistrationError({});
    
    // The success & error callbacks
    function OnRegistrationSuccess(response){
      // Jump to the sign in page is success
      $scope.goHome();
      console.log($scope.status);
      // Clear the newuser model
      $scope.newuser = null;
    };
    function OnRegistrationError(response) {
      console.error(response);
      alert("Email address is not valid, please try another one.")
    };
  };
  
  $scope.forgetPassword = function() {

    var config = {headers:  {
      'Authorization': 'Basic RGF0YVNlcnZpY2VzOldlQXJlQXdlc29tZQ==',
      'Content-Type': 'application/x-www-form-urlencoded'
      }
    };
    $http.post($scope.host+"/user/password/reset", 'email='+$scope.recoveremail, config)
    .success(function(response) {
      $scope.status = [0,0,0,1];
    }).error(function(response) {
      console.log('Error: Email not found.');
      alert("Error: Email Not Found");
    });
  };

  $scope.ToolObjLength = function(obj) {
    var key, cnt = 0;
    for (key in obj){
      if (obj.hasOwnProperty(key)) {
        cnt += 1;
      }
    }
    return cnt;
  };

});

app.directive('confirmPassword', function() {
  return {
    require: 'ngModel',
    link: function(scope, elem, attrs, ctrl) {
      ctrl.$validators.confirmPassword = function(modelValue, viewValue) {
        var pw1 = document.getElementById(attrs.confirmPassword).value;
        var pw2 = elem.val();
        var v = true;
        if (pw1 === '' || pw2 === '' )
          v = true
        else
          v = (pw1===pw2 && pw1 !== '');
        if (v)
          scope.RegForm.RegPassword.$setValidity("origPassword", true);
        return v;
      };
    }
  };
});

app.directive('origPassword', function() {
  return {
    require: 'ngModel',
    link: function(scope, elem, attrs, ctrl) {
      ctrl.$validators.origPassword = function(modelValue, viewValue) {
        var pw1 = elem.val();
        var pw2 = document.getElementById(attrs.origPassword).value;
        var v = true;
        if (pw2 === '')
          v = true;  // No input in the "confirm password" textbox yet
        else
          v = (pw1===pw2 && pw1 !== '');
        if (!v)
          scope.RegForm.RegConfirmPassword.$setValidity("confirmPassword", false);
        else
          scope.RegForm.RegConfirmPassword.$setValidity("confirmPassword", true);

        return v;
      };
    }
  };
});


app.directive('gtpemail', function() {
  return {
    require: 'ngModel',
    link: function(scope, elm, attrs, ctrl) {
      ctrl.$validators.gtpemail = function(modelValue, viewValue) {

        // var EMAIL_REGEXP = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
        var EMAIL_REGEXP = /^([\w-]+(?:\.[\w-]+)*)@([\w-]+(?:\.[\w-]+)+)$/i;

        if (ctrl.$isEmpty(modelValue)) {
          // consider empty models to be valid
          return true;
        }

        if (EMAIL_REGEXP.test(viewValue)) {
          // it is valid
          return true;
        }

        // it is invalid
        return false;
      };
    }
  };
});