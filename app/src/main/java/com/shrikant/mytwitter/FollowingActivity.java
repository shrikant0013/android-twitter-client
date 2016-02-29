package com.shrikant.mytwitter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import com.loopj.android.http.TextHttpResponseHandler;
import com.shrikant.mytwitter.adapters.DividerItemDecoration;
import com.shrikant.mytwitter.adapters.EndlessRecyclerViewScrollListener;
import com.shrikant.mytwitter.adapters.RecyclerViewFollowingAdapter;
import com.shrikant.mytwitter.tweetmodels.User;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FollowingActivity extends AppCompatActivity {

    final static String NEXT_CURSOR = "next_cursor";
    final static String USERS = "users";

    private TwitterClient mTwitterClient;
    LinearLayoutManager layoutManager;

    long cursor = -1;
    LinkedList<User> mUsers;
    String screenName;
    RecyclerViewFollowingAdapter mRecyclerViewFollowingAdapter;

    @Bind(R.id.rvFollowing) RecyclerView mRecyclerViewFollowing;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.swipeContainerFollowing) SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher_birdy);
        getSupportActionBar().setTitle("Following");

        Intent i = getIntent();
        screenName = i.getStringExtra("screen_name");

        //mTweets = new ArrayList<>();
        mUsers = new LinkedList<>();
        mRecyclerViewFollowingAdapter = new RecyclerViewFollowingAdapter(this, mUsers);
        mRecyclerViewFollowing.setAdapter(mRecyclerViewFollowingAdapter);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        mRecyclerViewFollowing.addItemDecoration(itemDecoration);

        // Setup layout manager for items
        layoutManager = new LinearLayoutManager(this);
        // Control orientation of the items
        // also supports LinearLayoutManager.HORIZONTAL
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // Optionally customize the position you want to default scroll to
        layoutManager.scrollToPosition(0);
        // Set layout manager to position the items
        // Attach the layout manager to the recycler view
        mRecyclerViewFollowing.setLayoutManager(layoutManager);

        mRecyclerViewFollowing.addOnScrollListener(
                new EndlessRecyclerViewScrollListener(layoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        // Triggered only when new data needs to be appended to the list
                        // Add whatever code is needed to append new items to the bottom of the list
                        Toast.makeText(getApplicationContext(),
                                "Loading more...", Toast.LENGTH_SHORT).show();
                        // Send an API request to retrieve appropriate data using the offset value as a parameter.
                        // Deserialize API response and then construct new objects to append to the adapter
                        // Add the new objects to the data source for the adapter
                        // For efficiency purposes, notify the adapter of only the elements that got changed
                        // curSize will equal to the index of the first element inserted because the list is 0-indexed
                        populateFollowing(true, false);

                    }
                });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateFollowing(false, true);
            }
        });

        mTwitterClient = TwitterApplication.getRestClient(); //singleton client

        //Check for internet
        if (!isNetworkAvailable()) {
            Toast.makeText(getApplicationContext(), "Opps looks like network connectivity problem",
                    Toast.LENGTH_LONG).show();
            //TODO launch activity and show failure droid
        } else if (!isOnline()) {
            Toast.makeText(getApplicationContext(), "Your device is not online, " +
                            "check wifi and try again!",
                    Toast.LENGTH_LONG).show();
        } else {
            mUsers.clear();
            mRecyclerViewFollowingAdapter.notifyDataSetChanged();
            //kick off realtime timelines
            populateFollowing(false, false);
        }
    }

    //Send an API request to get the timeline json
    // Fill the view by creating the tweet objects from the json

    //[] == JsonArray
    private void populateFollowing(final boolean isScrolled, final boolean isRefreshed) {
        mTwitterClient.getFollowings(screenName, isRefreshed? -1: cursor,
                new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        List<User> fetchedUsers = new ArrayList<>();
                        if (responseString != null) {
                            Log.i("FollowingActivity", responseString);
                            try {
                                Gson gson = new GsonBuilder().create();
                                JsonObject jsonObject = gson.fromJson(responseString, JsonObject.class);
                                if (jsonObject.has(NEXT_CURSOR)) {
                                    cursor = Long.parseLong(jsonObject.get(NEXT_CURSOR).getAsString());
                                }
                                JsonArray jsonUsersArray = jsonObject.getAsJsonArray(USERS);

                                if (jsonUsersArray != null) {
                                    for (int i = 0; i < jsonUsersArray.size(); i++) {
                                        JsonObject jsonUserObject = jsonUsersArray.get(i).getAsJsonObject();

                                        if (jsonUserObject != null) {
                                            fetchedUsers.add(User.fromJsonObjectToUser(jsonUserObject));
                                        }
                                    }
                                    Log.i("FollowingActivity", fetchedUsers.size() + " users found");

                                    //add to list
                                    mUsers.addAll(fetchedUsers);
                                    Log.i("TimelineActivity", mUsers.getFirst().getIdStr() + " max id");
                                    Log.i("TimelineActivity", mUsers.getLast().getIdStr() + " since id");
                                    Log.i("TimelineActivity", mUsers.size() + " users found");
                                }
                            } catch (JsonParseException e) {
                                Log.d("Async onSuccess", "Json parsing error:" + e.getMessage(), e);
                            }

                            //notify adapter
                            if (isScrolled) {
                                mRecyclerViewFollowingAdapter.notifyItemRangeInserted(
                                        mRecyclerViewFollowingAdapter.getItemCount(),
                                        fetchedUsers.size());
                            } else if (isRefreshed) {
                                mRecyclerViewFollowingAdapter.notifyDataSetChanged();
                                // Now we call setRefreshing(false) to signal refresh has finished
                                mSwipeRefreshLayout.setRefreshing(false);

                            } else {
                                mRecyclerViewFollowingAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString,
                                          Throwable throwable) {
                        Log.w("AsyncHttpClient", "HTTP Request failure: " + statusCode + " " +
                                throwable.getMessage());
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    protected boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (InterruptedException | IOException e) { e.printStackTrace(); }
        return false;
    }
}
