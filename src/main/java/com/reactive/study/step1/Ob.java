package com.reactive.study.step1;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressWarnings("deprecation")
public class Ob {

    /*
        observer 의 문제점?!

        1. Complete 가 없다.. 표현 할 수 가 없다 -> 마지막 데이터가 뭔지 그냥 문자열로 표현해줘야한다. -> ex) 단순 db 데이터 1개 조회인데 종료이닞 모룬다.
        2. 에러

     */

    /**
     * iterable -> pull
     *
     * observable -> push
     */

    /*
    Java Iterable 구현
          Iterable<Integer> iter = () -> new Iterator<>() {
            int i = 0;
            final static  int MAX = 10;
            @Override
            public boolean hasNext() {
                return i < MAX;
            }

            @Override
            public Integer next() {
                return ++i;
            }
        };

        for (Integer i : iter) {
            System.out.println(i);
        }

     */

    static class IntObservable extends Observable implements Runnable {
        @Override
        public void run() {
            for (int i=1; i<=10; i++) {
                setChanged();
                notifyObservers(i);
            }
        }
    }



    public static void main(String[] args) {
        Observer ob = (o, arg) -> {
            System.out.println(Thread.currentThread().getName() + " " + arg);
        };

        IntObservable io = new IntObservable();
        io.addObserver(ob);

        ExecutorService es = Executors.newSingleThreadExecutor();
        es.execute(io);

        System.out.println(Thread.currentThread().getName() + " EXIT");
        es.shutdown();

    }
}
