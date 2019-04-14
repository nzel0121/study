package com.elevenst;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.BitSet;

@EnableCircuitBreaker
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@Configuration
public class DisplayApplication {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public static void main(String[] args) {

        //SpringApplication.run(DisplayApplication.class);

        BitSet a = new BitSet(4);

        Arrays.asList(1,2,3,0,5).stream()
                .forEach(i->a.set(i));

        System.out.println(a.get(0,5));
    }

}
