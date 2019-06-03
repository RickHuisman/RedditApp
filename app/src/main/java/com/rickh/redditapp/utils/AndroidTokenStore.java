package com.rickh.redditapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import net.dean.jraw.models.OAuthData;
import net.dean.jraw.oauth.TokenStore;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Simple implementation of TokenStore that uses SharedPreferences
 */
public class AndroidTokenStore implements TokenStore {

    private final Context context;

    public AndroidTokenStore(Context context) {
        this.context = context;
    }


    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(context.getPackageName() + ".reddit_token_store", Context.MODE_PRIVATE);
    }

    @Override
    public void deleteLatest(@NotNull String s) {

    }

    @Override
    public void deleteRefreshToken(@NotNull String s) {

    }

    @Nullable
    @Override
    public OAuthData fetchLatest(@NotNull String s) {
        return null;
    }

    @Nullable
    @Override
    public String fetchRefreshToken(@NotNull String s) {
        return null;
    }

    @Override
    public void storeLatest(@NotNull String s, @NotNull OAuthData oAuthData) {

    }

    @Override
    public void storeRefreshToken(@NotNull String s, @NotNull String s1) {

    }
}
