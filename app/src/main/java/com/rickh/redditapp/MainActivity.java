package com.rickh.redditapp;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rickh.redditapp.utils.RxUtils;

import net.dean.jraw.RedditClient;
import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.http.LoggingMode;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Submission;
import net.dean.jraw.paginators.SubredditPaginator;

import java.util.ArrayList;

import rx.Subscription;
import timber.log.Timber;

import static com.rickh.redditapp.utils.RxUtils.logError;
import static rx.Observable.just;

public class MainActivity extends DankActivity {

    private RecyclerView mSubredditPostList;
    private SubredditAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Frontpage");

        mSubredditPostList = findViewById(R.id.subreddit_post_list);
        mSubredditPostList.setHasFixedSize(true);
        mSubredditPostList.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new SubredditAdapter();
        mSubredditPostList.setAdapter(mAdapter);

        RedditClient redditClient = new RedditClient(getUserAgent());
        redditClient.setLoggingMode(LoggingMode.ALWAYS);

        AuthenticationManager authenticationManager = AuthenticationManager.get();

        DankRedditClient dankRedditClient = new DankRedditClient(
                getApplicationContext(),
                getString(R.string.reddit_api_secret),
                redditClient,
                authenticationManager);

        SubredditPaginator frontPagePaginator = dankRedditClient.getSubredditPaginator("Formula1");

        Subscription subscription = dankRedditClient
                .authenticateIfNeeded()
                .flatMap(__ -> just(frontPagePaginator.next()))
                .compose(RxUtils.applySchedulers())
                .subscribe(submissions -> {
                    ArrayList<Submission> test = new ArrayList<>();
                    for (Submission submission : submissions) {
                        test.add(submission);
                        System.out.println(submission.getTitle());
//                        Timber.i(submission.getSubredditName() + " - " + submission.getTitle() + " - " + submission.getScore());
                    }
                    mAdapter.setSubmissions(test);

                }, logError("Couldn't get front-page"));

        unsubscribeOnDestroy(subscription);
    }

    private UserAgent getUserAgent() {
        try {
            PackageInfo packageInfo = getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0);
            return UserAgent.of("android", getApplicationContext().getPackageName(), packageInfo.versionName, "saketme");

        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e, "Couldn't get app version name");
            return null;
        }
    }
}
