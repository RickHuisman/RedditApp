package com.rickh.redditapp.reddit;

import com.rickh.redditapp.App;

import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.pagination.DefaultPaginator;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static io.reactivex.Observable.just;

public class Repository {

    private RedditClient mRedditClient;

    public Repository() {
        mRedditClient = App.getAccountHelper().switchToUser("rickhuis");
    }

    public Observable<Listing<Submission>> getFrontPageObservable(String subreddit) {
        return Observable.fromCallable(() -> true)
                .flatMap(__ -> just(subredditPaginator(subreddit).build().next()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private DefaultPaginator.Builder<Submission, SubredditSort> subredditPaginator(String subreddit) {
        return mRedditClient.subreddit(subreddit)
                .posts()
                .sorting(SubredditSort.TOP)
                .timePeriod(TimePeriod.YEAR);
    }
}
