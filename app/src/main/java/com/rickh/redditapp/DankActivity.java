package com.rickh.redditapp;

import android.support.v7.app.AppCompatActivity;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class DankActivity extends AppCompatActivity {

    private CompositeSubscription onStopSubscriptions;
    private CompositeSubscription onDestroySubscriptions;

    @Override
    protected void onStop() {
        if (onStopSubscriptions != null) {
            onStopSubscriptions.clear();
        }

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (onDestroySubscriptions != null) {
            onDestroySubscriptions.clear();
        }

        super.onDestroy();
    }

    protected void unsubscribeOnStop(Subscription subscription) {
        if (onStopSubscriptions == null) {
            onStopSubscriptions = new CompositeSubscription();
        }
        onStopSubscriptions.add(subscription);
    }

    protected void unsubscribeOnDestroy(Subscription subscription) {
        if (onDestroySubscriptions == null) {
            onDestroySubscriptions = new CompositeSubscription();
        }
        onDestroySubscriptions.add(subscription);
    }
}
