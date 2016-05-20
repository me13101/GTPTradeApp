from bottle import route, run, template, response, request, error
from json import dumps
import csv
import json
import pymssql
import string
from hashlib import sha1
import hmac
import functools
import inspect
import base64
import time

key = b'5Dd0UXAIllS1xrRgrC2i6mT8voZQ9kLO'
 
@route('/stockprice')
def index():
    data = {}
    data1 = {}
    data2 = {}
    list = []
    param = str(request.query['stock'])
    query = str("SELECT * FROM current_price_data WHERE stock_id= %s ORDER BY datetime;" %param)
    conn = pymssql.connect(server='THUNSERV', user='sa', password='Password1', database='MarketData')
    cursor = conn.cursor()
    cursor.execute(query)
    row = cursor.fetchone()
    while row:
        data["name"] = row[5].rstrip()
        data["type"] = str(row[6]).rstrip()
        data1["price"] = row[1]
        data1["datetime"] = str(row[2])
        data1["delta"] = str(row[7])
        data1["pricechange"] = str(row[8])
        data1["stock_id"] = str(row[0]).rstrip()
        data["data"] = data1
        data1 = {}
        list.append(data)
        data = {}
        row = cursor.fetchone()
    data2["stocklist"] = list
    jsonString = json.dumps(data2)
    response.content_type = 'application/json'
    jsonhash = jsonString.encode('utf-8')
    hashed = hmac.new(key, jsonhash, sha1)
    response.add_header('Authentication', 'hmac: ' + base64.b64encode(hashed.digest()).decode('utf-8'))
    return jsonString

@route('/pricelist')
def index():
    data = {}
    data1 = {}
    data2 = {}
    list = []
    query = str("SELECT * FROM current_price_data ORDER BY stock_id;")
    conn = pymssql.connect(server='THUNSERV', user='sa', password='Password1', database='MarketData')
    cursor = conn.cursor()
    cursor.execute(query)
    row = cursor.fetchone()
    while row:
        data["name"] = row[5].rstrip()
        data["type"] = str(row[6]).rstrip()
        data1["price"] = row[1]
        data1["datetime"] = str(row[2])
        data1["delta"] = str(row[7])
        data1["pricechange"] = str(row[8])
        data1["stock_id"] = str(row[0]).rstrip()
        data["data"] = data1
        data1 = {}
        list.append(data)
        data = {}
        row = cursor.fetchone()
    data2["stocklist"] = list
    jsonString = json.dumps(data2)
    response.content_type = 'application/json'
    jsonhash = jsonString.encode('utf-8')
    hashed = hmac.new(key, jsonhash, sha1)
    response.add_header('Authentication', 'hmac: ' + base64.b64encode(hashed.digest()).decode('utf-8'))
    return jsonString

@route('/pricehistory')
def index():
    data = {}
    data1 = {}
    data2 = {}
    list = []
    list2 = []
    param = str(request.query['stock'])
    query = str("SELECT * FROM daily_price_data WHERE stock_id= %s ORDER BY datetime;" %param)
    conn = pymssql.connect(server='THUNSERV', user='sa', password='Password1', database='MarketData')
    cursor = conn.cursor()
    cursor.execute(query)
    row = cursor.fetchone()
    data["name"] = row[5].rstrip()
    data1["stock_id"] = str(row[0]).rstrip()
    while row:
        data2["price"] = row[1]
        data2["datetime"] = str(row[2])
        list2.append(data2)
        data2 = {}
        row = cursor.fetchone()
    data1["pricelist"] = list2
    data["data"] = data1
    jsonString = json.dumps(data)
    response.content_type = 'application/json'
    jsonhash = jsonString.encode('utf-8')
    hashed = hmac.new(key, jsonhash, sha1)
    response.add_header('Authentication', 'hmac: ' + base64.b64encode(hashed.digest()).decode('utf-8'))
    return jsonString

@route('/analytics/SMA')
def index():
    data = {}
    data1 = {}
    list = []
    param_stock = str(request.query['stock'])
    param_days = float(request.query['days'])
    param_interval = float(request.query['interval']) 

    if param_interval == 1:
        query = str("SELECT * FROM technical_daily_table WHERE stock_id= %s ORDER BY datetime;" %param_stock)
    else:
        query = str("SELECT * FROM technical_table_historical WHERE stock_id= %s ORDER BY datetime;" %param_stock)
    conn = pymssql.connect(server='THUNSERV', user='sa', password='Password1', database='MarketData')
    cursor = conn.cursor()
    cursor.execute(query)
    row = cursor.fetchone()
    data["metric"] = "Simple Moving Average"
    data["stock_id"] = str(row[2]).rstrip()
    while row:
        if param_days == 12:
            data1["value"] = row[3]
        elif param_days == 26:
            data1["value"] = row[4]
        elif param_days == 50:
            data1["value"] = row[5]
        data1["datetime"] = str(row[1])
        list.append(data1)
        data1 = {}
        row = cursor.fetchone()
    data["valuelist"] = list
    jsonString = json.dumps(data)
    response.content_type = 'application/json'
    jsonhash = jsonString.encode('utf-8')
    hashed = hmac.new(key, jsonhash, sha1)
    response.add_header('Authentication', 'hmac: ' + base64.b64encode(hashed.digest()).decode('utf-8'))
    return jsonString

@route('/analytics/EMA')
def index():
    data = {}
    data1 = {}
    list = []
    param_stock = str(request.query['stock'])
    param_days = float(request.query['days'])
    param_interval = float(request.query['interval'])
    
    if param_interval == 1:
        query = str("SELECT * FROM technical_daily_table WHERE stock_id= %s ORDER BY datetime;" %param_stock)
    else:
        query = str("SELECT * FROM technical_table_historical WHERE stock_id= %s ORDER BY datetime;" %param_stock)
    conn = pymssql.connect(server='THUNSERV', user='sa', password='Password1', database='MarketData')
    cursor = conn.cursor()
    cursor.execute(query)
    row = cursor.fetchone()
    data["metric"] = "Exponential Moving Average"
    data["stock_id"] = str(row[2]).rstrip()
    while row:
        if param_days == 12:
            data1["value"] = row[6]
        elif param_days == 26:
            data1["value"] = row[7]
        elif param_days == 50:
            data1["value"] = row[8]
        data1["datetime"] = str(row[1])
        list.append(data1)
        data1 = {}
        row = cursor.fetchone()
    data["valuelist"] = list
    jsonString = json.dumps(data)
    response.content_type = 'application/json'
    jsonhash = jsonString.encode('utf-8')
    hashed = hmac.new(key, jsonhash, sha1)
    response.add_header('Authentication', 'hmac: ' + base64.b64encode(hashed.digest()).decode('utf-8'))
    return jsonString

@route('/analytics/MACD')
def index():
    data = {}
    data1 = {}
    list = []
    param_stock = str(request.query['stock'])
    param_days = float(request.query['days'])
    param_interval = float(request.query['interval'])
    
    if param_interval == 1:
        query = str("SELECT * FROM technical_daily_table WHERE stock_id= %s ORDER BY datetime;" %param_stock)
    else:
        query = str("SELECT * FROM technical_table_historical WHERE stock_id= %s ORDER BY datetime;" %param_stock)
    conn = pymssql.connect(server='THUNSERV', user='sa', password='Password1', database='MarketData')
    cursor = conn.cursor()
    cursor.execute(query)
    row = cursor.fetchone()
    data["metric"] = "Moving Average Convergence Divergence"
    data["stock_id"] = str(row[2]).rstrip()
    while row:
        data1["value"] = row[9]
        data1["datetime"] = str(row[1])
        list.append(data1)
        data1 = {}
        row = cursor.fetchone()
    data["valuelist"] = list
    jsonString = json.dumps(data)
    response.content_type = 'application/json'
    jsonhash = jsonString.encode('utf-8')
    hashed = hmac.new(key, jsonhash, sha1)
    response.add_header('Authentication', 'hmac: ' + base64.b64encode(hashed.digest()).decode('utf-8'))
    return jsonString

@route('/analytics/signal')
def index():
    data = {}
    data1 = {}
    list = []
    param_stock = str(request.query['stock'])
    param_days = float(request.query['days'])
    param_interval = float(request.query['interval'])
    
    if param_interval == 1:
        query = str("SELECT * FROM technical_daily_table WHERE stock_id= %s ORDER BY datetime;" %param_stock)
    else:
        query = str("SELECT * FROM technical_table_historical WHERE stock_id= %s ORDER BY datetime;" %param_stock)
    conn = pymssql.connect(server='THUNSERV', user='sa', password='Password1', database='MarketData')
    cursor = conn.cursor()
    cursor.execute(query)
    row = cursor.fetchone()
    data["metric"] = "Signal Line"
    data["stock_id"] = str(row[2]).rstrip()
    while row:
        data1["value"] = row[10]
        data1["datetime"] = str(row[1])
        list.append(data1)
        data1 = {}
        row = cursor.fetchone()
    data["valuelist"] = list
    jsonString = json.dumps(data)
    response.content_type = 'application/json'
    jsonhash = jsonString.encode('utf-8')
    hashed = hmac.new(key, jsonhash, sha1)
    response.add_header('Authentication', 'hmac: ' + base64.b64encode(hashed.digest()).decode('utf-8'))
    return jsonString

@route('/analytics/bollinger')
def index():
    data = {}
    data1 = {}
    list = []
    param_stock = str(request.query['stock'])
    param_days = float(request.query['days'])
    param_interval = float(request.query['interval'])
    
    query = str("SELECT * FROM technical_table_historical WHERE stock_id= %s ORDER BY datetime;" %param_stock)
    conn = pymssql.connect(server='THUNSERV', user='sa', password='Password1', database='MarketData')
    cursor = conn.cursor()
    cursor.execute(query)
    row = cursor.fetchone()
    data["metric"] = "Bollinger Bands"
    data["stock_id"] = str(row[2]).rstrip()
    while row:
        data1["bol_low"] = row[11]
        data1["bol_mid"] = row[12]
        data1["bol_up"] = row[13]
        data1["datetime"] = str(row[1])
        list.append(data1)
        data1 = {}
        row = cursor.fetchone()
    data["valuelist"] = list
    jsonString = json.dumps(data)
    response.content_type = 'application/json'
    jsonhash = jsonString.encode('utf-8')
    hashed = hmac.new(key, jsonhash, sha1)
    response.add_header('Authentication', 'hmac: ' + base64.b64encode(hashed.digest()).decode('utf-8'))
    return jsonString

@route('/analytics/beta')
def index():
    data = {}
    data1 = {}
    list = []
    param_stock = str(request.query['stock'])
    param_days = float(request.query['days'])
    param_interval = float(request.query['interval'])
    
    query = str("SELECT * FROM technical_table2 WHERE stock_id= %s ORDER BY datetime;" %param_stock)
    conn = pymssql.connect(server='THUNSERV', user='sa', password='Password1', database='MarketData')
    cursor = conn.cursor()
    cursor.execute(query)
    row = cursor.fetchone()
    while row:
        data["metric"] = "Beta"
        data["stock_id"] = str(row[2]).rstrip()
        data1["beta"] = row[3]
        data1["datetime"] = str(row[1])
        list.append(data1)
        data1 = {}
        row = cursor.fetchone()
    data["valuelist"] = list
    jsonString = json.dumps(data)
    response.content_type = 'application/json'
    jsonhash = jsonString.encode('utf-8')
    hashed = hmac.new(key, jsonhash, sha1)
    response.add_header('Authentication', 'hmac: ' + base64.b64encode(hashed.digest()).decode('utf-8'))
    return jsonString

@route('/analytics/per_return')
def index():
    data = {}
    data1 = {}
    list = []
    param_stock = str(request.query['stock'])
    param_days = float(request.query['days'])
    param_interval = float(request.query['interval'])
    
    query = str("SELECT * FROM technical_table2 WHERE stock_id= %s ORDER BY datetime;" %param_stock)
    conn = pymssql.connect(server='THUNSERV', user='sa', password='Password1', database='MarketData')
    cursor = conn.cursor()
    cursor.execute(query)
    row = cursor.fetchone()
    while row:
        data["metric"] = "% Return"
        data["stock_id"] = str(row[2]).rstrip()
        data1["per_return"] = row[4]
        data1["datetime"] = str(row[1])
        list.append(data1)
        data1 = {}
        row = cursor.fetchone()
    data["valuelist"] = list
    jsonString = json.dumps(data)
    response.content_type = 'application/json'
    jsonhash = jsonString.encode('utf-8')
    hashed = hmac.new(key, jsonhash, sha1)
    response.add_header('Authentication', 'hmac: ' + base64.b64encode(hashed.digest()).decode('utf-8'))
    return jsonString

@route('/analytics/sd')
def index():
    data = {}
    data1 = {}
    list = []
    param_stock = str(request.query['stock'])
    param_days = float(request.query['days'])
    param_interval = float(request.query['interval'])
    
    query = str("SELECT * FROM technical_table2 WHERE stock_id= %s ORDER BY datetime;" %param_stock)
    conn = pymssql.connect(server='THUNSERV', user='sa', password='Password1', database='MarketData')
    cursor = conn.cursor()
    cursor.execute(query)
    row = cursor.fetchone()
    while row:
        data["metric"] = "Standard Deviation"
        data["stock_id"] = str(row[2]).rstrip()
        data1["sd"] = row[5]
        data1["datetime"] = str(row[1])
        list.append(data1)
        data1 = {}
        row = cursor.fetchone()
    data["valuelist"] = list
    jsonString = json.dumps(data)
    response.content_type = 'application/json'
    jsonhash = jsonString.encode('utf-8')
    hashed = hmac.new(key, jsonhash, sha1)
    response.add_header('Authentication', 'hmac: ' + base64.b64encode(hashed.digest()).decode('utf-8'))
    return jsonString

@route('/analytics/highlow')
def index():
    data = {}
    data1 = {}
    list = []
    param_stock = str(request.query['stock'])
    param_days = float(request.query['days'])
    param_interval = float(request.query['interval'])
    
    query = str("SELECT * FROM technical_table2 WHERE stock_id= %s ORDER BY datetime;" %param_stock)
    conn = pymssql.connect(server='THUNSERV', user='sa', password='Password1', database='MarketData')
    cursor = conn.cursor()
    cursor.execute(query)
    row = cursor.fetchone()
    while row:
        data["metric"] = "52 Week High Low"
        data["stock_id"] = str(row[2]).rstrip()
        data1["high_52"] = row[6]
        data1["low_52"] = row[7]
        data1["datetime"] = str(row[1])
        list.append(data1)
        data1 = {}
        row = cursor.fetchone()
    data["valuelist"] = list
    jsonString = json.dumps(data)
    response.content_type = 'application/json'
    jsonhash = jsonString.encode('utf-8')
    hashed = hmac.new(key, jsonhash, sha1)
    response.add_header('Authentication', 'hmac: ' + base64.b64encode(hashed.digest()).decode('utf-8'))
    return jsonString

@route('/analytics/stockmetrics')
def index():
    data = {}
    data1 = {}
    list = []
    param_stock = str(request.query['stock'])
    param_days = float(request.query['days'])
    param_interval = float(request.query['interval'])
    
    query = str("SELECT * FROM technical_table2 WHERE stock_id= %s ORDER BY datetime ASC;" %param_stock)
    conn = pymssql.connect(server='THUNSERV', user='sa', password='Password1', database='MarketData')
    cursor = conn.cursor()
    cursor.execute(str("SELECT TOP 1 * FROM daily_price_data WHERE stock_id= %s AND datetime = '%s' ORDER BY datetime ASC;" % (param_stock, str(time.strftime("%Y-%m-%d 00:00:01")))))
    row = cursor.fetchone()
    data1["open"]= float(row[1])
    cursor.execute(query)
    row = cursor.fetchone()
    while row:
        data["metric"] = "All"
        data["stock_id"] = str(row[2]).rstrip()
        data1["high_52"] = row[6]
        data1["low_52"] = row[7]
        data1["sd"] = row[5]
        data1["per_return"] = row[4]
        data1["beta"] = row[3]
        data1["datetime"] = str(row[1])
        datetime = str(row[1])
        list.append(data1)
        data1 = {}
        row = cursor.fetchone()
    data["valuelist"] = list
    jsonString = json.dumps(data)
    response.content_type = 'application/json'
    jsonhash = jsonString.encode('utf-8')
    hashed = hmac.new(key, jsonhash, sha1)
    response.add_header('Authentication', 'hmac: ' + base64.b64encode(hashed.digest()).decode('utf-8'))
    return jsonString



@error(404)
def error404(error):
    return 'Nothing here, sorry'




run(host='192.168.23.1', port=8090)
