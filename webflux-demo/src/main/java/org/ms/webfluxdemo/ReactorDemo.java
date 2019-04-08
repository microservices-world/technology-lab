package org.ms.webfluxdemo;

import java.util.concurrent.TimeUnit;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

/**
 * @author Zhenglai
 * @since 2019-04-09 00:34
 */
public class ReactorDemo {

    public static void main(String[] args) {
        String[] strs = {"1", "2", "3"};

        Subscriber<Integer> subscriber = new Subscriber<>() {
            private Subscription subscription;

            @Override
            public void onSubscribe(final Subscription subscription) {
                this.subscription = subscription;
                subscription.request(1);
            }

            @Override
            public void onNext(final Integer item) {
                System.out.println("received data: " + item);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscription.request(1);
            }

            @Override
            public void onError(final Throwable throwable) {

            }

            @Override
            public void onComplete() {
                subscription.cancel();
            }
        };
        
        // jdk 8 stream
        Flux.fromArray(strs).map(s -> Integer.parseInt(s))
                // terminal ops
                // jdk 9 reactive stream
                .subscribe(subscriber);

        
    }
}
