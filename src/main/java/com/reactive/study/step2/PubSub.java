package com.reactive.study.step2;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 플로우 (데이터 가공 )
 * Publisher -> [Data] -> Op1 -> [Data2] -> Op2 -> [Data3] -> Subscriber
 * 1. map (d1 -> f -> d2 ) 데이터 1을 function 을 통해서 d2 로 가공시킴
 *
 * pub -> [Data1] -> mapPub -> [Data2] -> logSub
 *
 */
@Slf4j
public class PubSub {
    public static void main(String[] args) {

        Publisher<Integer> publisher = getPublisher(Stream.iterate(1, a -> a + 1).limit(10).collect(Collectors.toList()));
//        Publisher<Integer> mapPub = mapPub(publisher, s -> s * 10);
//        Publisher<String> mapPub = mapPub(publisher, s -> "[" + s + "]");
//        Publisher<Integer> subPub = subPub(publisher);
        Publisher<StringBuilder> reducePub = reducePub(publisher, new StringBuilder(),
                (a, b) -> a.append(b).append(","));
        reducePub.subscribe(logSub());

    }

    private static <T, R> Publisher<R> reducePub(Publisher<T> publisher, R init, BiFunction<R, T, R> biFunction) {
        return new Publisher<R>() {
            @Override
            public void subscribe(Subscriber<? super R> sub) {
                publisher.subscribe(new DelegateSub<T, R>(sub) {

                    R result = init;

                    @Override
                    public void onNext(T integer) {
                        result = biFunction.apply(result, integer);
                    }

                    @Override
                    public void onComplete() {
                        sub.onNext(result);
                        sub.onComplete();
                    }
                });
            }
        };
    }

//    private static Publisher<Integer> subPub(Publisher<Integer> publisher) {
//        return new Publisher<Integer>() {
//            @Override
//            public void subscribe(Subscriber<? super Integer> sub) {
//                publisher.subscribe(new DelegateSub(sub) {
//                    int sum = 0;
//
//                    @Override
//                    public void onNext(Integer integer) {
//                        sum += integer;
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        // 최종 결과를 한번만 넘겨줌
//                        sub.onNext(sum);
//                        sub.onComplete();
//                    }
//                });
//            }
//        };
//    }

    private static <T, R> Publisher<R> mapPub(Publisher<T> publisher, Function<T, R> function) {
        return new Publisher<R>() {
            @Override
            public void subscribe(Subscriber<? super R> sub) {
                publisher.subscribe(new DelegateSub<T, R>(sub) {
                    @Override
                    public void onNext(T t) {
                        sub.onNext(function.apply(t));
                    }
                });
            }
        };
    }

    private static <T> Subscriber<T> logSub() {
        return new Subscriber<T>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.debug("onSubscribe: {}", s);
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(T integer) {
                log.debug("onNext: {}", integer);
            }

            @Override
            public void onError(Throwable t) {
                log.debug("onError: {}", t);
            }

            @Override
            public void onComplete() {
                log.debug("onComplete");
            }
        };
    }

    private static Publisher<Integer> getPublisher(List<Integer> iter) {
        return new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> s) {
                s.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        try {
                            iter.forEach(s::onNext);
                            s.onComplete();
                        } catch (Throwable e) {
                            s.onError(e);
                        }
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };
    } // end

}
