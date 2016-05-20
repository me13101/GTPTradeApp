library('RODBC')
library('jsonlite')
library('httr')



DataSource = function(no_of_ar_param,no_of_ma_param,ar_params,ma_params,no_of_days){
  market_data <- arima.sim(n = no_of_days,list(order = c(no_of_ar_param,0,no_of_ma_param), ar = ar_params, ma = ma_params));
  return(market_data);                      
}

  AAPL=DataSource(3,2,c(0.4,0.2,0.3),c(0.6,0.3),30)+100;
  GSPC=DataSource(3,2,c(0.3,0.4,0.1),c(0.3,0.4),30)+2000;
  
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
  )
  stockname = c(
    'Exxon Mobil Corp',
    'Dow Chemical Co',
    'Sterling Constructn',
    'Tetra Pak Ltd',
    'FIAT Chrysler Auto',
    'Parmalat',
    'Johnson & Johnson',
    'Kite Pharma',
    'Tesco PLC',
    'Mediaset',
    'Tui',
    'Telecom Italia',
    'Irish Water Ltd',
    'UBS AG',
    'Assicurazioni Gen',
    'Immobiliere Dass',
    'Aviva',
    'Apple Inc',
    'Eni',
    'Huntsman Corp',
    'Building Cnstrct',
    'Singapore Airlines',
    'Continental',
    'Nestle',
    'Procter & Gamble',
    'Sage Therapeutics',
    'Luxottica',
    'Facebook Inc',
    'Allegiant Travel',
    'Sunrise Telecom',
    'Gamesa Corp Tech',
    'Bank of America Corp',
    'Allianz',
    'Towers Watson & Co',
    'Aquila Resources',
    'Int Business Mach'
  )
  
  industry = c(
    'Oil & Gas',
    'Chemicals',
    'Construction & Materials',
    'Industrial Goods and Services',
    'Automobiles & Parts',
    'Food & Beverage',
    'Personal & Household Goods',
    'Health Care',
    'Retail',
    'Media',
    'Travel & Leisure',
    'Telecomunications',
    'Utilities',
    'Banks',
    'Insurance',
    'Real Estate',
    'Financial Services',
    'Technology',
    'Oil & Gas',
    'Chemicals',
    'Construction & Materials',
    'Industrial Goods and Services',
    'Automobiles & Parts',
    'Food & Beverage',
    'Personal & Household Goods',
    'Health Care',
    'Retail',
    'Media',
    'Travel & Leisure',
    'Telecomunications',
    'Utilities',
    'Banks',
    'Insurance',
    'Real Estate',
    'Financial Services',
    'Technology',
  )

    
  region = c(
    'North America',
    'North America',
    'South America',
    'North America',
    'Europe',
    'Europe',
    'North America',
    'APAC',
    'Europe',
    'Europe',
    'Europe',
    'Europe',
    'Europe',
    'Europe',
    'Europe',
    'APAC',
    'North America',
    'North America',
    'Europe',
    'South America',
    'South America',
    'APAC',
    'Europe',
    'North America',
    'North America',
    'APAC',
    'Europe',
    'North America',
    'Europe',
    'North America',
    'South America',
    'North America',
    'Europe',
    'North America',
    'APAC',
    'North America'
  )
  today=format(Sys.time(), "%Y-%m-%d %X");
  WedData = c('AAPL',AAPL[1])
  con <- odbcConnect("AnalyticConn", uid = "sa", pwd = "Password1")
  sqlQuery(con, sprintf("INSERT INTO current_price_data VALUES('%s', %f)", WedData[1],round(as.numeric(WedData[2]), digits = 2))) 
  
  
  
  
  
  
  