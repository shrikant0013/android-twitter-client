package com.shrikant.mytwitter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import com.loopj.android.http.TextHttpResponseHandler;
import com.shrikant.mytwitter.fragments.UserInfoFragment;
import com.shrikant.mytwitter.fragments.UserTimelineFragment;
import com.shrikant.mytwitter.tweetmodels.User;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

public class ProfileActivity extends AppCompatActivity {
    public static final String USER = "user";
    TwitterClient mTwitterClient;
    User newUser;
    String screenName;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTwitterClient = TwitterApplication.getRestClient();

        //Get the screen name from activity that launches this
        screenName = getIntent().getStringExtra("screen_name");
        if (getIntent().hasExtra("screen_name")) {
            getUserInfo(savedInstanceState);
        } else {
            getMyInfo(savedInstanceState);
        }
    }

    void getMyInfo(final Bundle savedInstanceState) {
        mTwitterClient.getMyInfo(new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Gson gson = new GsonBuilder().create();
                    JsonObject jsonUserObject = gson.fromJson(responseString, JsonObject.class);

                    if (jsonUserObject != null) {
                        newUser = User.fromJsonObjectToUser(jsonUserObject);
                        if (newUser != null && !TextUtils.isEmpty(newUser.getTwitterHandle())) {
                            getSupportActionBar().setTitle("@" + newUser.getTwitterHandle());
                        }

                        loadPage(savedInstanceState, screenName);
                    }
                } catch (JsonParseException e) {
                    Log.d("Async onSuccess", "Json parsing error:" + e.getMessage(), e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.w("AsyncHttpClient", "HTTP Request failure: " + statusCode + " " +
                        throwable.getMessage());
            }

        });
    }

    void getUserInfo(final Bundle savedInstanceState) {
        mTwitterClient.getUserInfo(screenName, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Gson gson = new GsonBuilder().create();
                    JsonObject jsonUserObject = gson.fromJson(responseString, JsonObject.class);

                    if (jsonUserObject != null) {
                        newUser = User.fromJsonObjectToUser(jsonUserObject);
                        if (newUser != null && !TextUtils.isEmpty(newUser.getTwitterHandle())) {
                            getSupportActionBar().setTitle("@" + newUser.getTwitterHandle());
                        }

                        loadPage(savedInstanceState, screenName);
                    }
                } catch (JsonParseException e) {
                    Log.d("Async onSuccess", "Json parsing error:" + e.getMessage(), e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.w("AsyncHttpClient", "HTTP Request failure: " + statusCode + " " +
                        throwable.getMessage());
            }

        });
    }

    void loadPage(Bundle savedInstanceState, String screenName) {
        if (savedInstanceState == null) {
            //Create UserTimelinefragment
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(screenName);
            UserInfoFragment userInfoFragment =
                    UserInfoFragment.newInstance(
                            newUser.getProfileImageUrl(),
                            newUser.getProfileBackgroundImageUrl(),
                            newUser.getUserName(), newUser.getTagLine(),
                            newUser.getFollowers(), newUser.getFollowing());

            //Display the UserTimelinefragment in this activity(dynamically)
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flUserTimelineContainer, userTimelineFragment);
            ft.replace(R.id.rlUserHeader, userInfoFragment);
            ft.commit();
        }
    }

    public void showFollowing(View view) {
        Intent i = new Intent(this, FollowingActivity.class);
        i.putExtra("screen_name", screenName);
        startActivity(i);
    }

    public void showFollowers(View view) {
        Intent i = new Intent(this, FollowersActivity.class);
        i.putExtra("screen_name", screenName);
        startActivity(i);

    }
}
