package com.noambechhofer.yaniv.utilities;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

/**
 * An {@link AbstractSubscriber} is a {@link Subscriber} that does nothing. This
 * allows classes to implement {@code Subscriber} without having to implement
 * all of the methods.
 */
public interface AbstractSubscriber<T> extends Subscriber<T> {

    @Override
    default void onSubscribe(Subscription subscription) {
    }

    @Override
    default void onNext(T item) {
    }

    @Override
    default void onError(Throwable throwable) {
    }

    @Override
    default void onComplete() {
    }

}
