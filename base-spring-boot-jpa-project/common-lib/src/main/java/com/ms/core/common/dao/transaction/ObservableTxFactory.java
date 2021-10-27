package com.ms.core.common.dao.transaction;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rx.Observable;
import rx.Subscriber;

/**
 *  Clase que gestiona las trasacciones asincronas
 */
@Component()
public class ObservableTxFactory {
	
    public final <T> Observable<T> create(Observable.OnSubscribe<T> f) {
        return new ObservableTx<>(this, f);
    }

    @Transactional
    public void call(Observable.OnSubscribe onSubscribe, Subscriber subscriber) {
        onSubscribe.call(subscriber);
    }

    private static class ObservableTx<T> extends Observable<T> {
        public ObservableTx(ObservableTxFactory observableTxFactory, OnSubscribe<T> f) {
            super(new OnSubscribeDecorator<>(observableTxFactory, f));
        }
    }

    private static class OnSubscribeDecorator<T> implements Observable.OnSubscribe<T> {
        private final ObservableTxFactory observableTxFactory;
        private final Observable.OnSubscribe<T> onSubscribe;
        OnSubscribeDecorator(final ObservableTxFactory observableTxFactory, final Observable.OnSubscribe<T> s) {
            this.onSubscribe = s;
            this.observableTxFactory = observableTxFactory;
        }

        @Override
        public void call(Subscriber<? super T> subscriber) {
            observableTxFactory.call(onSubscribe, subscriber);
        }
    }
    
}