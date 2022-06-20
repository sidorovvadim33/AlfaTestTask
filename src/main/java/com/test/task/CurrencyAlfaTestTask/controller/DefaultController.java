package com.test.task.CurrencyAlfaTestTask.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Random;

@RestController
@EnableFeignClients
public class DefaultController {
    private final int GIF_LIMIT = 100;

    @Autowired
    private CurrencyClient currencyClient;

    @Autowired
    private GifClient gifClient;

    @GetMapping("/")
    public ModelAndView index(Model model) {
        //Yesterday Date for Api
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        //Getting Currency form API JSON
        double currentCurrencyRate = getCurrencyFromApiJSON(currencyClient.getCurrentExchangeRate());
        double yesterdayCurrencyRate = getCurrencyFromApiJSON(currencyClient.getYesterdayExchangeRate(yesterday.toString()));

        //Getting Gif ID from API JSON depending on currency compare
        String gif = getGifIdDependingOnCurrency(currentCurrencyRate, yesterdayCurrencyRate);

        //Initializing html file
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");

        //Adding a link to gif url to the html with Thymeleaf
        model.addAttribute("src", "https://i.giphy.com/media/" + gif + "/giphy.webp");

        return modelAndView;
    }

    public double getCurrencyFromApiJSON(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject.getJSONObject("rates").getDouble("RUB");
    }

    public String getGifIdDependingOnCurrency(double currentCurrencyRate, double yesterdayCurrencyRate) {
        String jsonStringGif = "";

        if (currentCurrencyRate > yesterdayCurrencyRate) {
            jsonStringGif = gifClient.getGifArray("rich", GIF_LIMIT);
        } else {
            jsonStringGif = gifClient.getGifArray("broke", GIF_LIMIT);
        }

        JSONObject gifJsonObj = new JSONObject(jsonStringGif);
        JSONArray gifArray = gifJsonObj.getJSONArray("data");

        Random rnd = new Random();
        int number = rnd.nextInt(GIF_LIMIT);
        return gifArray.getJSONObject(number).getString("id");
    }
}
