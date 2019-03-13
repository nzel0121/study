package com.elevenst.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProductRemoteServiceImpl implements ProductRemoteService {

    public static final String PRODUCT_URL = "http://localhost:8082/products/";
    @Autowired
    RestTemplate restTemplate;

    @Override
    public String getProductInfo(String proudctId) {
        return restTemplate.getForObject(PRODUCT_URL + proudctId, String.class);
    }
}
