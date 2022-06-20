package com.test.task.CurrencyAlfaTestTask;

import com.test.task.CurrencyAlfaTestTask.controller.CurrencyClient;
import com.test.task.CurrencyAlfaTestTask.controller.DefaultController;
import com.test.task.CurrencyAlfaTestTask.controller.GifClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class CurrencyAlfaTestTaskApplicationTests {
    @Autowired
    private CurrencyClient currencyClient;

    @Autowired
    private GifClient gifClient;
    @Autowired
    DefaultController defaultController;

    final double currentRate = 57.06465168;
    final double yesterdayRate = 58.06678398;

    @Test
    void contextLoads() {
        assertThat(defaultController).isNotNull();
    }

    @Test
    void testGifFeign() {
        assertThat(this.gifClient.getGifArray("rich", 1)).isNotNull();
    }

    @Test
    void testCurrencyFeignCurrent() {
        assertThat(this.currencyClient.getCurrentExchangeRate()).isNotNull();
    }

    @Test
    void testCurrencyFeignYesterday() {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        assertThat(this.currencyClient.getYesterdayExchangeRate(yesterday.toString())).isNotNull();
    }

    @Test
    void testGetCurrencyFromApiJSON() {
        CurrencyClient currencyClient = mock(CurrencyClient.class);

        String currentTestRate = "{" +
                "rates: {" +
                "\"RUB\": " + currentRate + "}}";
        when(currencyClient.getCurrentExchangeRate()).thenReturn(currentTestRate);

        String yesterdayTestRate = "{" +
                "rates: {" +
                "\"RUB\": " + yesterdayRate + "}}";
        ;
        when(currencyClient.getYesterdayExchangeRate("someDate")).thenReturn(yesterdayTestRate);

        assertThat(defaultController.getCurrencyFromApiJSON(currencyClient.getCurrentExchangeRate())).isEqualTo(currentRate);
        assertThat(defaultController.getCurrencyFromApiJSON(currencyClient.getYesterdayExchangeRate("someDate"))).isEqualTo(yesterdayRate);
    }

    @Test
    void testGetGifIdDependingOnCurrency() throws IOException {
        String gif = defaultController.getGifIdDependingOnCurrency(currentRate, yesterdayRate);
        URL url = null;
        url = new URL("https://i.giphy.com/media/" + gif + "/giphy.webp");
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        int responseCode = httpURLConnection.getResponseCode();

        assertThat(HttpURLConnection.HTTP_OK).isEqualTo(responseCode);
    }
}
