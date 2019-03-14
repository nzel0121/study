package com.elevenst.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductRemoteServiceImpl implements ProductRemoteService {

    public static final String PRODUCT_URL = "http://localhost:8082/products/";
    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getProductInfoFallback")
    @Override
    public String getProductInfo(String proudctId) {
        return restTemplate.getForObject(PRODUCT_URL + proudctId, String.class);
    }

    public String getProductInfoFallback(String productId, Throwable t) {
        System.out.println("t = " + t);
        return "[This Product is sold out]";
    }
}
