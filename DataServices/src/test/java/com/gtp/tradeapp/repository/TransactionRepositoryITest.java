package com.gtp.tradeapp.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gtp.tradeapp.Application;
import com.gtp.tradeapp.builder.TransactionBuilder;
import com.gtp.tradeapp.entity.Transaction;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = Application.class)
public class TransactionRepositoryITest {

    @Autowired
    TransactionRepository transactionRepository;

    private List<Transaction> transactionList;

    @Before
    public void setUp() {
        transactionList = getTransaction();
    }


    @Test
    public void shouldAddNewTransaction() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        String expectedTransaction = IOUtils.toString(classLoader.getResourceAsStream("controller/transaction/expectedTransaction.json"));

        transactionRepository.save(transactionList);

        ObjectMapper mapper = new ObjectMapper();
        String actualTransaction = mapper.writeValueAsString(transactionRepository.findByUserId(1L).get());

        assertEquals(expectedTransaction, actualTransaction, false);
    }

    private List<Transaction> getTransaction() {
        return Arrays.asList(
                new TransactionBuilder()
                        .withPrice(new BigDecimal(100))
                        .withQuantity(10)
                        .withTicker("AAPL")
                        .withUserId(1L)
                        .build(),
                new TransactionBuilder()
                        .withPrice(new BigDecimal(150))
                        .withQuantity(5)
                        .withTicker("SMTH")
                        .withUserId(1L)
                        .build()
        );
    }
}