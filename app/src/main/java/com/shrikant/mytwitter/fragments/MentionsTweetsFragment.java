package com.shrikant.mytwitter.fragments;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import com.loopj.android.http.TextHttpResponseHandler;
import com.shrikant.mytwitter.R;
import com.shrikant.mytwitter.adapters.ComplexRecyclerViewMentionsTweetsAdapter;
import com.shrikant.mytwitter.adapters.DividerItemDecoration;
import com.shrikant.mytwitter.adapters.EndlessRecyclerViewScrollListener;
import com.shrikant.mytwitter.tweetmodels.Tweet;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by spandhare on 2/22/16.
 */
public class MentionsTweetsFragment extends TweetsFragment {
    private LinearLayoutManager mMentionsLinearlayout;
    private LinkedList<Tweet> mMentionTweets;
    private ComplexRecyclerViewMentionsTweetsAdapter mComplexRecyclerViewMentionsAdapter;

    @Bind(R.id.rvMentionsTweets) RecyclerView mRecyclerViewMentionTweets;
    @Bind(R.id.swipeContainerMentions) SwipeRefreshLayout mMentionsSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreateView(inflater, parent, savedInstanceState);
        View mentionsView = inflater.inflate(R.layout.fragment_mentions_tweets, parent, false);
        ButterKnife.bind(this, mentionsView);

        mRecyclerViewMentionTweets.setAdapter(mComplexRecyclerViewMentionsAdapter);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST);
        mRecyclerViewMentionTweets.addItemDecoration(itemDecoration);

        // Setup layout manager for items
        mMentionsLinearlayout = new LinearLayoutManager(getActivity());
        // Control orientation of the items
        // also supports LinearLayoutManager.HORIZONTAL
        mMentionsLinearlayout.setOrientation(LinearLayoutManager.VERTICAL);
        // Optionally customize the position you want to default scroll to
        mMentionsLinearlayout.scrollToPosition(0);
        // Set layout manager to position the items
        // Attach the layout manager to the recycler view

        mRecyclerViewMentionTweets.setLayoutManager(mMentionsLinearlayout);

        mRecyclerViewMentionTweets.addOnScrollListener(
                new EndlessRecyclerViewScrollListener(mMentionsLinearlayout) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount) {
                        // Triggered only when new data needs to be appended to the list
                        // Add whatever code is needed to append new items to the bottom of the list
                        Toast.makeText(getContext(),
                                "Loading more...", Toast.LENGTH_SHORT).show();
                        // Send an API request to retrieve appropriate data using the offset value as a parameter.
                        // Deserialize API response and then construct new objects to append to the adapter
                        // Add the new objects to the data source for the adapter
                        // For efficiency purposes, notify the adapter of only the elements that got changed
                        // curSize will equal to the index of the first element inserted because the list is 0-indexed
                        populateTimeLine(true, false);

                    }
                });

        mMentionsSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimeLine(false, true);
            }
        });

        if (!isOnline()) {
            Toast.makeText(getContext(), "Your device is not online, " +
                            "check wifi and try again!",
                    Toast.LENGTH_LONG).show();
        } else {
            mMentionTweets.clear();
            mComplexRecyclerViewMentionsAdapter.notifyDataSetChanged();
            //kick off realtime timelines
            populateTimeLine(false, false);
        }

        return mentionsView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMentionTweets = new LinkedList<>();
        mComplexRecyclerViewMentionsAdapter = new ComplexRecyclerViewMentionsTweetsAdapter(getActivity(),
                mMentionTweets);
    }

    @Override
    void populateTimeLine(final boolean isScrolled, final boolean isRefreshed) {
        mTwitterClient.getMentionsTimeline(
            !mMentionTweets.isEmpty() ? Long.parseLong(mMentionTweets.getLast().getIdStr()) - 1 : 1,
            !mMentionTweets.isEmpty() ? Long.parseLong(mMentionTweets.getFirst().getIdStr()) : 1,
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

                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JsonObject jsonTweetObject = jsonArray.get(i).getAsJsonObject();

                                    if (jsonTweetObject != null) {
                                        fetchedTweets.add(Tweet.fromJsonObjectToTweet(jsonTweetObject));
                                    }
                                }
                                Log.i("TimelineActivity", fetchedTweets.size() + " tweets found");

                                //add to list
                                if (isRefreshed) {
                                    Log.i("TimelineActivity", fetchedTweets.size() + " new tweets " +
                                            "found");
                                    for (int i = fetchedTweets.size() - 1; i >= 0; i--) {
                                        mMentionTweets.addFirst(fetchedTweets.get(i));
                                    }
                                } else {
                                    mMentionTweets.addAll(fetchedTweets);
                                }
                                Log.i("TimelineActivity", mMentionTweets.getFirst().getIdStr() + " max id");
                                Log.i("TimelineActivity", mMentionTweets.getLast().getIdStr() + " since id");
                                Log.i("TimelineActivity", mMentionTweets.size() + " tweets found");
                            }
                        } catch (JsonParseException e) {
                            Log.d("Async onSuccess", "Json parsing error:" + e.getMessage(), e);
                        }

                        //notify adapter
                        if (isScrolled) {
                            mComplexRecyclerViewMentionsAdapter.notifyItemRangeInserted(
                                    mComplexRecyclerViewMentionsAdapter.getItemCount(),
                                    fetchedTweets.size());
                        } else if (isRefreshed) {
                            mComplexRecyclerViewMentionsAdapter.notifyItemRangeInserted(0,
                                    fetchedTweets.size());
                            //mMentionsLinearlayout.scrollToPosition(0);
                            // Now we call setRefreshing(false) to signal refresh has finished
                            mMentionsSwipeRefreshLayout.setRefreshing(false);

                        } else {
                            mComplexRecyclerViewMentionsAdapter.notifyDataSetChanged();
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
    public void onResume() {
        super.onResume();
        Log.i("Mentions", "Mentions resume");
    }
}
