# ANALYTICS - ENDPOINT DOCUMENTATION
------
Welcome. Here you may find the list of endpoints provided by the Analytics Group: we hope to clarify the endpoint JSON formats as well as how you call our endpoints. 
If anything makes no sense, please ask Waleed or Cathy. 

# IP address
#### dev:
192.168.23.43:8080
#### prod:
192.168.23.1:8090

You must be connected to the GTP_LAN_NETWORK to call the endpoints.

# List of Endpoints
------

  * /stockprice
  * /pricelist
  * /pricehistory
  * /analytics/SMA
  * /analytics/EMA
  * /analytics/MACD
  * /analytics/signal
  * /analytics/bollinger
  * /analytics/beta
  * /analytics/per_return
  * /analytics/sd
  * /analytics/highlowr
  * /analytics/stockmetrics



If you call the endpoint wrong, you will receive a 500 Internal Server Error.

# Endpoint Usage
#### /stockprice (GET):
**Description:**
Provides the current price for one specified stock.

**Parameters:**
```
stock - set to desired stock_id
```

**Example call:**
192.168.23.1:8090/stockprice?stock='AAPL'

**Successful JSON format response:**

```
{
"stocklist": [
  {
    "data": {
      "price": 117.66,
      "stock_id": "AAPL",
      "delta": "-0.445698",
      "datetime": "2015-10-20 12:25:00"
  },
  "type": "stock",
  "name": "Apple Inc"
  }
]
}
```

####/pricelist (GET):
**Description**:
Returns the current price of all 36 stocks and 5 indices.

**Parameters:**
```
This endpoint has no parameters 
```

**Example call:**
192.168.23.1:8090/pricelist

**Successful JSON response format:**

```
{
"stocklist": [
{
"data": {
"price": 9.55,
"stock_id": "STRL",
"delta": "4.539823",
"datetime": "2015-10-20 12:25:00"
},
"type": "stock",
"name": "Sterling Constructn"
},
{
"data": {
"price": 23.75,
"stock_id": "TTEK",
"delta": "0.397854",
"datetime": "2015-10-20 12:25:00"
},
"type": "stock",
"name": "Tetra Pak Ltd"
},
{
"data": {
"price": 9.91,
"stock_id": "PLT",
"delta": "0.291925",
"datetime": "2015-10-20 12:25:00"
},
"type": "stock",
"name": "Parmalat"
}, .......... and so on
```

####/pricehistory (GET):

**Description**:
Returns prices of one specified stock (price values every five minutes for the past 24 hours)

**Parameters:**
```
stock - set to desired stock id
```

**Example call:**
Example request: 192.168.23.1:8090/pricehistory?stock='AAPL'

**Successful JSON format response:**

```
{
"data": {
"stock_id": "AAPL",
"pricelist": [
{
"datetime": "2015-10-19 13:10:43",
"price": 49.25
},
{
"datetime": "2015-10-19 15:23:53",
"price": 44.48
},
{
"datetime": "2015-10-19 15:26:55",
"price": 56.89
},  
........
]
},
"name": "Apple Inc"
}
```

#### /analytics/SMA (GET): 
**Description**:
This endpoint returns the Simple Moving Average for the specified stock. Please ask Matt if you have any questions about how SMA works. 

**Parameters:**
```
This endpoint accepts three parameters - they are stock, days and interval. 

  stock - set to desired stock id

  days - must be 12, 26, or 50

  interval - Either 1 or 52. 1 Returns the last 24 hours and 52 returns the last year. 
```

**Example Request:**
192.168.23.1:8090/analytics/SMA?stock='AAPL'&days=26&interval=1

**Successful JSON format response:**

```
{
"metric": "Simple Moving Average",
"valuelist": [
{
"datetime": "2015-09-30 00:00:00",
"value": 97.1084615384615
},
{
"datetime": "2015-09-30 00:00:00",
"value": 97.1084615384615
}
],
"stock_id": "AAPL"
}
```

#### /analytics/EMA (GET):

**Description:**This endpoint returns the Exponential Moving Average for the specified stock. Please ask Matt if you have any questions about how EMA works.

**Parameters:** 
```
This endpoint accepts three parameters - they are stock, days and interval. 

  stock - set to desired stock id

  days - must be 12, 26, or 50

  interval - Either 1 or 52. 1 Returns the last 24 hours and 52 returns the last year. 
```

**Example Request:**
192.168.23.1:8090/analytics/EMA?stock='AAPL'&days=12&interval=1

**Successful JSON Response Format:**

```
{
"metric": "Exponential Moving Average",
"valuelist": [
{
"datetime": "2015-09-30 00:00:00",
"value": 95.9483333333333
},
{
"datetime": "2015-09-30 00:00:00",
"value": 95.9483333333333
}
],
"stock_id": "AAPL"
}
```

#### /analytics/MACD (GET):
**Description:** 
This endpoint returns the Moving Average Convergence Divergence for the specified stock id. Please ask Matt if you have any confusion about how MACD works. 

**Parameters:** 
```
This endpoint accepts three parameters - they are stock, days and interval. 

  stock - set to desired stock id

  days - not used. any value is accepted.

  interval - Either 1 or 52. 1 Returns the last 24 hours and 52 returns the last year. 
```

**Example Request:**
192.168.23.1:8090/analytics/MACD?stock='AAPL'&days=12&interval=1

**Successful JSON Response Format:**

```
{
"metric": "Moving Average Convergence Divergence",
"valuelist": [
{
"datetime": "2015-09-30 00:00:00",
"value": 0.807353012665657
},
{
"datetime": "2015-09-30 00:00:00",
"value": 0.807353012665657
}
],
"stock_id": "AAPL"
}
```

#### /analytics/signal (GET):
**Description:** 
This endpoint returns the Signal Line for a specified stock id. 

**Parameters:** 
```
This endpoint accepts three parameters - they are stock, days and interval. 

  stock - set to desired stock id

  days - not used. any value is accepted.

  interval - Either 1 or 52. 1 Returns the last 24 hours and 52 returns the last year. 
```

**Example Request:**
192.168.23.1:8090/analytics/signal?stock='AAPL'&days=12&interval=1

**Successful JSON Response Format:**

```
{
"metric": "Signal Line",
"valuelist": [
{
"datetime": "2015-09-30 00:00:00",
"value": 1.47201276070027
},
{
"datetime": "2015-09-30 00:00:00",
"value": 1.47201276070027
}
],
"stock_id": "AAPL"
}
```

#### /analytics/bollinger (GET):

**Description:**
This endpoint returns the Bollinger Bands for a specified stock id. 

**Parameters:** 
```
This endpoint accepts three parameters - they are stock, days and interval. 

  stock - set to desired stock id

  days - not used. any value is accepted.

  interval - not used. any value is accepted. 
```

**Example Request:**
192.168.23.1:8090/analytics/bollinger?stock='AAPL'&days=12&interval=1

note: note not fully implemented yet - all values are set to 0

**Successful JSON Response Format:**

```
{
"metric": "Bollinger Bands",
"valuelist": [
{
"datetime": "2015-09-30 00:00:00",
"value": 0
},
{
"datetime": "2015-09-30 00:00:00",
"value": 0
}
],
"stock_id": "AAPL"
}
```

#### /analytics/beta (GET):
**Description:**
Returns beta for a particular stock over a 52 weeks period

**Parameters:** 
```
This endpoint accepts three parameters - they are stock and interval. 

  stock - set to desired stock id

  days - not used. any value is accepted.

  interval - not used. any value is accepted. 
```

**Example call:**
192.168.23.1:8090/analytics/beta?stock='AAPL'&days=12&interval=1

**Successful JSON format response:**

```
{
"stock_id": "AAPL",
"valuelist": [
{
"datetime": "2015-10-21 00:00:00",
"beta": 1
}
],
"metric": "Beta"
}
```

#### /analytics/per_return (GET):
**Description:**
Returns the percentage return per stock over 52 weeks

**Parameters:** 
```
This endpoint accepts three parameters - they are stock, days and interval. 

  stock - set to desired stock id

  days - not used. any value is accepted.

  interval - not used. any value is accepted.
```

**Example call:**
192.168.23.1:8090/analytics/per_return?stock='AAPL'&days=12&interval=1

**Successful JSON format response:**

```
{
"stock_id": "AAPL",
"valuelist": [
{
"datetime": "2015-10-21 00:00:00",
"per_return": 0.113506658211795
}
],
"metric": "% Return"
}
```

#### /analytics/sd (GET):
**Description:**
Returns the standard deviation per stock over 52 weeks

**Parameters:** 
```
This endpoint accepts three parameters - they are stock, days and interval. 

  stock - set to desired stock id

  days - not used. any value is accepted.

  interval - not used. any value is accepted. 
```

**Example call:**
192.168.23.1:8090/analytics/sd?stock='AAPL'&days=12&interval=1

**Successful JSON format response:**

```
{
"stock_id": "AAPL",
"valuelist": [
{
"datetime": "2015-10-21 00:00:00",
"sd": 0.070995973410007
}
],
"metric": "Standard Deviation"
}
```

#### /analytics/highlow (GET):
**Description:**
Returns the 52 week high and 52 week low per stock

**Parameters:** 
```
This endpoint accepts three parameters - they are stock, days and interval. 

  stock - set to desired stock id

  days - not used. any value is accepted.

  interval - not used. any value is accepted.
```

**Example call:**
192.168.23.1:8090/analytics/highlow?stock='AAPL'&days=12&interval=1

**Successful JSON format response:**

```
{
"stock_id": "AAPL",
"valuelist": [
{
"low_52": 87.63,
"datetime": "2015-10-21 00:00:00",
"high_52": 131.28
}
],
"metric": "52 Week High Low"
}
```

#### /analytics/stockmetrics (GET):
**Description:**
Returns the 52 week high and low, beta, %return and standard deviation for the desired stock.
**Parameters:** 
```
This endpoint accepts three parameters - they are stock, days and interval. 

  stock - set to desired stock id

  days - not used. any value is accepted.

  interval - not used. any value is accepted.
```

**Example call:**
192.168.23.1:8090/analytics/stockmetrics?stock='AAPL'&days=12&interval=1

**Successful JSON format response:**

```
{
metric: "All",
valuelist: [
{
beta: -0.141803874875754,
per_return: -0.0126488645609883,
high_52: 146.29,
low_52: 123.04,
sd: 0.0109309423788297,
datetime: "2015-10-22 00:00:00"
}
],
stock_id: "AAPL"
}
```




# Further Documentation (to be completed)
	* database schema
	* metrics
	* endpoints (this document)



