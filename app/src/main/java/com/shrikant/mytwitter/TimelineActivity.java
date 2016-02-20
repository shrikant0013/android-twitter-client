package com.shrikant.mytwitter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import com.loopj.android.http.TextHttpResponseHandler;
import com.shrikant.mytwitter.adapters.ComplexRecyclerViewArticleAdapter;
import com.shrikant.mytwitter.adapters.DividerItemDecoration;
import com.shrikant.mytwitter.adapters.EndlessRecyclerViewScrollListener;
import com.shrikant.mytwitter.models.Tweet;

import org.apache.http.Header;

import android.content.Intent;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TimelineActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 200;
    private TwitterClient mTwitterClient;
    LinearLayoutManager layoutManager;

    LinkedList<Tweet> mTweets;
    ComplexRecyclerViewArticleAdapter mComplexRecyclerViewArticleAdapter;

    @Bind(R.id.rvTweets) RecyclerView mRecyclerViewTweets;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.swipeContainer)  SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        //mTweets = new ArrayList<>();
        mTweets = new LinkedList<>();
        mComplexRecyclerViewArticleAdapter = new ComplexRecyclerViewArticleAdapter(this, mTweets);
        mRecyclerViewTweets.setAdapter(mComplexRecyclerViewArticleAdapter);

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
                        populateTimeLine(true, false);

                    }
                });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimeLine(false, true);
            }
        });

        mTwitterClient = TwitterApplication.getRestClient(); //singleton client

        //kick off the timelines
        populateTimeLine(false, false);

    }

    //Send an API request to get the timeline json
    // Fill the view by creating the tweet objects from the json

    //[] == JsonArray
    private void populateTimeLine(final boolean isScrolled, final boolean isRefreshed) {
        mTwitterClient.getHomeTimeline(
                !mTweets.isEmpty()? Long.parseLong(mTweets.getLast().getIdStr()) - 1 : 1,
                !mTweets.isEmpty()? Long.parseLong(mTweets.getFirst().getIdStr()) : 1,
                isScrolled, isRefreshed,
                new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                List<Tweet> fetchedTweets = new ArrayList<Tweet>();
                if (responseString != null) {
                    Log.i("TimelineActivity", responseString);
                    try {
                        Gson gson = new GsonBuilder().create();
                        JsonArray jsonArray = gson.fromJson(responseString, JsonArray.class);

                        if (jsonArray != null) {
                            Type collectionType = new TypeToken<List<Tweet>>() {
                            }.getType();
                            fetchedTweets = gson.fromJson(jsonArray, collectionType);
                            Log.i("TimelineActivity", fetchedTweets.size() + " tweets found");

                            //add to list
                            if (isRefreshed) {
                                Log.i("TimelineActivity", fetchedTweets.size() + " new tweets " +
                                        "found");
                                for (int i = fetchedTweets.size() - 1; i >= 0; i--) {
                                    mTweets.addFirst(fetchedTweets.get(i));
                                }
                            } else {
                                mTweets.addAll(fetchedTweets);
                            }
                            Log.i("TimelineActivity", mTweets.getFirst().getIdStr() + " max id");
                            Log.i("TimelineActivity", mTweets.getLast().getIdStr() + " since id");
                            Log.i("TimelineActivity", mTweets.size() + " tweets found");
                        }
                    }catch (JsonParseException e) {
                        Log.d("Async onSuccess", "Json parsing error:" + e.getMessage(), e);
                    }

                    //notify adapter
                    if (isScrolled) {
                        mComplexRecyclerViewArticleAdapter.notifyItemRangeInserted(
                                mComplexRecyclerViewArticleAdapter.getItemCount(),
                                fetchedTweets.size());
                    } else if(isRefreshed) {
                        mComplexRecyclerViewArticleAdapter.notifyItemRangeInserted(0,
                                fetchedTweets.size());
                        //layoutManager.scrollToPosition(0);
                        // Now we call setRefreshing(false) to signal refresh has finished
                        mSwipeRefreshLayout.setRefreshing(false);

                    } else {
                        mComplexRecyclerViewArticleAdapter.notifyDataSetChanged();
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
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miCompose:
                composeTweet();

        }
        return super.onOptionsItemSelected(item);
    }

    public void composeTweet() {
        Toast.makeText(this, "Composed clicked", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("TimelineActivity", "Back to onactivity result");
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            Tweet tweet = (Tweet) data.getExtras().get("tweet");
            int code = data.getExtras().getInt("code", 0);
            // Toast the name to display temporarily on screen
            Log.i("TimelineActivity", tweet.getText().toString());
            Toast.makeText(this, tweet.getText().toString(), Toast.LENGTH_SHORT).show();

            mTweets.addFirst(tweet);
            //int curSize = mComplexRecyclerViewArticleAdapter.getItemCount();
            mComplexRecyclerViewArticleAdapter.notifyItemRangeInserted(0,
                    1);
            layoutManager.scrollToPosition(0);
        }
    }
}
