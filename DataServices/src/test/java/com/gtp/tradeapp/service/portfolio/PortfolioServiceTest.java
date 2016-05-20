package com.gtp.tradeapp.service.portfolio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtp.tradeapp.builder.AssetBuilder;
import com.gtp.tradeapp.builder.HistoricalPortfolioBuilder;
import com.gtp.tradeapp.builder.PositionBuilder;
import com.gtp.tradeapp.builder.UserBuilder;
import com.gtp.tradeapp.domain.AssetClass;
import com.gtp.tradeapp.domain.AssetType;
import com.gtp.tradeapp.domain.CurrentPosition;
import com.gtp.tradeapp.domain.marketdata.MarketInstrument;
import com.gtp.tradeapp.entity.Asset;
import com.gtp.tradeapp.entity.HistoricalPortfolio;
import com.gtp.tradeapp.entity.Position;
import com.gtp.tradeapp.entity.User;
import com.gtp.tradeapp.repository.AssetRepository;
import com.gtp.tradeapp.repository.HistoricalPortfolioRepository;
import com.gtp.tradeapp.repository.PositionRepository;
import com.gtp.tradeapp.repository.UserRepository;
import com.gtp.tradeapp.service.marketdata.MarketDataService;
import org.apache.commons.io.IOUtils;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.gtp.tradeapp.domain.AssetClass.Commodities;
import static com.gtp.tradeapp.domain.AssetClass.Equity;
import static com.gtp.tradeapp.domain.AssetClass.FixedIncome;
import static com.gtp.tradeapp.domain.AssetType.index;
import static com.gtp.tradeapp.domain.AssetType.stock;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class PortfolioServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    HistoricalPortfolioRepository historicalPortfolioRepository;
    @Mock
    PositionRepository positionRepository;
    @Mock
    MarketDataService marketDataService;
    @Mock
    AssetRepository assetRepository;

    @InjectMocks
    PortfolioServiceImpl portfolioService;

    private User user;
    private List<Position> positions;
    private List<HistoricalPortfolio> historicalPortfolios;
    private Map<String, Asset> assetMap;
    private Map<String, MarketInstrument> marketInstrumentMap;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);
        initParams();
    }

    @Test
    public void shouldReturnCurrentAmount() throws Exception {
        when(userRepository.findByUsername("limluc")).thenReturn(Optional.of(user));

        assertEquals(new BigDecimal(1000000), portfolioService.getCurrentCashAmount(user));
    }

    @Test
    public void shouldReturnCurrentPositionAndDistribution() throws Exception {
        when(positionRepository.findStockByUserId(1L)).thenReturn(Optional.of(positions));
        when(assetRepository.findOne("UBS")).thenReturn(assetMap.get("UBS"));
        when(assetRepository.findOne("STRL")).thenReturn(assetMap.get("STRL"));
        when(marketDataService.getLatestStockPrice("UBS")).thenReturn(marketInstrumentMap.get("UBS"));
        when(marketDataService.getLatestStockPrice("STRL")).thenReturn(marketInstrumentMap.get("STRL"));
        List<CurrentPosition> currentPosition = portfolioService.getCurrentPosition(user);

        assertEquals(2, currentPosition.size());
        assertEquals(BigDecimal.valueOf(20.73), currentPosition.get(0).getCurrentPrice());
        assertEquals(10, currentPosition.get(0).getQuantity());
        assertEquals(BigDecimal.valueOf(100.73), currentPosition.get(1).getCurrentPrice());
        assertEquals(10, currentPosition.get(1).getQuantity());

        Map<AssetType, Double> assetTypeDistribution = portfolioService.getAssetTypeDistribution(currentPosition);
        //TODO: Add Test with Index
        assertEquals(Double.valueOf(1), assetTypeDistribution.get(stock));
        assertEquals(Double.valueOf(0), assetTypeDistribution.get(index));

        Map<AssetClass, Double> assetClassDistribution = portfolioService.getAssetClassDistribution(currentPosition);
        //TODO: Add Test with Index
        assertEquals(Double.valueOf(1), assetClassDistribution.get(Equity));
        assertEquals(Double.valueOf(0), assetClassDistribution.get(Commodities));
        assertEquals(Double.valueOf(0), assetClassDistribution.get(FixedIncome));
    }

    @Test
    public void shouldReturnHistoricalPortfolio() throws Exception {
        when(historicalPortfolioRepository.findByUserIdOrderByPortfolioDateDesc(1L)).thenReturn(Optional.of(historicalPortfolios));
        assertEquals(historicalPortfolios, portfolioService.getHistoricalPortfolio(user));
    }

    @Test
    public void shouldReturnAssetSummary() throws Exception {

        Map<String, BigDecimal> expected = new HashMap<String, BigDecimal>() {
            {
                put("cash", BigDecimal.valueOf(1000000));
                put("totalPortfolio", new BigDecimal(1214.60, new MathContext(6, RoundingMode.HALF_EVEN)));
                put("netAssetValue", new BigDecimal(1001214.60, new MathContext(9, RoundingMode.HALF_EVEN)));
                put("delta", BigDecimal.valueOf(8342.45));
                put("ytd", new BigDecimal(1214.60, new MathContext(6, RoundingMode.HALF_EVEN)));
                put("beta", BigDecimal.valueOf(-0.635229440511374));
            }
        };

        when(userRepository.findByUsername("limluc")).thenReturn(Optional.of(user));
        when(positionRepository.findStockByUserId(1L)).thenReturn(Optional.of(positions));
        when(marketDataService.getLatestStockPrice("UBS")).thenReturn(marketInstrumentMap.get("UBS"));
        when(marketDataService.getLatestStockPrice("STRL")).thenReturn(marketInstrumentMap.get("STRL"));
        when(historicalPortfolioRepository.findByUserIdOrderByPortfolioDateDesc(1L)).thenReturn(Optional.of(historicalPortfolios));

        when(marketDataService.getIndicatorStockPrice(any())).thenReturn("{\"metric\": \"Beta\", \"valuelist\": [{\"beta\": -0.635229440511374, \"datetime\": \"2015-10-22 00:00:00\"}], \"stock_id\": \"UBS\"}");
        when(marketDataService.getIndicatorStockPrice(any())).thenReturn("{\"metric\": \"Beta\", \"valuelist\": [{\"beta\": -0.635229440511374, \"datetime\": \"2015-10-22 00:00:00\"}], \"stock_id\": \"UBS\"}");

        assertEquals(expected, portfolioService.getAssetSummary(user));
    }

    private void initParams() throws IOException {
        initUser();
        initPosition();
        initAsset();
        initMarketInstrument();
        initHistoricalPortfolio();
    }

    private void initUser() {
        user = new UserBuilder(1L)
                .withUsername("limluc")
                .withFirstname("Lucky")
                .withLastname("Lim")
                .withPassword("123456")
                .withCash(new BigDecimal(1000000))
                .asUser()
                .build();
    }

    private void initPosition() {
        positions = Arrays.asList(
                new PositionBuilder()
                        .withQuantity(10)
                        .withStockId("UBS")
                        .withUserId(1L)
                        .build(),
                new PositionBuilder()
                        .withQuantity(10)
                        .withStockId("STRL")
                        .withUserId(1L)
                        .build());
    }

    private void initAsset() {
        assetMap = new HashMap<String, Asset>() {{
            put("UBS", new AssetBuilder()
                    .withAssetClass(Equity)
                    .withAssetType(stock)
                    .withIndustry("ABC")
                    .withName("UBS AG")
                    .withRegion("CH")
                    .withTicker("UBS")
                    .build());
            put("STRL", new AssetBuilder()
                    .withAssetClass(Equity)
                    .withAssetType(stock)
                    .withIndustry("SMT")
                    .withName("STRL AG")
                    .withRegion("US")
                    .withTicker("STRL")
                    .build());
        }};
    }

    private void initMarketInstrument() throws IOException {
        String ubs = IOUtils.toString(
                getClass().getClassLoader().getResourceAsStream("controller/asset/sampleUBS.json"));
        String strl = IOUtils.toString(
                getClass().getClassLoader().getResourceAsStream("controller/asset/sampleSTRL.json"));

        marketInstrumentMap = new HashMap<String, MarketInstrument>() {{
            put("UBS", new ObjectMapper().readValue(ubs, MarketInstrument.class));
            put("STRL", new ObjectMapper().readValue(strl, MarketInstrument.class));
        }};
    }

    private void initHistoricalPortfolio() {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        historicalPortfolios = Arrays.asList(
                new HistoricalPortfolioBuilder(1L, 1L)
                        .withCash(BigDecimal.valueOf(100))
                        .withPortfolio(BigDecimal.valueOf(20))
                        .build(),
                new HistoricalPortfolioBuilder(1L, 1L)
                        .withCash(BigDecimal.valueOf(100))
                        .withPortfolio(BigDecimal.valueOf(20))
                        .build()
        );
    }
}