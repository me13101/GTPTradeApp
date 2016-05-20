var app = angular.module('myApp', []);

app.controller('DashboardCtrl', function($scope, $http, $location, limitToFilter) {

	$http.defaults.headers.post.Authorization = localStorage.getItem("auth");

	//$scope.host = "http://192.168.23.65:9091"; //Junaid
	//$scope.host = "http://192.168.23.1:9082"; //Dev
	$scope.host = "http://192.168.23.1:9081"; //Testing
	//$scope.host = "http://192.168.23.1:9080"; //Production
	//$scope.host = ""; //Lucky
	//$scope.host = ""; //Kingsley

	$scope.ideas = [
	                ['no data', 1]
	                ];
	//console.log($scope.ideas);

	$scope.limitedIdeas = limitToFilter($scope.ideas, $scope.ideas.length);


	var config = {headers:  {
		'Authorization': localStorage.getItem("auth"),
		'Content-Type': 'application/x-www-form-urlencoded'
	}
	};

	/**
	 * portfolio panel details
	 */
	$http.get($scope.host+"/portfolio/details?username=" + localStorage.getItem("username"), config
	).success(function(response) {
		$scope.userprofile = response;
		console.log(response);
//		jsonData = msDates(jsonData.data.pricelist);
//		jsonData = toArray(jsonData);
//		////console.log(jsonData[0]);
//		$scope.marketData = jsonData;
//		createChart($scope.marketData, name, symbol);
//		$scope.tables = $scope.ToolMakeTablesModel(response.trades);
//		console.log($scope.tables);
//		var arr = [];
//		for (var i = 0; i < $scope.tables.length; i++){
//		//console.log($scope.tables[i]);
//		arr.push([$scope.tables[i].title, $scope.tables[i].percentage]);
//		}
//		console.log(arr);
//		$scope.ideas = arr;
//		$scope.limitedIdeas = limitToFilter($scope.ideas, $scope.ideas.length);
		//console.log($scope.userprofile);
	}).error(function(response) {
		localStorage.removeItem("auth") ;
		window.location = "./login.html";
	});

	/**
	 * portfolio history
	 */
	$http.get($scope.host+"/portfolio/history/details?username=", config
	).success(function(response) {
		$scope.userHistory = response;

		if ($scope.userHistory.length == 0){
			$scope.userHistory = getDummy();
			//console.log("got here");
		}
		var name = 'Portfolio';
		var symbol = 'Net';
		var jsonData = $scope.userHistory;
		//$scope.indiceData = jsonData;
		console.log(jsonData);
		//var name = jsonData.name;
		//var symbol = jsonData.data.stock_id;
		var data = [];
		msDates(jsonData);
		for (var i = 0; i < jsonData.length; i++){
			data.push([jsonData[i].portfolioDate, jsonData[i].netAssetValue]);
		}
		console.log(data);
		$scope.userHistory = data;
		createChart($scope.userHistory, name, symbol);
		//console.log($scope.userHistory);
	}).error(function(response) {
		localStorage.removeItem("auth") ;
		window.location = "./login.html";
	});

	/**
	 * market data
	 */
	$http.get($scope.host+"/marketdata/get/all/latest", config
			//$http.get($scope.host+"/marketdata/get/historical?stock="+stock, config
	).success(function(response) {
		//$scope.marketData = response;
		//console.log($scope.marketData)
		var list = response;
		var indexData = [];
		var marketData = [];
		//console.log(response.stocklist.length)



		for (var i = 0; i < list.stocklist.length; i++){
			//console.log(list.stocklist[i].data.delta);
			//console.log(((Math.round((list.stocklist[i].data.delta*100)*100))/100));
			marketData.push(list.stocklist[i]);
			if (list.stocklist[i].type == 'index'){
				indexData.push(list.stocklist[i]);
			}
		}



		//console.log(indexData);
		$scope.marketData = marketData;
		console.log($scope.marketData);
		$scope.indexData = indexData;

		indexData.forEach(function(obj, index, array){
			obj.data.up = (obj.data.delta >= 0);
			obj.data.delta *= 10000;
			obj.data.delta = Math.round(obj.data.delta);
			obj.data.delta = Math.abs(obj.data.delta);
			obj.data.delta /= 100;
		});
		//console.log($scope.indexData);
	}).error(function(response) {
		//console.log(response);
	});

	/**
	 * dummy data iff no portfolio history data present
	 */
	function getDummy(){
		var noData = [
		              {
		            	  "portfolioId": 3,
		            	  "userId": 4,
		            	  "portfolioDate": "2015-10-15 23:10:00",
		            	  "netAssetValue": 990823.40,
		            	  "cash": 9800000.40,
		            	  "portfolio": 10823.00
		              },
		              {
		            	  "portfolioId": 4,
		            	  "userId": 4,
		            	  "portfolioDate": "2015-10-14 23:10:00",
		            	  "netAssetValue": 959823.40,
		            	  "cash": 9500000.40,
		            	  "portfolio": 9823.00
		              },
		              {
		            	  "portfolioId": 1,
		            	  "userId": 4,
		            	  "portfolioDate": "2015-10-13 23:10:00",
		            	  "netAssetValue": 990823.40,
		            	  "cash": 9800000.40,
		            	  "portfolio": 10823.00
		              },
		              {
		            	  "portfolioId": 2,
		            	  "userId": 4,
		            	  "portfolioDate": "2015-10-12 23:10:00",
		            	  "netAssetValue": 1002823.40,
		            	  "cash": 9900000.40,
		            	  "portfolio": 12823.00
		              },
		              {
		            	  "portfolioId": 5,
		            	  "userId": 4,
		            	  "portfolioDate": "2015-10-11 23:10:00",
		            	  "netAssetValue": 9513823.40,
		            	  "cash": 9400000.40,
		            	  "portfolio": 13823.00
		              },
		              {
		            	  "portfolioId": 6,
		            	  "userId": 4,
		            	  "portfolioDate": "2015-10-10 23:10:00",
		            	  "netAssetValue": 9411823.40,
		            	  "cash": 9300000.40,
		            	  "portfolio": 11823.00
		              }
		              ];
		return noData;
	}

	/**
	 * market data 
	 */
	var stock = "AAPL";

	//$http.get($scope.host+"/marketdata/get/all/latest", config
	$http.get($scope.host+"/marketdata/get/historical?stock="+stock, config
	).success(function(response) {
		var jsonData = response;
		$scope.indiceData = jsonData;
		//console.log(jsonData);
		var name = jsonData.name;
		var symbol = jsonData.data.stock_id;
		//jsonData = msDates(jsonData.data.pricelist);
		//jsonData = toArray(jsonData);
		////console.log(jsonData[0]);
		//$scope.marketData = jsonData;
		//createChart($scope.marketData, name, symbol);
		////console.log($scope.marketData);


	}).error(function(response) {
		//console.log(response);
	});

	$scope.logout = function(){
		localStorage.removeItem("auth") ;
		window.location = "./logout.html";
	};


	$scope.ToolMakeTablesModel = function(srcData){
		// Parameters:
		// -----------
		// 1. srcData:  response.trades

		// Returns:
		// 1. resTables:  Should be assigned to $scope.tables

		function translateType(orig) {
			// Parameters:
			// -----------
			// 1. orig:   The type from the server

			// Return:
			// 1. The type to be shown on the page

			var dictTypeToName = {
					'stock': 'Equities',
					'commodities': 'Commidities'
			};
			if (orig in dictTypeToName){
				return dictTypeToName[orig];
			}
			else {
				return 'Unknown Type';
			}
		};

		var resTables = [];
		var resTypeIndicesList = {};  // Reverse index table
		var resTypePercentage = {};   // The percentage of each type
		var totalValue = 0.0;

		// Make the type-index list
		// Sum the total value of each type and all type
		// ** Use forEach since srcData is an array
		srcData.forEach(function(obj, index, array){
			var resType = translateType(obj.type);
			var tempValue = obj.amount * obj.tradedPrice; 
			totalValue += tempValue;
			if (! (resType in resTypeIndicesList)) {
				resTypeIndicesList[resType] = [index];
				resTypePercentage[resType] = tempValue;
			}
			else{
				resTypeIndicesList[resType].push(index);
				resTypePercentage[resType] += tempValue;
			}
		});

		// Calculate the percentage of each type
		for (key in resTypePercentage) {
			// if (arrayHasOwnIndex(resTypePercentage, key)) {
			resTypePercentage[key] = Math.round((resTypePercentage[key] / totalValue) * 10000) / 100;
			console.log(resTypePercentage[key]);
			// }
		}

		// Make the resTables according to the type
		// Use the for loop, since resTypeIndicesList is an object
		for (key in resTypeIndicesList) {
			// if (arrayHasOwnIndex(resTypeIndicesList, key)) {
			resTables.push({
				title: key,
				percentage: resTypePercentage[key],
				data: []
			});
			resTypeIndicesList[key].forEach(function(theIndex, i, array){
				resTables[resTables.length - 1].data.push(srcData[theIndex])
			});
			// }
		};

		return resTables;
	}; // End of $scope.ToolMakeTablesModel

   
  $http.get($scope.host+"/game/gamer/profile", config
  ).success(function(response) {
    $scope.gamer = response;
      console.log('gamer');
      console.log(response);
    }).error(function(response) {

 });
  
});

function msDates(data){
	//console.log(data[0].datetime);
	for (var i = 0; i < data.length; i++){
		var d = new Date(data[i].portfolioDate);
		data[i].portfolioDate = d.getTime();	
	}
	return data;
}
function toArray(data){
	var result = [];
	for (var i = 0; i < data.length; i++){
		result.push([data[i].datetime, data[i].price]);
	}
	//console.log(result);
	return result;
}


app.directive('hcPie', function () {
	return {
		restrict: 'C',
		replace: true,
		scope: {
			items: '='
		},
		controller: function ($scope, $element, $attrs) {
			//console.log(2);

		},
		template: '<div id="container" style="margin: 0 auto">not working</div>',
		link: function (scope, element, attrs) {
			// console.log(3);
			Highcharts.setOptions({
				colors: ['#5476C2', '#55A9A3', '#F7634F', '#B88BE0', '#B0ABA6', '#FCD64D', '#8FE894', '#6AF9C4']
			});
			var chart = new Highcharts.Chart({
				chart: {
					renderTo: 'container',
					plotBackgroundColor: null,
					plotBorderWidth: null,
					plotShadow: false,
					type: 'pie',
					height: 200
				},
				title: {
					text: 'Distribution'
				},
				tooltip: {
					pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>',
					percentageDecimals: 1
				},
				plotOptions: {
					pie: {
						allowPointSelect: true,
						cursor: 'pointer',
						dataLabels: {
							enabled: false,
							color: '#000000',
							connectorColor: '#000000',
							formatter: function () {
								return '<b>' + this.point.name + '</b>: ' + parseFloat(Math.round(this.percentage * 100) / 100).toFixed(2)  + ' %';
							}
						}
					}
				},
				series: [{
					type: 'pie',
					name: 'Portfolio share',
					data: scope.items
				}]
			});
			scope.$watch("items", function (newValue) {
				chart.series[0].setData(newValue, true);
			}, true);

		}
	}
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
	  var tempNum = 1;
	  // if (Math.random() > 0.5)
	  //   tempNum = 0;
	  var tempUrl = './portfolioHist'+ tempNum +'.json'
	  http.get(tempUrl, config).success(function(response){
	  // http.get(scope.host+"/portfolio/history/details", config).success(function(response){
	    // Parse the data
	    var result = [];
	    var listSrcData = response;
	    var dictNameToIndex = {'netAssetValue':0, 'cash':1, 'portfolio':2};

	    if (listSrcData.length === 0) {
	      console.error('No portfolio history data given by the server.');
	    }
	    else {
	      parseDataAndRedrawChart();
	    }

	    function parseDataAndRedrawChart() {
	      // {
	      //   "portfolioId": 1,
	      //   "userId": 4,
	      //   "portfolioDate": "2015-10-13 23:10:00",
	      //   "netAssetValue": 990823.40,
	      //   "cash": 9800000.40,
	      //   "portfolio": 10823.00
	      // }

	      for (key in dictNameToIndex) {
	        result.push({
	          name: key,
	          data: []
	        });
	      }

	      // Make the result data
	      listSrcData.forEach(function(obj, index, array){
	        for (key in obj){
	          if (key in dictNameToIndex) {
	            result[dictNameToIndex[key]].data.push([ ToolStringToDate(obj.portfolioDate), obj[key] ]);
	          }
	        }
	      });

	      // Update the chart
	      result.forEach(function(val, index, array){
	        // Sort the data before given to highstock
	        val.data.sort(function(a,b){
	          if (a[0] < b[0]) {
	            return -1;
	          }
	          if (a[0] > b[0]) {
	            return 1;
	          }
	          return 0;
	        });
	        // Refresh chart data
	        scope.chart.series[index].setData(val.data, false);
	        scope.chart.series[index].name = val.name;
	      });
	      scope.chart.redraw();

	    }

	  }).error(function(response){
	    console.error('Failed to fetch data for the portfolio history update.')
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
	    controller: function ($scope, $element, $attrs, $http, $timeout) {
	      $scope.seriesOptions = [{ name: 'AAPL', 
	        data: [
	        /* Jun 2008 */
	        [1214179200000,24.74],
	        [1214265600000,24.75],
	        [1214352000000,25.34],
	        [1214438400000,24.04],
	        [1214524800000,24.30],
	        [1214784000000,23.92]
	        ]
	      }];
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
	      
	    }
	  }
	});

$(function () {

	$(document).ready(function () {
		Highcharts.setOptions({
			global: {
				useUTC: false
			}
		});

		$('#container1').highcharts({
			chart: {
				type: 'spline',
				animation: Highcharts.svg, // don't animate in old IE
				marginRight: 10,
				events: {
					load: function () {

						// set up the updating of the chart each second
						var series = this.series[0];
						setInterval(function () {
							var x = (new Date()).getTime(), // current time
							y = Math.random();
							series.addPoint([x, y], true, true);
						}, 1000);
					}
				}
			},
			title: {
				text: 'Live random data'
			},
			xAxis: {
				type: 'datetime',
				tickPixelInterval: 150
			},
			yAxis: {
				title: {
					text: 'Value'
				},
				plotLines: [{
					value: 0,
					width: 1,
					color: '#808080'
				}]
			},
			tooltip: {
				formatter: function () {
					return '<b>' + this.series.name + '</b><br/>' +
					Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
					Highcharts.numberFormat(this.y, 2);
				}
			},
			legend: {
				enabled: false
			},
			exporting: {
				enabled: false
			},
			series: [{
				name: 'Random data',
				data: (function () {
					// generate an array of random data
					var data = [],
					time = (new Date()).getTime(),
					i;

					for (i = -19; i <= 0; i += 1) {
						data.push({
							x: time + i * 1000,
							y: Math.random()
						});
					}
					return data;
				}())
			}]
		});
	});
});

function createChart(data, name, symbol) {
	//console.log("got here");
	//$.getJSON('http:www.highcharts.com/samples/data/jsonp.php?filename=aapl-c.json&callback=?', function (data1) {
	//Create the chart

	$('#stkchart').highcharts('StockChart', {


		rangeSelector : {
			selected : 1
		},

		title : {
			text : name+' History'
		},

		navigator : {
			enabled : false
		},

		series : [{
			name : symbol,
			data : data,
			tooltip: {
				valueDecimals: 2
			}
		}]
	});
	//});

};
$('#markPanel').click(function() {
	window.location.href = './trading.html';
});
$('#portPanel').click(function() {
	window.location.href = './portfolio.html';
});
$('#learningPanel').click(function() {
	window.location.href = './learning.html';
});
$('#leaderboardsPanel').click(function() {
	window.location.href = './profile.html';
});
$('#connectionsPanel').click(function() {
	window.location.href = './connections.html';
});


