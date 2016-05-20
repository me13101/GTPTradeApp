var app = angular.module('myApp', []);

app.run(['$anchorScroll', function($anchorScroll) {
  $anchorScroll.yOffset = 50;   // always scroll by 50 extra pixels
}]);

app.controller('TradingCtrl', function($anchorScroll, $scope, $http, $location) {

  $http.defaults.headers.post.Authorization = localStorage.getItem("auth"); 
  $scope.executeStockProfile = null;
  $scope.isStockExecuted =0;
  $scope.netAssetUp = true;
  $scope.currentlyDisplayedStock = null;
  
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

  // get profile details 
  // userprofile
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
    
  //Get Stock List 
  //stockList
  $http.get($scope.host+"/marketdata/get/all/latest", config
    ).success(function(response) {
      $scope.stockList = response.stocklist;
      console.log(response);
      
    }).error(function(response) {
      localStorage.removeItem("auth") ;
      window.location = "./login.html";
      console.log(response);
    });  
    
  /** functions are defined here ~**/
  
  $scope.logout = function(){
    localStorage.removeItem("auth") ;
    window.location = "./logout.html";
  };
  
  // function to display chosen stock
  // currentlyDisplayedStock 
  $scope.displayStock = function(id) {
    $scope.displayId = id;
    console.log("stockdetails1");
    // Stock details
    $http.get($scope.host+"/marketdata/get/latest?stock=" + id, config
    ).success(function(response) {
      console.log('dadsd');
      console.log(response);
      $scope.currentlyDisplayedStock = response.stocklist[0];
      $scope.netAssetUp = ($scope.currentlyDisplayedStock.delta > 0);
      
    }).error(function(response) {
      localStorage.removeItem("auth") ;
      window.location = "./logout.html";
      console.log(response);
    });
    
    // Stock historical data
    //   currentlyDisplayedStockHistoricalData
    $http.get($scope.host+"/marketdata/get/historical?stock=" + id , config
    ).success(function(response) {
      $scope.currentlyDisplayedStockHistoricalData = response.data.pricelist;
      console.log("stockdetails");
      console.log($scope.currentlyDisplayedStockHistoricalData);
      
    }).error(function(response) {
      localStorage.removeItem("auth") ;
      window.location = "./logout.html";
      console.log(response);
    });
  };
  
  $scope.scrollTo = function(id) {
    var old = $location.hash();
    $location.hash(id);
    $anchorScroll();
    //reset to old to keep any additional routing logic from kicking in
    $location.hash(old);
  };
  
  // after clicking the buy/sell button
  // 
  $scope.confirmStock = function() {
    //refresh stock details
    $http.get($scope.host+"/marketdata/get/latest?stock=" + $scope.currentlyDisplayedStock.data.stock_id, config
    ).success(function(response) {
      $scope.currentlyDisplayedStock = response.stocklist[0];
      console.log("stockdetails");
      console.log($scope.currentlyDisplayedStock);     
    }).error(function(response) {
      localStorage.removeItem("auth") ;
      window.location = "./logout.html";
      console.log(response);
    });    
  };
  
  // from true false to buy / sell string
  $scope.displaySide = function(){
    if ($scope.executeStockProfile != null){
      if ($scope.executeStockProfile.isBuy)
        return 'BUY';
      else {
        return 'SELL';
      }
    }
  }
  
  // after clicking the buy/sell button
  $scope.executeStock = function() {
    //refresh stock details
    tempConfig = {headers:  {
      'Authorization': localStorage.getItem("auth"),
      'Content-Type': 'application/json'
      }
    };
    $scope.trade = new Object();
    
    $scope.trade.ticker = $scope.currentlyDisplayedStock.data.stock_id;
    
    $scope.trade.qty = $scope.executeStockProfile.quantity;
    
    $http.post($scope.host+"/trading/" + $scope.displaySide().toLowerCase(),$scope.trade, tempConfig
    ).success(function(response) {
      $scope.executedStock = response;
      console.log(response);
      $scope.isStockExecuted =1;      
    }).error(function(response) {
      //localStorage.removeItem("auth") ;
      //window.location = "./logout.html";
      //console.log(response);
    });    
  };

  $scope.toPercentageData = function(x) {
   
    return Math.round(Math.abs(x)*10000)/100;
  };
  
    $http.get($scope.host+"/game/gamer/profile", config
  ).success(function(response) {
    $scope.gamer = response;
      console.log('gamer');
      console.log(response);
    }).error(function(response) {

 });

  
});
function ToolStringToDate(dateStr) {
  //2015-10-15 12:10:00"
  var dateParts = dateStr.split(' ');
  var timeParts = dateParts[1].split(':');
  var dateParts = dateParts[0].split('-');

  var date = new Date( dateParts[0], parseInt(dateParts[1], 10) - 1, dateParts[2], timeParts[0], timeParts[1], timeParts[2] );

  return date.getTime();
}
function ToolRefreshData(scope, timeout, http, interval){
  // Config for the ajax requests
  var config = {headers:  {
    'Authorization': localStorage.getItem("auth"),
    'Content-Type': 'application/x-www-form-urlencoded'
    }
  };
  //Get the data from API
  // http.get(scope.host+'/portfolio/history/details', config).success(function(response){
  
  var config = {headers:  {
    'Authorization': localStorage.getItem("auth"),
    'Content-Type': 'application/x-www-form-urlencoded'
    }
  };

  http.get(scope.host+"/marketdata/get/historical?stock=" + scope.displayId, config
    ).success(function(response) {   
    if (response.data != null){
      var result = [];
      var listSrcData = response.data.pricelist;

      var dictNameToIndex = {};

      if (listSrcData.length === 0) {
        console.error('No data given by the server.');
      }
      
      result.push({
            name: response.name,
            data: []
          });
      
      for (i = 0; i < listSrcData.length; i++) { 
          result[0].data.push([ToolStringToDate(listSrcData[i].datetime),listSrcData[i].price ]);
      }
   console.log(result);
      
      // Update the chart
      result.forEach(function(val, index, array){
        scope.chart.series[index].setData(val.data, false);
        scope.chart.series[index].name = val.name;
      });
      scope.chart.redraw();
    }
      
      
      
      
      
    }).error(function(response) {
    });
  
  
  // Hard code random update for demo
  // var t = Math.round(Math.random()*10);
  // scope.chart.series[0].setData(scope.seriesOptions[0].data.slice(10*t,1000+10*t), true);
  timeout(ToolRefreshData, interval, true, scope, timeout, http, interval);
};

app.directive('chartPortfolioValue', function () {
  return {
    restrict: 'C',
    replace: true,
    // scope: {
    //   items: '='
    // },
    controller: function ($scope, $element, $attrs, $http, $timeout) {
      $scope.seriesOptions = [{ name: 'AAPL', data:[] }];
       
      $scope.seriesOptions.push(new Array($scope.seriesOptions[0]));
      $scope.seriesOptions.push(new Array($scope.seriesOptions[0]));

      $timeout(
        ToolRefreshData,
        1000,
        true,
        $scope,
        $timeout,
        $http,
        1000
      );
    },
    template: '<div id="chartPV" style="margin: 0 auto">not working</div>',
    link: function (scope, element, attrs) {
      console.log(3);
      var chart = new Highcharts.StockChart({
          chart: {
            renderTo: 'chartPV',
            height: 300
          },

          yAxis: {
              labels: {
                  formatter: function () {
                      return (this.value > 0 ? ' + ' : '') + this.value + '%';
                  }
              },
              plotLines: [{
                  value: 0,
                  width: 2,
                  color: 'silver'
              }]
          },

          navigator : {
            enabled : false
          },

          plotOptions: {
              series: {
                  compare: 'percent'
              }
          },

          tooltip: {
              pointFormat: '<span style="color:{series.color}">{series.name}</span>: <b>{point.y}</b> ({point.change}%)<br/>',
              valueDecimals: 2
          },

          series: scope.seriesOptions
      });
      scope.chart = chart;
      // scope.$watch("items", function (newValue) {
      //   console.log('Value Changed.')
      //   chart.series[0].setData(newValue, true);
      // }, true);
      // var chart = new Highcharts.Chart({
      //   chart: {
      //     renderTo: 'chartPV',
      //     plotBackgroundColor: null,
      //     plotBorderWidth: null,
      //     plotShadow: false,
      //     type: 'pie',
      //     height: 200
      //   },
      //   title: {
      //     text: ''
      //   },
      //   tooltip: {
      //     pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>',
      //     percentageDecimals: 1
      //   },
      //   plotOptions: {
      //     pie: {
      //       allowPointSelect: true,
      //       cursor: 'pointer',
      //       dataLabels: {
      //         enabled: true,
      //         color: '#000000',
      //         connectorColor: '#000000',
      //         formatter: function () {
      //           return '<b>' + this.point.name + '</b>: ' + parseFloat(Math.round(this.percentage * 100) / 100).toFixed(2)  + ' %';
      //         }
      //       }
      //     }
      //   },
      //   series: [{
      //     type: 'pie',
      //     name: 'Browser share',
      //     data: scope.items
      //   }]
      // });
      // scope.$watch("items", function (newValue) {
      //   chart.series[0].setData(newValue, true);
      // }, true);
      
    }
  }
});


app.directive('integer', function() {
  return {
    require: 'ngModel',
    link: function(scope, elm, attrs, ctrl) {
      ctrl.$validators.integer = function(modelValue, viewValue) {
        var INTEGER_REGEXP = /^\-?\d+$/;
        
        if (ctrl.$isEmpty(modelValue)) {
          // consider empty models to be valid
          return true;
        }

        if (INTEGER_REGEXP.test(viewValue)) {
          // it is valid
          return true;
        }

        // it is invalid
        return false;
      };
    }
  };
});