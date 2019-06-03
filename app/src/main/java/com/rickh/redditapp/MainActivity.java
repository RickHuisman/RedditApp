package com.rickh.redditapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import net.dean.jraw.RedditClient;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.oauth.DeferredPersistentTokenStore;
import net.dean.jraw.pagination.DefaultPaginator;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static io.reactivex.Observable.just;

public class MainActivity extends DankActivity {

    private RecyclerView mSubredditPostList;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private SubredditAdapter mAdapter;
    private RedditClient redditClient;
    private DeferredPersistentTokenStore tokenStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Frontpage");

        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddUserActivity.class));
                System.out.println("Login");
            }
        });

        tokenStore = App.getTokenStore();
        System.out.println("TOKENSTORE");
        System.out.println(tokenStore.getUsernames());

        mSubredditPostList = findViewById(R.id.subreddit_post_list);
        mSubredditPostList.setHasFixedSize(true);
        mSubredditPostList.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new SubredditAdapter();
        mSubredditPostList.setAdapter(mAdapter);

        redditClient = App.getAccountHelper().switchToUser("rickhuis");

        mDisposable.add(getFrontPageObservable().subscribe(new Consumer<Listing<Submission>>() {
            @Override
            public void accept(Listing<Submission> submissions) throws Exception {
                ArrayList<Submission> adapterList = new ArrayList<>();
                adapterList.addAll(submissions);
                mAdapter.setSubmissions(adapterList);

                for (Submission s : submissions) {
                    System.out.println(s);
                }
            }
        }));
    }

    private Observable<Listing<Submission>> getFrontPageObservable() {
        return Observable.fromCallable(() -> true)
                .flatMap(__ -> just(subredditPaginator("Soccer").build().next()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private DefaultPaginator.Builder<Submission, SubredditSort> subredditPaginator(String subreddit) {
        return redditClient.subreddit(subreddit)
                .posts()
                .sorting(SubredditSort.TOP)
                .timePeriod(TimePeriod.YEAR);
    }
}
