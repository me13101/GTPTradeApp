library('RODBC')
library('TTR')
con <- odbcConnect("AnalyticConn", uid = "prodService", pwd = "gtpprod#1234")

db = '[MarketData].[dbo].[historical_price_data]' #historical price table for running analysis on
dailyDb = '[MarketData].[dbo].[daily_price_data]'
analyticDb = '[MarketData].[dbo].[technical_table_historical]' #table contains technical analysis for graphs
analyticDb2 = '[MarketData].[dbo].[technical_table2]' #other technical analysis
analyticDb3 = '[MarketData].[dbo].[technical_daily_table]' #other technical analysis

#Get list of assets (for historical price table)
listAssets <- function(con, db){
  list = sqlQuery(con, sprintf("SELECT DISTINCT [stock_id] FROM %s", db))
  return(list[,])
}

getListDates <- function(con, db) {
  num = length(listAssets(con,db))
  query = sprintf('SELECT [datetime] FROM %s GROUP BY [datetime] HAVING count(*) = %s ORDER BY [datetime] ASC',db,num)
  table = sqlQuery(con,query)[,1]
  return(table)
  #return(sqlQuery(con, sprintf("SELECT DISTINCT [datetime] FROM %s ORDER BY [datetime] ASC",db))[,1])
}

#Simple Moving Average

#calculates all n-day SMA values for a specific stock, given a value for n
smaFunction <- function(con, db, ticker, days) {
  assetPrices = sqlQuery(con, sprintf("SELECT [stock_id], [datetime], [price] FROM %s WHERE [stock_id] = '%s' ORDER BY [datetime]",db, ticker))
  smaTable = setNames(data.frame(assetPrices[,2], SMA(assetPrices[,3], n = days)),c("DateTime","Price"))
  return(smaTable)
}

#Given a value n, specific date, and stock_id/ticker, calculates the n-day SMA
calcSmaOnDate <- function(con, db, days, date, ticker) {
  assetPrices = sqlQuery(con, sprintf("SELECT TOP %s [stock_id],[price],[datetime] FROM %s WHERE [datetime] <= '%s' AND [stock_id] = '%s' ORDER BY [datetime] DESC", days, db, date, ticker))
  sma = SMA(assetPrices[,2], n = days)[days]
  return(sma)
}

#Given a value n, specific date, calculates the n-day SMA on that date for all stocks in the historical table
allSmaOnDate <- function(con, db, days, date) {
  listAssets = listAssets(con, db)
  listSma = rep(0,length(listAssets))
  index = 1
  for (ticker in listAssets){
    listSma[index] = calcSmaOnDate(con, db, days, date, ticker)
    index = index + 1
  }
  return(listSma)
}

#Exponential Moving Average

#calculates all n-day EMA values for a specific stock, given a value for n
emaFunction <- function(con, db, ticker, days) {
  assetPrices = sqlQuery(con, sprintf("SELECT [stock_id], [datetime], [price] FROM %s WHERE [stock_id] = '%s' ORDER BY [datetime]",db, ticker))
  emaTable = setNames(data.frame(assetPrices[,2], EMA(assetPrices[,3], n = days)),c("DateTime","Price"))
  return(smaTable)
}

#Given a value n, specific date, and stock_id/ticker, calculates the n-day EMA
calcEmaOnDate <- function(con, db, days, date, ticker) {
  assetPrices = sqlQuery(con, sprintf("SELECT TOP %s [stock_id],[price],[datetime] FROM %s WHERE [datetime] <= '%s' AND [stock_id] = '%s' ORDER BY [datetime] DESC", days, db, date, ticker))
  ema = EMA(assetPrices[,2], n = days)[days]
  return(ema)
}

#Given a value n, specific date, calculates the n-day SMA on that date for all stocks in the historical table
allEmaOnDate <- function(con, db, days, date) {
  listAssets = listAssets(con, db)
  listEma = rep(0,length(listAssets))
  index = 1
  for (ticker in listAssets){
    listEma[index] = calcEmaOnDate(con, db, days, date, ticker)
    index = index + 1
  }
  return(listEma)
}

#MACD

#Calculates MACD (using 12 and 26 day EMA) and 9-day Signal Line for a specific date
calcMacdOnDate <- function(con, db, date, ticker) {
  assetPrices = sqlQuery(con, sprintf("SELECT TOP 34 [stock_id],[price],[datetime] FROM %s WHERE [datetime] <= '%s' AND [stock_id] = '%s' ORDER BY [datetime] DESC", db, date, ticker))
  macd = MACD(assetPrices[,2], nFast = 12, nSlow = 26, nSig = 9)[34,]
  return(macd)
}

#Calculates MACD for all stocks in the historical data table on a specific date
allMacdOnDate <- function(con, db, date) {
  listAssets = listAssets(con, db)
  listMACD = rep(0,length(listAssets))
  listSignal = rep(0,length(listAssets))
  index = 1
  for (ticker in listAssets){
    listMACD[index] = calcMacdOnDate(con, db, date, ticker)[1]
    listSignal[index] = calcMacdOnDate(con, db, date, ticker)[2]
    index = index + 1
  }
  macdSignal = setNames(data.frame(listMACD,listSignal),c('MACD','Signal'))
  return(macdSignal)
}

calcBollingerOnDate <- function(con, db, date, ticker) {
  assetPrices = sqlQuery(con, sprintf("SELECT TOP 20 [datetime],[stock_id],[price],[dailyhigh],[dailylow] FROM %s WHERE [datetime] <= '%s' AND [stock_id] = '%s' ORDER BY [datetime] DESC", db, date, ticker))
  HLC = cbind(assetPrices[,3],assetPrices[,4],assetPrices[,5])
  boll = BBands(HLC, n=20, sd=2)[20,]
  return(boll)
}

allBollOnDate <- function(con, db, date) {
  listAssets = listAssets(con, db)
  length = length(listAssets)
  bol_down = rep(0,length)
  bol_mid = rep(0,length)
  bol_up = rep(0,length)
  index = 1
  for (ticker in listAssets){
    boll = calcBollingerOnDate(con,db,date,ticker)
    bol_down[index] = boll[1]
    bol_mid[index] = boll[2]
    bol_up[index] = boll[3]
    index = index + 1
  }
  return(data.frame(bol_down,bol_mid,bol_up))
}

#Creates a table for a specific date of all metrics for all stocks in the historical data table
tabulateNewMetrics <- function(con, db, date) {
  assets = listAssets(con, db)
  dateCol = rep(date,length(listAssets))
  macdSignal = allMacdOnDate(con, db, date)
  boll = allBollOnDate(con, db, date)
  table = data.frame(dateCol, assets, allSmaOnDate(con, db, 12, date), allSmaOnDate(con, db, 26, date), allSmaOnDate(con, db, 50, date), allEmaOnDate(con, db, 12, date), allEmaOnDate(con, db, 26, date), allEmaOnDate(con, db, 50, date), macdSignal[1], macdSignal[2], boll[,1], boll[,2], boll[3])
  return(setNames(table,c("Date", "Ticker", "SMA-12 Day", "SMA-26 Day", "SMA-50 Day", "EMA-12 Day", "EMA-26 Day", "EMA-50 Day", "MACD", "Signal Line", "Bollinger-Low", "Bollinger-Mid", "Bollinger-Up")))
}

#Uploads table created by tabulateNewMetrics() to the specified database
uploadNewMetrics = function(con, db, date, analyticDb) {
  table = tabulateNewMetrics(con, db, date)
  for (i in 1:dim(table)[1]) {
    key = paste(substr(date,1,4),substr(date,6,7),substr(date,9,10),table[i,2],sep='')
    query = sprintf("INSERT INTO %s VALUES ('%s','%s','%s',%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)", analyticDb, key, date, table[i,2],table[i,3],table[i,4],table[i,5],table[i,6],table[i,7],table[i,8],table[i,9],table[i,10],table[i,11], table[i,12], table[i,13])
    print(query)
    sqlQuery(con, query)
  }  
}

#Creates a table for a specific date of all metrics for all stocks in the historical data table
tabulateDailyMetrics <- function(con, db, date) {
  assets = listAssets(con, db)
  dateCol = rep(date,length(listAssets))
  macdSignal = allMacdOnDate(con, db, date)
  table = data.frame(dateCol, assets, allSmaOnDate(con, db, 12, date), allSmaOnDate(con, db, 26, date), allSmaOnDate(con, db, 50, date), allEmaOnDate(con, db, 12, date), allEmaOnDate(con, db, 26, date), allEmaOnDate(con, db, 50, date), macdSignal[1], macdSignal[2])
  return(setNames(table,c("Date", "Ticker", "SMA-12 Day", "SMA-26 Day", "SMA-50 Day", "EMA-12 Day", "EMA-26 Day", "EMA-50 Day", "MACD", "Signal Line")))
}

#Uploads table created by tabulateNewMetrics() to the specified database
uploadDailyMetrics = function(con, db, date, analyticDb) {
  table = tabulateDailyMetrics(con, db, date)
  listKeys = sqlQuery(con, sprintf("SELECT [t3_key] FROM %s", analyticDb))
  for (i in 1:dim(table)[1]) {
    key = paste(substr(date,12,16),table[i,2],sep='')
    query = sprintf("DELETE FROM %s WHERE [t3_key] = '%s'", analyticDb, key)
    sqlQuery(con, query)
    query1 = sprintf("INSERT INTO %s VALUES ('%s','%s','%s',%s,%s,%s,%s,%s,%s,%s,%s)", analyticDb, key, date, table[i,2],table[i,3],table[i,4],table[i,5],table[i,6],table[i,7],table[i,8],table[i,9],table[i,10])
    sqlQuery(con, query1)
  }  
}

#For a given stock and index, calculates beta, % return, and standard deviation for the stock using all historical prices
findBetaReturnSd = function(con, db, stock_id, index_id, date) {
  stock = sqlQuery(con,sprintf("SELECT TOP 365 [stock_id],[price],[datetime],[dailyhigh],[dailylow] FROM %s WHERE [stock_id] = '%s' AND [datetime] < '%s' ORDER BY [datetime]",db,stock_id,date))
  index = sqlQuery(con,sprintf("SELECT TOP 365 [stock_id],[price],[datetime],[dailyhigh],[dailylow] FROM %s WHERE [stock_id] = '%s' AND [datetime] < '%s' ORDER BY [datetime]",db,index_id,date))
  
  high52 = max(stock[,4])
  low52 = min(stock[,5])
  myData<-setNames(data.frame(stock[,2],index[,2]),c('Stock','INDX'))
  
  d<-dim(myData)[1]
  j<-d-1
  Rj<-rep(0,j)
  Rm<-rep(0,j)
  
  
  for(i in 1:j){
    Rj[i]<-(myData[i+1,1]-myData[i,1])/myData[i,1]
    Rm[i]<-(myData[i+1,2]-myData[i,2])/myData[i,2]
  }
  
  myData2<-data.frame(Rm=Rm,Rj=Rj)
  myResult<-lm(Rj~Rm, data=myData2)
  myCoef<-coef(summary(myResult))
  myReturn<- (myData[d,1]-myData[1,1])/myData[1,1]
  return(setNames(data.frame(date,stock_id, myCoef[2,1], myReturn, sd(Rj), high52, low52),c('DateTime','Ticker', 'Beta','Return','SD','52 Wk High','52 Wk Low')))                                 
}

#Creates table of beta, return, and standard deviation for all stocks in the historical price table
allBetaReturnSd <- function(con, db, index_id, date) {
  listAssets = listAssets(con, db)
  numAssets = length(listAssets)
  betaReturnSd = findBetaReturnSd(con, db, listAssets[1], index_id, date)
  if (numAssets > 1) {
    for (i in 2:length(listAssets)){
      betaReturnSd = rbind(betaReturnSd, findBetaReturnSd(con, db, listAssets[i], index_id, date))
    }
  }
  return(betaReturnSd)
}

#Uploads table created from allBetaReturnSd to specified database
uploadBetaReturnSd = function(con, db, date, index_id, analyticDb2) {
  table = allBetaReturnSd(con, db, index_id, date)
  sqlQuery(con, sprintf("TRUNCATE TABLE %s", analyticDb2))
  for (i in 1:dim(table)[1]) {
    key = paste(substr(table[i,1],1,4),substr(table[i,1],6,7),substr(table[i,1],9,10),table[i,2],sep='')
    query = sprintf("INSERT INTO %s VALUES ('%s','%s','%s',%s,%s,%s,%s,%s)", analyticDb2, key, table[i,1], table[i,2],table[i,3],table[i,4],table[i,5],table[i,6],table[i,7])
    sqlQuery(con, query)
  }  
}
