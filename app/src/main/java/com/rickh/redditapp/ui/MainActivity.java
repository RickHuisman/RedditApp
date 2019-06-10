package com.rickh.redditapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rickh.redditapp.R;
import com.rickh.redditapp.viewmodels.MainViewModel;

import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;

import java.util.ArrayList;

import static io.reactivex.Observable.just;

public class MainActivity extends AppCompatActivity {

    private MainViewModel mMainViewModel;
    private RecyclerView mSubredditPostList;
    private SubredditAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setTitle("Frontpage");

//        Button loginButton = findViewById(R.id.login);
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, AddUserActivity.class));
//            }
//        });

        mSubredditPostList = findViewById(R.id.subreddit_post_list);
        mSubredditPostList.setHasFixedSize(true);
        mSubredditPostList.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new SubredditAdapter();
        mSubredditPostList.setAdapter(mAdapter);

        mMainViewModel.getSubredditSubmissions("Soccer").observe(this, new Observer<Listing<Submission>>() {
            @Override
            public void onChanged(Listing<Submission> submissions) {
                mAdapter.setSubmissions(new ArrayList<>(submissions));
            }
        });
    }

    private void goToRandomSubreddit() {
        mMainViewModel.getRandomSubredditObservable().observe(this, new Observer<Listing<Submission>>() {
            @Override
            public void onChanged(Listing<Submission> submissions) {
                mAdapter.setSubmissions(new ArrayList<>(submissions));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                goToRandomSubreddit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
