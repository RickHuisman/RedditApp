package com.rickh.redditapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.rickh.redditapp.reddit.Repository;

import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MainViewModel extends AndroidViewModel {

    private Repository repository;
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private LiveData<Listing<Submission>> mSubmissions = new MutableLiveData<>();

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository();
    }

    public LiveData<Listing<Submission>> getSubredditSubmissions(String subreddit) {
        mDisposable.add(repository.getFrontPageObservable(subreddit).subscribe(new Consumer<Listing<Submission>>() {
            @Override
            public void accept(Listing<Submission> submissions) {
                ((MutableLiveData<Listing<Submission>>) mSubmissions).setValue(submissions);
            }
        }));
        return mSubmissions;
    }
}
