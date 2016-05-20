library('RODBC')

con <- odbcConnect("AnalyticConn", uid = "sa", pwd = "Password1")
x = sqlQuery(con,"SELECT [price],[datetime] FROM [MarketData].[dbo].[daily_price_data] where [stock_id]='ALVG' ORDER BY [datetime]")
data=c();
for (i in 1:290){
  data[i]=x[i,1]
}

plot(data, type='l')