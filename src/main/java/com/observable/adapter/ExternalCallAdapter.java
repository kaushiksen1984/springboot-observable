package com.observable.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ExternalCallAdapter {

    @Autowired
    private RestTemplate restTemplate;

    public String getNameFromMS() throws InterruptedException {
        return restTemplate.getForEntity("http://localhost:8080/name/getName", String.class)
                    .getBody();

    }

    public String getGreetingFromMs(String name) throws InterruptedException {
        return restTemplate.getForEntity("http://localhost:8082/greetings/getGreeting/{name}",
                        String.class, name).getBody();
    }

}
