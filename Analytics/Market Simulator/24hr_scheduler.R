##RUN THIS AT 00:01!!!!!
library('RODBC')
source("C:/Python/19OctSimulator/TechAnalysisFinal.R")
#source("W:/19OctSimulator/TechAnalysisFinal.R")
###COPY THE CLOSING PRICE TO HISTORICAL DATABASE#


time_end=sprintf("%s 00:00:00.000",Sys.Date())
time_start=sprintf("%s 00:00:00.000",(Sys.Date()-1))

query = sprintf("INSERT INTO [MarketData].[dbo].[historical_price_data] ([stock_id]
      ,[price]
      ,[datetime]
      ,[region]
      ,[industry]
      ,[stock_name])

SELECT [stock_id]
      ,[price]
      ,[datetime]
      ,[region]
      ,[industry]
      ,[stock_name]
  FROM [MarketData].[dbo].[current_price_data]

UPDATE historical_price_data set [datetime]= '%s' where [datetime] between '%s 00:01:00.000' and '%s 12:00:00.000'",time_start,Sys.Date()-1,Sys.Date())


con <- odbcConnect("AnalyticConn", uid = "prodService", pwd = "gtpprod#1234")
sqlQuery(con, query)


###UPDATE DAILY HIGH&LOW TO HISTORICAL DATA EVERY 24HR
stockid=c('XOM',
          'DOW',
          'STRL',
          'TTEK',
          'FCHA',
          'PLT',
          'JNJ',
          'KITE',
          'TESO',
          'MS',
          'TUI',
          'TLIT',
          'IRWT',
          'UBS',
          'ARZGF',
          'FINA',
          'AVV',
          'AAPL',
          'ENI',
          'HUN',
          'BCA',
          'SIAL',
          'CONG',
          'NESNY',
          'PG',
          'SAGE',
          'LUX',
          'FB',
          'ALGT',
          'SRTI',
          'GAM',
          'BAC',
          'ALVG',
          'TW',
          'AQAC',
          'IBM',
          'MAInd',
          'EUInd',
          'SAInd',
          'NAInd',
          'APInd'
)


for (i in 1:(length(stockid))){
  con <- odbcConnect("AnalyticConn", uid = "prodService", pwd = "gtpprod#1234")
  daily_high=sqlQuery(con, sprintf("SELECT TOP 1 [price] FROM [MarketData].[dbo].[daily_price_data] WHERE [datetime] between '%s' AND '%s' and [stock_id] = '%s' ORDER BY [price] desc",time_start,time_end,stockid[i]))
  daily_low=sqlQuery(con, sprintf("SELECT TOP 1 [price] FROM [MarketData].[dbo].[daily_price_data] WHERE [datetime] between '%s' AND '%s' and [stock_id] = '%s' ORDER BY [price] asc",time_start,time_end,stockid[i]))
  sqlQuery(con, sprintf("UPDATE [historical_price_data] SET [dailyhigh] = %f, [dailylow]=%f WHERE [stock_id] = '%s' AND [datetime] = '%s'",as.numeric(daily_high[1,1]), as.numeric(daily_low[1,1]), stockid[i], time_start))
}

###TECHNICAL ANALYSIS###
uploadNewMetrics(con, '[MarketData].[dbo].[historical_price_data]', time_start, '[MarketData].[dbo].[technical_table_historical]')
uploadBetaReturnSd(con, '[MarketData].[dbo].[historical_price_data]', time_start, 'MAInd', '[MarketData].[dbo].[technical_table2]')