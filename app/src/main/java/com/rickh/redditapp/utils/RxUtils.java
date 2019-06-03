package com.rickh.redditapp.utils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Utility methods related to Rx.
 */
public class RxUtils {

    /**
     * A transformer that makes an Observable execute its computation (or emit items) inside an IO thread and the
     * operators that follow this method call (including the Subscriber) execute on the main thread. This should ideally
     * be called right before (or as close as possible) to the subscribe() call to ensure any other operator doesn't
     * accidentally get executed on the main thread.
     */
//    public static <T> Observable.Transformer<T, T> applySchedulers() {
//        return observable -> observable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//    }
//
//    public static <T> Action1<T> doNothing() {
//        return t -> {};
//    }
//
//    public static Action<Throwable> logError(String errorMessage) {
//        return error -> Timber.e(error, errorMessage);
//    }

}
