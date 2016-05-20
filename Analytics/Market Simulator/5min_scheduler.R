###UPDATE EVERY 5 MINUTES###

library('RODBC')
library('TTR')
source("C:/Python/19OctSimulator/TechAnalysisFinal.R")
#source("W:/19OctSimulator/TechAnalysisFinal.R")

#UPDATE CURRENT PRICE TABLE FROM FORECASTED PRICE
today=format(Sys.time(), "%Y-%m-%d %X");
con <- odbcConnect("AnalyticConn", uid = "prodService", pwd = "gtpprod#1234")
index <- sqlQuery(con, sprintf("SELECT [index] FROM [index_value]"))

#Delta Calculation
old_price <- sqlQuery(con, sprintf("SELECT [price],[stock_id] FROM [current_price_data]"))
new_price <- sqlQuery(con, sprintf("SELECT [%d],[stock_id] FROM [stock_forecast]", index[1,1]))
prices <- merge(old_price, new_price)
print(prices)
delta <- vector(mode="numeric", length=41)
change <- vector(mode = "numeric", length=41)
for(i in 1:41){
  delta[i] = (prices[i,3] - prices[i,2])/prices[i,2]
  change[i] = (prices[i,3] - prices[i,2])
}
for(i in 1:41){
  sqlQuery(con, sprintf("UPDATE current_price_data SET delta=%f, price_change=%f WHERE stock_id='%s'", delta[i], change[i], prices[i,1]))
}
###


sqlQuery(con, sprintf("UPDATE current_price_data SET current_price_data.price = stock_forecast.[%d] FROM current_price_data  INNER JOIN  stock_forecast on current_price_data.stock_id = stock_forecast.stock_id;", index[1,1]))
sqlQuery(con, sprintf("UPDATE current_price_data SET datetime='%s'", today))



if (index == 72){
  index <- sqlQuery(con, sprintf("UPDATE [index_value] SET [index] = %d", 1))
}else{
  index <- sqlQuery(con, sprintf("UPDATE [index_value] SET [index] = %d", index[1,1] + 1))
}

#UPDATE DAILY PRICE TABLE FROM CURRENT PRICE
sqlQuery(con, sprintf("INSERT INTO [MarketData].[dbo].[daily_price_data] SELECT [stock_id],[price],[datetime],[region],[industry],[stock_name],[stock_trade_flag] FROM [MarketData].[dbo].[current_price_data]"))


#####TECH ANALYSIS##
uploadDailyMetrics(con, '[MarketData].[dbo].[daily_price_data]', today, '[MarketData].[dbo].[technical_daily_table]')
#sqlQuery(con, "DELETE FROM [MarketData].[dbo].[technical_daily_table] WHERE datetime in (SELECT TOP 41 [datetime] FROM [MarketData].[dbo].[daily_price_data] ORDER BY datetime ASC)")


######DELETE OLDEST 41 ROWS
sqlQuery(con, "DELETE FROM [MarketData].[dbo].[daily_price_data] WHERE datetime in (SELECT TOP 41 [datetime] FROM [MarketData].[dbo].[daily_price_data] ORDER BY datetime ASC)")

####IMPORTANT: uncomment the above line when having enough data!!! =3=