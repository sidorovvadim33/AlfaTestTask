package com.test.task.CurrencyAlfaTestTask.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "${currency.client.name}", url = "${currency.client.api}")
public interface CurrencyClient {
    @GetMapping("/latest.json?" +
            "app_id=" + "${currency.client.api.id}" +
            "&symbols=" + "${currency.client.rate.currency}")
    public String getCurrentExchangeRate();

    @GetMapping("/historical/" +
            "{date}" + ".json?" +
            "app_id=" + "${currency.client.api.id}" + "&" +
            "symbols=" + "${currency.client.rate.currency}")
    public String getYesterdayExchangeRate(@PathVariable String date);
}
