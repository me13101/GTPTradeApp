###UPDATE EVERY 6 HOURS###


library('RODBC')
source("C:/Python/19OctSimulator/functionDennisPricingModelARMA11.R")#todo: add functionDennis...

#Update stock_forecast table every 6 hr

forecasted_data=ForecastedData();

for (i in 1:41){
  for (j in 1:72){
    con <- odbcConnect("AnalyticConn", uid = "prodService", pwd = "gtpprod#1234")
    index <- sqlQuery(con, sprintf("UPDATE [index_value] SET [index] = %d", 1))
    query=sprintf("UPDATE [MarketData].[dbo].[stock_forecast] SET [%s]=%f WHERE [stock_id]='%s'",as.character(j), round(as.numeric(forecasted_data[[i]][j+1]),digits = 2),forecasted_data[[i]][1])
    sqlQuery(con, query)
  }
}
