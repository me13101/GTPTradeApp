library('RODBC')
#library('jsonlite')
#library('httr')

tEnd=72
i=1
Arma = function(no_of_ar_param,no_of_ma_param,ar_params,ma_params,no_of_days){
  market_data <- arima.sim(n = no_of_days,list(order = c(no_of_ar_param,0,no_of_ma_param), ar = ar_params, ma = ma_params));
  return(market_data);                      
}

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



MarketData = function(tEnd, cu_val){
  #Define the number of dates that the simulation is running
  t = sample(5:(tEnd-5), 1)
  x1 <- seq(1, t)
  x2 <- seq(1, tEnd-t)
  
  #Creating random numbers to simulate the market data
  a1 <- c(sample(-5:5, 1)/100)
  b1 <- c(sample(-5:5, 1)/100)
  AAPL1=Arma(1,1,a1,b1,t)+cu_val
  a2 <- c(sample(-5:5, 1)/100)
  b2 <- c(sample(-5:5, 1)/100)
  AAPL2=Arma(1,1,a2,b2,tEnd-t)+cu_val
  
  #Trend in the Data
  if (cu_val < 5)
  {
    g <- numeric(t)+1
    h <- numeric(tEnd-t)+1
    c1 <- c(sample(1:25, 1)/10000)
    c2 <- c(sample(1:50, 1)/10000)
    g=g*(1+c1)
    h=h*(1+c2)
    d1 <- rnorm(tEnd, 0, 0.01)
    AAPL3=c(c(AAPL1)*(cumprod(g)+d1[1:t]), c(AAPL2)*(cumprod(h)+d1[(t+1):tEnd]))
    AAPL4=c(c(AAPL3[1:t]), c(AAPL3[(t+1):(tEnd)]+(AAPL3[t]-AAPL3[t+1])))
    return(AAPL4)    
  }else{
    g <- numeric(t)+1
    h <- numeric(tEnd-t)+1
    c1 <- c(sample(-50:50, 1)/10000)
    c2 <- c(sample(-50:50, 1)/10000)
    g=g*(1+c1)
    h=h*(1+c2)
    d1 <- rnorm(tEnd, 0, 0.01)
    AAPL3=c(c(AAPL1)*(cumprod(g)+d1[1:t]), c(AAPL2)*(cumprod(h)+d1[(t+1):tEnd]))
    AAPL4=c(c(AAPL3[1:t]), c(AAPL3[(t+1):(tEnd)]+(AAPL3[t]-AAPL3[t+1])))
    return(abs(AAPL4))
  }
  
}


ForecastedData=function(){
  
  con <- odbcConnect("AnalyticConn", uid = "prodService", pwd = "gtpprod#1234")
  cu_val=sqlQuery(con, "SELECT TOP 1000 [72] FROM [MarketData].[dbo].[stock_forecast]")
  
  XOM=MarketData(tEnd, cu_val[1,1])
  rXOM=c(stockid[1], XOM)
  DOW=MarketData(tEnd, cu_val[2,1])
  rDOW=c(stockid[2], DOW)
  STRL=MarketData(tEnd, cu_val[3,1])
  rSTRL=c(stockid[3], STRL)
  TTEK=MarketData(tEnd, cu_val[4,1])
  rTTEK=c(stockid[4], TTEK)
  FCHA=MarketData(tEnd, cu_val[5,1])
  rFCHA=c(stockid[5], FCHA)
  PLT=MarketData(tEnd, cu_val[6,1])
  rPLT=c(stockid[6], PLT)
  JNJ=MarketData(tEnd, cu_val[7,1])
  rJNJ=c(stockid[7], JNJ)
  KITE=MarketData(tEnd, cu_val[8,1])
  rKITE=c(stockid[8], KITE)
  TESO=MarketData(tEnd, cu_val[9,1])
  rTESO=c(stockid[9], TESO)
  MS=MarketData(tEnd, cu_val[10,1])
  rMS=c(stockid[10], MS)
  TUI=MarketData(tEnd, cu_val[11,1])
  rTUI=c(stockid[11], TUI)
  TLIT=MarketData(tEnd, cu_val[12,1])
  rTLIT=c(stockid[12], TLIT)
  IRWT=MarketData(tEnd, cu_val[13,1])
  rIRWT=c(stockid[13], IRWT)
  UBS=MarketData(tEnd, cu_val[14,1])
  rUBS=c(stockid[14], UBS)
  ARZGF=MarketData(tEnd, cu_val[15,1])
  rARZGF=c(stockid[15], ARZGF)
  FINA=MarketData(tEnd, cu_val[16,1])
  rFINA=c(stockid[16], FINA)
  AVV=MarketData(tEnd, cu_val[17,1])
  rAVV=c(stockid[17], AVV)
  AAPL=MarketData(tEnd, cu_val[18,1])
  rAAPL=c(stockid[18], AAPL)
  ENI=MarketData(tEnd, cu_val[19,1])
  rENI=c(stockid[19], ENI)
  HUN=MarketData(tEnd, cu_val[20,1])
  rHUN=c(stockid[20], HUN)
  BCA=MarketData(tEnd, cu_val[21,1])
  rBCA=c(stockid[21], BCA)
  SIAL=MarketData(tEnd, cu_val[22,1])
  rSIAL=c(stockid[22], SIAL)
  CONG=MarketData(tEnd, cu_val[23,1])
  rCONG=c(stockid[23], CONG)
  NESNY=MarketData(tEnd, cu_val[24,1])
  rNESNY=c(stockid[24], NESNY)
  PG=MarketData(tEnd, cu_val[25,1])
  rPG=c(stockid[25], PG)
  SAGE=MarketData(tEnd, cu_val[26,1])
  rSAGE=c(stockid[26], SAGE)
  LUX=MarketData(tEnd, cu_val[27,1])
  rLUX=c(stockid[27], LUX)
  FB=MarketData(tEnd, cu_val[28,1])
  rFB=c(stockid[28], FB)
  ALGT=MarketData(tEnd, cu_val[29,1])
  rALGT=c(stockid[29], ALGT)
  SRTI=MarketData(tEnd, cu_val[30,1])
  rSRTI=c(stockid[30], SRTI)
  GAM=MarketData(tEnd, cu_val[31,1])
  rGAM=c(stockid[31], GAM)
  BAC=MarketData(tEnd, cu_val[32,1])
  rBAC=c(stockid[32], BAC)
  ALVG=MarketData(tEnd, cu_val[33,1])
  rALVG=c(stockid[33], ALVG)
  TW=MarketData(tEnd, cu_val[34,1])
  rTW=c(stockid[34], TW)
  AQAC=MarketData(tEnd, cu_val[35,1])
  rAQAC=c(stockid[35], AQAC)
  IBM=MarketData(tEnd, cu_val[36,1])
  rIBM=c(stockid[36], IBM)
  
  
  #Market Index
  MaSum=c(XOM+ENI+DOW+HUN+STRL+BCA+TTEK+SIAL+FCHA+CONG+PLT+NESNY+JNJ+
            PG+KITE+SAGE+TESO+LUX+MS+FB+TUI+ALGT+TLIT+SRTI+IRWT+GAM+UBS+
            BAC+ARZGF+ALVG+FINA+TW+AVV+AQAC+AAPL+IBM)
  
  MAInd=c(XOM/MaSum*XOM+ENI/MaSum*ENI+DOW/MaSum*DOW+HUN/MaSum*HUN+STRL/MaSum*STRL+
            BCA/MaSum*BCA+TTEK/MaSum*TTEK+SIAL/MaSum*SIAL+FCHA/MaSum*FCHA+
            CONG/MaSum*CONG+PLT/MaSum*PLT+NESNY/MaSum*NESNY+
            JNJ/MaSum*JNJ+PG/MaSum*PG+KITE/MaSum*KITE+SAGE/MaSum*SAGE+TESO/MaSum*TESO+
            LUX/MaSum*LUX+MS/MaSum*MS+FB/MaSum*FB+TUI/MaSum*TUI+ALGT/MaSum*ALGT+
            TLIT/MaSum*TLIT+SRTI/MaSum*SRTI+IRWT/MaSum*IRWT+GAM/MaSum*GAM+
            UBS/MaSum*UBS+BAC/MaSum*BAC+ARZGF/MaSum*ARZGF+ALVG/MaSum*ALVG+
            FINA/MaSum*FINA+TW/MaSum*TW+AVV/MaSum*AVV+AQAC/MaSum*AQAC+AAPL/MaSum*AAPL+
            IBM/MaSum*IBM)
  rMAInd=c(stockid[37], MAInd)
  #plot(MaInd, type="l", col=5)
  
  #European Index
  EUSum=c(ENI+FCHA+CONG+PLT+TESO+LUX+MS+TUI+ALGT+TLIT+IRWT+UBS+ARZGF+ALVG)
  
  EUInd=c(ENI/EUSum*ENI+FCHA/EUSum*FCHA+
            CONG/EUSum*CONG+PLT/EUSum*PLT+TESO/EUSum*TESO+
            LUX/EUSum*LUX+MS/EUSum*MS+TUI/EUSum*TUI+ALGT/EUSum*ALGT+
            TLIT/EUSum*TLIT+IRWT/EUSum*IRWT+UBS/EUSum*UBS+ARZGF/EUSum*ARZGF+
            ALVG/EUSum*ALVG)
  rEUInd=c(stockid[38], EUInd)
  #lines(EUInd, type="l", col=6)
  
  #South-America Index
  SASum=c(HUN+STRL+BCA+GAM)
  
  SAInd=c(HUN/SASum*HUN+STRL/SASum*STRL+
            BCA/SASum*BCA+GAM/SASum*GAM)
  rSAInd=c(stockid[39], SAInd)
  #lines(SAInd, type="l", col=7)
  
  #North-America Index
  NASum=c(XOM+DOW+TTEK+NESNY+JNJ+PG+FB+SRTI+BAC+TW+AVV+AAPL+IBM)
  
  NAInd=c(XOM/NASum*XOM+DOW/NASum*DOW+TTEK/NASum*TTEK+NESNY/NASum*NESNY+
            JNJ/NASum*JNJ+PG/NASum*PG+FB/NASum*FB+SRTI/NASum*SRTI+BAC/NASum*BAC+
            TW/NASum*TW+AVV/NASum*AVV+AAPL/NASum*AAPL+IBM/NASum*IBM)
  rNAInd=c(stockid[40], NAInd)
  #lines(NAInd, type="l", col=8)
  
  #APAC
  APACSum=c(SIAL+KITE+SAGE+FINA+AQAC)
  
  APInd=c(SIAL/APACSum*SIAL+KITE/APACSum*KITE+SAGE/APACSum*SAGE+
            FINA/MaSum*FINA+AQAC/APACSum*AQAC)
  rAPInd=c(stockid[41], APInd)
  #lines(APACInd, type="l", col=9)
  
  
  forecasted_data = list(rXOM,
                         rDOW,
                         rSTRL,
                         rTTEK,
                         rFCHA,
                         rPLT,
                         rJNJ,
                         rKITE,
                         rTESO,
                         rMS,
                         rTUI,
                         rTLIT,
                         rIRWT,
                         rUBS,
                         rARZGF,
                         rFINA,
                         rAVV,
                         rAAPL,
                         rENI,
                         rHUN,
                         rBCA,
                         rSIAL,
                         rCONG,
                         rNESNY,
                         rPG,
                         rSAGE,
                         rLUX,
                         rFB,
                         rALGT,
                         rSRTI,
                         rGAM,
                         rBAC,
                         rALVG,
                         rTW,
                         rAQAC,
                         rIBM,
                         rMAInd,
                         rEUInd,
                         rSAInd,
                         rNAInd,
                         rAPInd
  )
  
  return(forecasted_data)
  
}


