package com.observable.controller;

import com.observable.adapter.ExternalCallAdapter;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private ExternalCallAdapter externalCallAdapter;

    @GetMapping("/getMessage")
    public String getMessage() throws InterruptedException {
        long time = System.currentTimeMillis();
        Observable<String> nameObservable = Observable.fromCallable(externalCallAdapter::getNameFromMS)
                .subscribeOn(Schedulers.newThread());
        long responseTimeFromNameAPI = (System.currentTimeMillis() - time);
        System.out.println("Response time from Name API: " +  responseTimeFromNameAPI);
        System.out.println("Response from Name API : " + nameObservable.blockingFirst());
        String retrievedName = nameObservable.blockingFirst();
        Observable<String> greetingObservable = Observable.fromCallable(()->externalCallAdapter.getGreetingFromMs(retrievedName))
                .subscribeOn(Schedulers.newThread());
        long responseTimeFromGreetinsAPI = (System.currentTimeMillis() - time);
        System.out.println("Response time from Greeting API: " +  responseTimeFromGreetinsAPI);
        System.out.println("Response from Greetings API : " + greetingObservable.blockingFirst());
        String response = Observable.zip(nameObservable, greetingObservable, this::merge).blockingFirst();
        long responseTime = (System.currentTimeMillis() - time);
        System.out.println("Time to get response from API : " + responseTime);
        return response;
    }

    private String merge(String nameObservable, String greetingObservable) {
        return "Hello " + greetingObservable;
    }
}
