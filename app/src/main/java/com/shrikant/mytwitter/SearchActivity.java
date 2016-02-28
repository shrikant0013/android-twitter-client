package com.shrikant.mytwitter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import com.loopj.android.http.TextHttpResponseHandler;
import com.shrikant.mytwitter.adapters.ComplexRecyclerViewTweetsAdapter;
import com.shrikant.mytwitter.adapters.DividerItemDecoration;
import com.shrikant.mytwitter.adapters.EndlessRecyclerViewScrollListener;
import com.shrikant.mytwitter.tweetmodels.Tweet;

import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    private static final String STATUSES = "statuses";
    private static final String SINCE_ID_STR = "since_id_str";
    private static final String MAX_ID_STR = "max_id_str";

    private TwitterClient mTwitterClient;
    LinearLayoutManager layoutManager;

    LinkedList<Tweet> mTweets;
    ComplexRecyclerViewTweetsAdapter mComplexRecyclerViewTweetsAdapter;

    @Bind(R.id.rvSearch) RecyclerView mRecyclerViewTweets;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.swipeContainerSearch) SwipeRefreshLayout mSwipeRefreshLayout;
    String queryString;

    String sinceId = "1";
    String maxId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTweets = new LinkedList<>();
        mComplexRecyclerViewTweetsAdapter = new ComplexRecyclerViewTweetsAdapter(this, mTweets);
        mRecyclerViewTweets.setAdapter(mComplexRecyclerViewTweetsAdapter);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        mRecyclerViewTweets.addItemDecoration(itemDecoration);

        // Setup layout manager for items
        layoutManager = new LinearLayoutManager(this);
        // Control orientation of the items
        // also supports LinearLayoutManager.HORIZONTAL
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // Optionally customize the position you want to default scroll to
        layoutManager.scrollToPosition(0);
        // Set layout manager to position the items
        // Attach the layout manager to the recycler view
        mRecyclerViewTweets.setLayoutManager(layoutManager);

        mRecyclerViewTweets.addOnScrollListener(
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
                        populateTweetsFromSearch(queryString, true, false);

                    }
                });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTweetsFromSearch(queryString, false, true);
            }
        });

        mTwitterClient = TwitterApplication.getRestClient(); //singleton client
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.expandActionView();
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.setQueryHint(getString(R.string.search));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                queryString = query;
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
                    populateTweetsFromSearch(query, false, false);
                }

                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void populateTweetsFromSearch(String query, final boolean isScrolled,
                                          final boolean isRefreshed ) {
        mTwitterClient.searchTweets(query,
                Long.parseLong(maxId), Long.parseLong(sinceId),
                isScrolled, isRefreshed,
                new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        List<Tweet> fetchedTweets = new ArrayList<Tweet>();
                        if (responseString != null) {
                            Log.i("SearchActivity", responseString);
                            try {
                                Gson gson = new GsonBuilder().create();
                                JsonObject jsonSearchObject = gson.fromJson(responseString, JsonObject.class);

                                if (jsonSearchObject != null) {
                                    if (jsonSearchObject.has(SINCE_ID_STR)) {
                                        sinceId = jsonSearchObject.get(SINCE_ID_STR).getAsString();
                                    }
                                    if (jsonSearchObject.has(MAX_ID_STR)) {
                                        maxId = jsonSearchObject.get(MAX_ID_STR).getAsString();
                                    }
                                }

                                if (jsonSearchObject != null && jsonSearchObject.has(STATUSES)) {

                                    JsonArray jsonArray = jsonSearchObject.get(STATUSES).getAsJsonArray();

                                    if (jsonArray != null) {

                                        for (int i = 0; i < jsonArray.size(); i++) {
                                            JsonObject jsonTweetObject = jsonArray.get(i).getAsJsonObject();

                                            if (jsonTweetObject != null) {
                                                fetchedTweets.add(Tweet.fromJsonObjectToTweet(jsonTweetObject));
                                            }
                                        }
                                        Log.i("SearchActivity", fetchedTweets.size() + " tweets found");

                                        //add to list
                                        if (isRefreshed) {
                                            Log.i("SearchActivity", fetchedTweets.size() + " new tweets " +
                                                    "found");
                                            for (int i = fetchedTweets.size() - 1; i >= 0; i--) {
                                                mTweets.addFirst(fetchedTweets.get(i));
                                            }
                                        } else {
                                            mTweets.addAll(fetchedTweets);
                                        }
                                        Log.i("SearchActivity", mTweets.getFirst().getIdStr() + " max id");
                                        Log.i("SearchActivity", mTweets.getLast().getIdStr() + " since id");
                                        Log.i("SearchActivity", mTweets.size() + " tweets found");
                                    }
                                }
                            } catch (JsonParseException e) {
                                Log.d("Async onSuccess", "Json parsing error:" + e.getMessage(), e);
                            }

                            //notify adapter
                            if (isScrolled) {
                                mComplexRecyclerViewTweetsAdapter.notifyItemRangeInserted(
                                        mComplexRecyclerViewTweetsAdapter.getItemCount(),
                                        fetchedTweets.size());
                            } else if (isRefreshed) {
                                mComplexRecyclerViewTweetsAdapter.notifyItemRangeInserted(0,
                                        fetchedTweets.size());
                                //layoutManager.scrollToPosition(0);
                                // Now we call setRefreshing(false) to signal refresh has finished
                                mSwipeRefreshLayout.setRefreshing(false);

                            } else {
                                mComplexRecyclerViewTweetsAdapter.notifyDataSetChanged();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (InterruptedException | IOException e) { e.printStackTrace(); }
        return false;
    }

    public void showProfile(View view) {
        Intent i = new Intent(this, ProfileActivity.class);
        Log.i("SearchActivity", "Clicked user profile is: " + (String)view.getTag());
        i.putExtra("screen_name", (String)view.getTag());
        startActivity(i);
    }
}
