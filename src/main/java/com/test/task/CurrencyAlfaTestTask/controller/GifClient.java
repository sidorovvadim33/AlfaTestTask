package com.test.task.CurrencyAlfaTestTask.controller;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${gif.client.name}", url = "${gif.client.api}")
public interface GifClient {
    @GetMapping("?type=gifs&sort=&" +
            "q=" + "{searchTag}" +
            "&api_key=" + "${gif.client.api.key}" +
            "&limit=" + "{limit}")
    public String getGifArray(@PathVariable("searchTag") String searchTag, @PathVariable("limit") int limit);
}


