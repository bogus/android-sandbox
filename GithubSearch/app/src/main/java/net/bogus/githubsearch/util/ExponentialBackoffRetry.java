package net.bogus.githubsearch.util;


import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.concurrent.TimeUnit;
import org.reactivestreams.Publisher;


public class ExponentialBackoffRetry implements
        Function<Flowable<? extends Throwable>, Publisher<?>> {

    private final int maxRetries;
    private final int retryDelayMillis;
    private int retryCount = 0;

    public ExponentialBackoffRetry(final int maxRetries, final int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public Publisher<?> apply(Flowable<? extends Throwable> input) throws Exception {
        return input.flatMap(
                new io.reactivex.functions.Function<Throwable, Flowable<?>>() {
                    @Override
                    public Flowable<?> apply(Throwable throwable) throws Exception {
                        if (++retryCount < maxRetries) {
                            return Flowable.timer(retryDelayMillis * retryCount, TimeUnit.MILLISECONDS);
                        }
                        return Flowable.error(throwable);
                    }
                });
    }

}
