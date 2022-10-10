package com.reactive.study.object;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;

/**
 * publisher subscriber
 */
public class PubSub {
    public static void main(String[] args) {
        // publisher <- Observable
        // Subscriber <- Observer

        Iterable<Integer> itr = Arrays.asList(1, 2, 3, 4, 5);

        Publisher publisher = new Publisher() {
            @Override
            public void subscribe(Subscriber subscriber) {
                subscriber.onSubscribe(new Flow.Subscription() {
                    @Override
                    public void request(long n) {

                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };

        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Flow.Subscription subscription) {

            }

            @Override
            public void onNext(Integer item) {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        };

        publisher.subscribe(subscriber);

    }
}
