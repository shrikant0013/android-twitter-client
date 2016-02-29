package com.shrikant.mytwitter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import com.loopj.android.http.TextHttpResponseHandler;
import com.shrikant.mytwitter.adapters.DividerItemDecoration;
import com.shrikant.mytwitter.adapters.EndlessRecyclerViewScrollListener;
import com.shrikant.mytwitter.adapters.RecyclerViewMessagesAdapter;
import com.shrikant.mytwitter.fragments.MessageComposeFragment;
import com.shrikant.mytwitter.tweetmodels.Message;

import org.apache.http.Header;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MessagesActivity extends AppCompatActivity
            implements MessageComposeFragment.OnMessageComposedListener{

    private static final String STATUSES = "statuses";
    private static final String SINCE_ID_STR = "since_id_str";
    private static final String MAX_ID_STR = "max_id_str";

    private TwitterClient mTwitterClient;
    LinearLayoutManager layoutManager;
    LinkedList<Message> mMessages;
    RecyclerViewMessagesAdapter mRecyclerViewMessagesAdapter;

    @Bind(R.id.rvMessages) RecyclerView mRecyclerViewMessages;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.swipeContainerMessages) SwipeRefreshLayout mSwipeRefreshLayout;

    String sinceId = "1";
    String maxId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                MessageComposeFragment messageComposeFragment = MessageComposeFragment.newInstance();
                FragmentManager fm = getSupportFragmentManager();
                messageComposeFragment.show(fm, "message_compose");
            }
        });

        getSupportActionBar().setLogo(R.mipmap.ic_twitter_white);
        getSupportActionBar().setTitle("Messages");

        mMessages = new LinkedList<>();
        mRecyclerViewMessagesAdapter = new RecyclerViewMessagesAdapter(this, mMessages);
        mRecyclerViewMessages.setAdapter(mRecyclerViewMessagesAdapter);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        mRecyclerViewMessages.addItemDecoration(itemDecoration);

        // Setup layout manager for items
        layoutManager = new LinearLayoutManager(this);
        // Control orientation of the items
        // also supports LinearLayoutManager.HORIZONTAL
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        // Optionally customize the position you want to default scroll to
        layoutManager.scrollToPosition(0);
        // Set layout manager to position the items
        // Attach the layout manager to the recycler view
        mRecyclerViewMessages.setLayoutManager(layoutManager);

        mRecyclerViewMessages.addOnScrollListener(
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
                        populateMessages(true, false);

                    }
                });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateMessages(false, true);
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
            mMessages.clear();
            mRecyclerViewMessagesAdapter.notifyDataSetChanged();
            //kick off realtime timelines
            populateMessages(false, false);
        }
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

    protected boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (InterruptedException | IOException e) { e.printStackTrace(); }
        return false;
    }

    void populateMessages(final boolean isScrolled, final boolean isRefreshed) {
        mTwitterClient.getMessages(
                !mMessages.isEmpty() ? Long.parseLong(mMessages.getLast().getIdStr()) - 1 : 1,
                !mMessages.isEmpty() ? Long.parseLong(mMessages.getFirst().getIdStr()) : 1,
            isScrolled, isRefreshed,
            new TextHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    List<Message> fetchedMessages = new ArrayList<>();
                    if (responseString != null) {
                        Log.i("MessagesActivity", responseString);
                        try {
                            Gson gson = new GsonBuilder().create();
                            JsonArray jsonArray = gson.fromJson(responseString, JsonArray.class);

                            if (jsonArray != null) {

                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JsonObject jsonMessageObject = jsonArray.get(i).getAsJsonObject();

                                    if (jsonMessageObject != null) {
                                        fetchedMessages.add(Message.fromJsonObjectToMessage(jsonMessageObject));
                                    }
                                }
                                Log.i("MessagesActivity", fetchedMessages.size() + " messages found");

                                //add to list
                                if (isRefreshed) {
                                    Log.i("MessagesActivity", fetchedMessages.size() + " new messages " +
                                            "found");
                                    for (int i = fetchedMessages.size() - 1; i >= 0; i--) {
                                        mMessages.addFirst(fetchedMessages.get(i));
                                    }
                                } else {
                                    mMessages.addAll(fetchedMessages);
                                }
                                Log.i("MessagesActivity", mMessages.getFirst().getIdStr() + " max id");
                                Log.i("MessagesActivity", mMessages.getLast().getIdStr() + " since id");
                                Log.i("MessagesActivity", mMessages.size() + " messages found");
                            }
                        } catch (JsonParseException e) {
                            Log.d("Async onSuccess", "Json parsing error:" + e.getMessage(), e);
                        }

                        //notify adapter
                        if (isScrolled) {
                            mRecyclerViewMessagesAdapter.notifyItemRangeInserted(
                                    mRecyclerViewMessagesAdapter.getItemCount(),
                                    fetchedMessages.size());
                        } else if (isRefreshed) {
                            mRecyclerViewMessagesAdapter.notifyItemRangeInserted(0,
                                    fetchedMessages.size());
                            //layoutManager.scrollToPosition(0);
                            // Now we call setRefreshing(false) to signal refresh has finished
                            mSwipeRefreshLayout.setRefreshing(false);

                        } else {
                            mRecyclerViewMessagesAdapter.notifyDataSetChanged();
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
    public void updateMessages(Message composedMessage) {
        mMessages.addFirst(composedMessage);
        mRecyclerViewMessagesAdapter.notifyItemRangeInserted(0,
                    1);
        layoutManager.scrollToPosition(0);
    }
}