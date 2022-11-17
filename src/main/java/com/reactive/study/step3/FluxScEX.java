package com.reactive.study.step3;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class FluxScEX {
    public static void main(String[] args) throws InterruptedException {
        Flux.interval(Duration.ofMillis(200))
                .take(10) // 10개만 받음
                .subscribe(s -> log.debug("onNext : {}", s));

        TimeUnit.SECONDS.sleep(5);;

//        Executors.newSingleThreadExecutor().execute(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(2);
//            } catch (InterruptedException e) {
//
//            }
//            System.out.println("hello");
//        });
//        System.out.println("exit");
    }
}
