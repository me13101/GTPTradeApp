package com.gtp.tradeapp.service.marketdata;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtp.tradeapp.domain.AssetType;
import com.gtp.tradeapp.domain.marketdata.MarketInstrument;
import com.gtp.tradeapp.entity.Asset;
import com.gtp.tradeapp.repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MarketDataServiceImpl implements MarketDataService {
    //    @PROD
    private static final String URL = "http://192.168.23.1:8090/";
    private final AssetRepository assetRepository;

    RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public MarketDataServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public MarketInstrument getLatestStockPrice(String stock) {
        try {
            String url = latestStockPriceUrl(stock);

            HttpEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            validateAuthentication(response.getHeaders(), response.getBody());

            return new ObjectMapper().readValue(response.getBody(), MarketInstrument.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getHistoricalStockPrice(String stock) {
        try {
            String url = historicalStockPriceUrl(stock);

            HttpEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            validateAuthentication(response.getHeaders(), response.getBody());

            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public MarketInstrument getLatestMarketData(AssetType assetType) {
        try {
            String url = latestMarketDataUrl();

            HttpEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
            validateAuthentication(response.getHeaders(), response.getBody());

            MarketInstrument marketInstrument = new ObjectMapper().readValue(response.getBody(), MarketInstrument.class);
            if (assetType == null) {
                return marketInstrument;
            } else {
                return new MarketInstrument(marketInstrument.getStocklist().stream()
                        .filter(s -> s.getType().equals(assetType.toString()))
                        .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getStockPriceMetric(Map<String, Object> map) {
        try {
            String url = URL + "analytics/stockmetrics"+ "?stock='{stock}'&days={days}&interval={interval}";

            HttpEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, map);
            validateAuthentication(response.getHeaders(), response.getBody());

            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Object getIndicatorStockPrice(Map<String, Object> params) {
        try {
            String url = URL + "analytics/" + "{indicator}?stock='{stock}'&days={days}&interval={interval}";

            HttpEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class, params);
            validateAuthentication(response.getHeaders(), response.getBody());

            return response.getBody();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Iterable<Asset> getListOfStocks() {
        return assetRepository.findAll();
    }

    private String latestStockPriceUrl(String stock) {
        return URL + "stockprice?stock='" + stock + "'";
    }

    private String historicalStockPriceUrl(String stock) {
        return URL + "pricehistory?stock='" + stock + "'";
    }

    private String latestMarketDataUrl() {
        return URL + "pricelist";
    }

    private void validateAuthentication(HttpHeaders headers, String resultString) throws NoSuchAlgorithmException, InvalidKeyException {
        try {
            String authentication = headers.get("Authentication").get(0).substring(6);
            String tag = Arrays.toString(Base64.getDecoder().decode(authentication));
            String bytes = Arrays.toString(getHMAC().doFinal(resultString.getBytes()));
            if (!tag.equals(bytes)) {
                throw new RuntimeException("Unable to Authenticate to Market Data");
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to Authenticate to Market Data");
        }
    }

    private Mac getHMAC() throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secret = new SecretKeySpec("5Dd0UXAIllS1xrRgrC2i6mT8voZQ9kLO".getBytes(), hmac.getAlgorithm());
        hmac.init(secret);
        return hmac;
    }
}
