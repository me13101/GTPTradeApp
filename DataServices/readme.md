# Setting Up Environment
------
##### Coming Soon

# Deploying Changes
------
Perform
```sh
mvn package
```
and
```sh
java -jar data-services-X.x.jar --spring.profiles.active={dev,prod,uat}
```
# Details
------
##Plutos

#### dev:

[192.168.23.1:9082](192.168.23.1:9082)
#### uat:

[192.168.23.1:9081](192.168.23.1:9081)
#### prod:

[192.168.23.1:9080](192.168.23.1:9080)

Dependencies:

## Market Data 
#### prod: 

[192.168.23.1:8090](192.168.23.1:8090)

# Usage
------
### List of Endpoints

1. Authorisation
	* /oauth/token
	* /oauth/authorize
	* /token/revoke
2. User Management
	* /user/create 
	* /user/list
	* /user/id/{id} (TBD)
	* /user/delete (TBD)
	* /user/edit (TBD)
	* /user/password/reset
3. Portfolio  
	* /portfolio/amount (Deprecated)
	* /portfolio/details
	* /portfolio/history/details
4. Trades (TBD)
	* /trading/buy
	* /trading/sell
	* /trading/history
5. MarketData
    * /marketdata/get/all/latest
    * /marketdata/get/latest
    * /marketdata/get/historical
    * /marketdata/get/indicator (TBD)
    * /marketdata/get/list
6. Gamification
    * /game/gamer/{id}
    * /game/{id}/badges
7. Misc
	* /greeting
	* /
8. Docs
	* /autoconfig
	* /beans
	* /configprops
	* /dump
	* /env
	* /health
	* /info
	* /metrics
	* /mappings
	* /trace

## 1 Authorisation
### 1.1 OAuth Token Authorization:
##### Unauthorised Request
```sh
curl http://localhost:9090/
```
Unauthorized to access the resource results in following JSON:
```json
{
  "error": "unauthorized",
  "error_description": "Full authentication is required to access this resource"
}
```
##### Authorised Request
You can send request throught the following two methoes:
```sh
curl -X POST -vu DataServices:WeAreAwesome http://localhost:9090/oauth/token -H "Accept: application/json" -d "password=spring&username=roy@dummy.com&grant_type=password"
```
or
```javascript
    method: 'POST',
	headers: {
		"Authorization": "Basic " + new Buffer( "DataServices:WeAreAwesome" ).toString('base64')
	},
	parameters: {
	    "username" : username,
	    "password" : password,
	    `"grant_type" : "password"
	}
```
Successful authorization results in following JSON:
```json
{
  "access_token": "ff16372e-38a7-4e29-88c2-1fb92897f558",
  "token_type": "bearer",
  "refresh_token": "f554d386-0b0a-461b-bdb2-292831cecd57",
  "expires_in": 43199,
  "scope": "read write"
}
```
##### OAuth Refresh_Token
After the specified time period, the `access_token` will expire. Use the `refresh_token` that was returned in the original OAuth authorization to retrieve a new `access_token`:
```sh
curl -X POST -vu DataServices:WeAreAwesome 
http://localhost:9090/oauth/token 
-H "Accept: application/json" 
-d "grant_type=refresh_token&refresh_token=f554d386-0b0a-461b-bdb2-292831cecd57"
```
#### OAuth Authorization - SSL
To configure the project to run on HTTPS as shown in https://spring.io/guides/tutorials/bookmarks/[Building REST services with Spring], enable the `https` profile. 
You can do this by uncommenting the appropriate line in the application.properties file of this project. 
This will change the server port to `8443`. 
Modify the previous requests as in the following command.
```sh
curl -X POST -k -vu DataServices:WeAreAwesome 
https://localhost:8443/oauth/token 
-H "Accept: application/json" 
-d "password=spring&username=roy&grant_type=password"
```
The `-k` parameter is necessary to allow connections to SSL sites without valid certificates or the self signed certificate which is created for this project.

### 1.2 OAuth Token Authorization
##### Coming Soon

### 1.3 Logout / Destroy Token
You can send request throught the following two methoes:
```sh
curl -X POST http://localhost:9090/token/revoke -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558"
```
or

```javascript
	method: 'POST',
	headers: {
		"Application": "Bearer ff16372e-38a7-4e29-88c2-1fb92897f558"
	}
```
If the request is successful, you will see the following JSON response:
```json
{
  "code" : 1,
  "message" : "Token Removed"
}
```

## 2 User
### 2.1 Creating User
You can send request throught the following two methoes:
```sh
curl -X POST http://localhost:9090/user/create -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558 -d "SAMPLEJSON"
```
or
```javascript
		method: 'POST',
		headers: {
			"Content-Type": "application/json"
		},
		content: SAMPLE_JSON
```
SAMPLE_JSON 
```json
{
  "username": "EMAIL",
  "firstName": "FIRSTNAME",
  "lastName": "LASTNAME",
  "password": "PASSWORD"
}
```
If the request is successful, you will see the following JSON response:
```json
{
  "code" : 1,
  "message" : "User added Successfully!"
}
```
### 2.2 List of Users
You can send request throught the following two methoes:
```sh
curl http://localhost:9090/user/list -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558"
```
or
```javascript
		method: 'GET',
		headers: {
			"Content-Type": "application/json"
		}
```

If the request is successful, you will see the following JSON response:
```json
[{
  "id" : 1,
  "firstname": "Lucky",
  "lastname": "lim",
  "username": "limluc@outlook.com",
}]
```
### 2.3 User Details
You can send request throught the following two methoes:
```sh
curl http://localhost:9090/user/id/{id} -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558"
```
or
```javascript
		method: 'GET',
		headers: {
			"Content-Type": "application/json"
		}
```

If the request is successful, you will see the following JSON response:
```json
{
  "id" : 1,
  "firstname": "Lucky",
  "lastname": "lim",
  "username": "limluc@outlook.com",
}
```
### 2.4 User Delete
You can send request throught the following two methoes:
```sh
curl -X DELETE http://localhost:9090/user/delete -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558"
```
or
```javascript
		method: 'DELETE',
		headers: {
			"Content-Type": "application/json"
		}
```

If the request is successful, you will see the following JSON response:
```json
{
  "code" : 1,
  "message": "User Successfully Deleted"
}
```

### 2.5 User Forgot Password
You can send request throught the following two methoes:
```sh
curl -X POST http://localhost:9090/user/password/reset -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558 -d "email=EMAIL"
```
or
```javascript
		method: 'POST',
		headers: {
			"Content-Type": "application/json"
		},
		content: email=EMAIL
```

If the request is successful, you will see the following JSON response:
```json
{
  "code" : 1,
  "message" : "Email Has Been Sent to"
}
```

## 3 Portfolio
### 3.1 Portfolio Amount (Deprecated)
Use the `access_token` returned in the previous request to make the authorized request to the protected endpoint:
```sh
curl http://localhost:9090/portfolio/amount?username=USERNAME -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558"
```
or
```javascript
	method: 'GET',
	headers: {
		"Authorization": token
	},
	parameters: {
	    "username" : USERNAME
	}
```
If the request is successful, you will see the following JSON response:
```json
{
  "username": USERNAME,
  "amount": 10000000
}
```
### 3.2 Portfolio Details
Use the `access_token` returned in the previous request to make the authorized request to the protected endpoint:
```sh
curl http://localhost:9090/portfolio/details -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558"
```
or
```javascript
	method: 'GET',
	headers: {
		"Authorization": token
	}
```
If the request is successful, you will see the following JSON response:
```json
{
  "summary": {
    "netAssetValue": 1015229.00,
    "totalPortfolio": 15229.00,
    "delta": 0.02,
    "cash": 1000000.00
  },
  "portfolio": [
    {
      "userId": 4,
      "stockId": "TUI",
      "quantity": 50,
      "currentPrice": 39.58,
      "asset": {
        "name": "Tui",
        "assetClass": "Equity",
        "type": "stock",
        "industry": "Travel & Leisure",
        "region": "EU",
        "ticker": "TUI"
      },
      "totalAmount": 1979.00
    },
    {
      "userId": 4,
      "stockId": "UBS",
      "quantity": 1000,
      "currentPrice": 13.25,
      "asset": {
        "name": "UBS AG",
        "assetClass": "Equity",
        "type": "stock",
        "industry": "Banks",
        "region": "EU",
        "ticker": "UBS"
      },
      "totalAmount": 13250.00
    }
  ],
  "assetTypeDistribution": {
    "stock": 1.0,
    "index": 0.0
  },
  "assetClassDistribution": {
    "Equity": 1.0,
    "FixedIncome": 0.0,
    "Commodities": 0.0
  },
  "user": {
    "firstname": "Shari",
    "username": "shari@dummy.com",
    "lastname": "dream"
  }
}
```
### 3.3 Portfolio History Details
Use the `access_token` returned in the previous request to make the authorized request to the protected endpoint:
```sh
curl http://localhost:9090/portfolio/history/details -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558"
```
or
```javascript
	method: 'GET',
	headers: {
		"Authorization": token
	}
```
If the request is successful, you will see the following JSON response:
```json
[
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
]
```

## 4 Trades
### 4.1 Trading Buy
Use the `access_token` returned in the previous request to make the authorized request to the protected endpoint:
```sh
curl -X GET http://localhost:9090/trading/buy -d "{"userId": 4, "ticker": "UBS", "qty":10}" -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558"
```

If the request is successful, you will see the following JSON response:
```json
{
  "code": 1,
  "message": "Buy Transaction added Successfully!"
}
```
If the request failed, you will get the following JSON response:
```json
{
  "code": 0,
  "message": "User does not have enough cash for the transaction"
}
```
### 4.2 Trading Sell
Use the `access_token` returned in the previous request to make the authorized request to the protected endpoint:
```sh
curl -X GET http://localhost:9090/trading/sell -d "{"userId": 4, "ticker": "UBS", "qty":10}" -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558"
```

If the request is successful, you will see the following JSON response:
```json
{
  "code":1,
  "message":"Sell Transaction added Successfully!",
  "info":{
    "txnId":16,
    "userId":92,
    "datetime":"2015-10-21 14:40:40",
    "ticker":"UBS",
    "qty":-10002,
    "price":11.5,
    "total":-115023.0
  }
}
```
If the request failed, you will get the following JSON response:
```json
{
  "code": 0,
  "message": "User does not have enough stocks for the transaction"
}
```
### 4.3 Trading HISTORY
Use the `access_token` returned in the previous request to make the authorized request to the protected endpoint:
```sh
curl -X GET http://localhost:9090/trading/history -d "limit=50" -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558"
```
Note: By default is 50 most recent trades
If the request is successful, you will see the following JSON response:
```json
{
  "code":1,
  "message":"Sell Transaction added Successfully!",
  "info":{
    "txnId":16,
    "userId":92,
    "datetime":"2015-10-21 14:40:40",
    "ticker":"UBS",
    "qty":-10002,
    "price":11.5,
    "total":-115023.0
  }
}
```
If the request failed, you will get the following JSON response:
```json
{
  "code": 0,
  "message": "User does not have enough stocks for the transaction"
}
```

## 5 Market Data
### 5.1 Market Data Current Price
Use the `access_token` returned in the previous request to make the authorized request to the protected endpoint:
```sh
curl -X GET http://localhost:9090/marketdata/get/latest -d "stock=STOCK" -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558"
```
If the request is successful, you will see the following JSON response:
```json
{
  "stocklist": [
    {
      "name": "Apple Inc",
      "type": "stock",
      "data": {
        "delta": "0.018444",
        "stock_id": "AAPL",
        "price": 115.96,
        "datetime": "2015-10-20 14:55:00"
      }
    }
  ]
}
```

### 5.2 Market Data All Current Price
Use the `access_token` returned in the previous request to make the authorized request to the protected endpoint:
```sh
curl -X GET http://localhost:9090/marketdata/get/all/latest -d "type=[assetType]" -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558"
```
[optional]assetType = stock/index

If the request is successful, you will see the following JSON response (for assetType=stock):
```json
{
  "stocklist": [
    {
      "name": "Sterling Constructn",
      "type": "stock",
      "data": {
        "delta": "0.021417",
        "stock_id": "STRL",
        "price": 12.4,
        "datetime": "2015-10-20 15:00:00"
      }
    },
    {
      "name": "Tetra Pak Ltd",
      "type": "stock",
      "data": {
        "delta": "0.09923",
        "stock_id": "TTEK",
        "price": 24.26,
        "datetime": "2015-10-20 15:00:00"
      }
    },
   ......
   ]
}
```
### 5.3 Market Data Historical Price
Use the `access_token` returned in the previous request to make the authorized request to the protected endpoint:
```sh
curl -X GET http://localhost:9090/marketdata/get/historical -d "stock=stock" -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558"
```
If the request is successful, you will see the following JSON response:
```json
{
  "name": "Apple Inc",
  "data": {
    "stock_id": "AAPL",
    "pricelist": [
      {
        "price": 49.25,
        "datetime": "2015-10-19 13:10:43"
      },
      {
        "price": 44.48,
        "datetime": "2015-10-19 15:23:53"
      },
   ......
   ]
  }
}
```
### 5.4 Market Data Indicator Price
Use the `access_token` returned in the previous request to make the authorized request to the protected endpoint:
```sh
curl -X GET http://localhost:9090/marketdata/get/indicator/{indicator} -d "stock={STOCK}&days={DAYS}&interval={INTERVAL}" -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558"
```
If the request is successful, you will see the following JSON response:
```json
{
  "valuelist": [
    {
      "value": 95.4073333333333,
      "datetime": "2015-04-15 00:00:00"
    },
    {
      "value": 95.2493333333333,
      "datetime": "2015-04-16 00:00:00"
    },
   ......
   }
  ]
}
```

### 5.5 Market Data Stock List
Use the `access_token` returned in the previous request to make the authorized request to the protected endpoint:
```sh
curl -X GET http://localhost:9090/marketdata/get/list -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558"
```
If the request is successful, you will see the following JSON response:
```json
{
  "stocklist": [
    {
      "name": "Apple",
      "assetClass": "Equity",
      "type": "stock",
      "industry": "Technology",
      "region": "NA",
      "ticker": "AAPL"
    },
    {
      "name": "Allegiant Travel Company",
      "assetClass": "Equity",
      "type": "stock",
      "industry": "Travel & Leisure",
      "region": "EU",
      "ticker": "ALGT"
    },..........
    ]
}
```

##6 Gamification
#### 6.1 Get Gamer Profile
Use the `access_token` returned in the previous request to make the authorized request to the protected endpoint:
```sh
 curl http://localhost:9090/game/gamer/profile -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558"
```
If the request is successful, you will see the following JSON response:
```json
 {
   "user_id": 1,
   "chips": 0,
   "exp" : 0,
   "add_money" : 0
 }
```

#### 6.2 Get Gamer Badges
Use the `access_token` returned in the previous request to make the authorized request to the protected endpoint:
```sh
 curl http://localhost:9090/game/gamer/badges -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558"
```
If the request is successful, you will see the following JSON response:
```json
[
  {
     "userId": 1,
     "asset_type_id": 1,
     "level" : 1

   },
   {
      "userId": 1,
      "asset_type_id": 2,
      "level" : 3

    },
    {
         "userId": 1,
         "asset_type_id": 3,
         "level" : 1

       },
    ......
   ]
  ```
## 7 Misc
#### 7.1 Greeting
Use the `access_token` returned in the previous request to make the authorized request to the protected endpoint:
```sh
 curl http://localhost:9090/greeting -H "Authorization: Bearer ff16372e-38a7-4e29-88c2-1fb92897f558"
```
 If the request is successful, you will see the following JSON response:
```json
 {
   "id": 1,
   "content": "Hello, USERNAME!"
 }
```
