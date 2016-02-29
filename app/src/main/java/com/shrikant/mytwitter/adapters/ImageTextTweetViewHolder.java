package com.shrikant.mytwitter.adapters;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import com.loopj.android.http.TextHttpResponseHandler;
import com.shrikant.mytwitter.R;
import com.shrikant.mytwitter.TweetDetailActivity;
import com.shrikant.mytwitter.TwitterApplication;
import com.shrikant.mytwitter.TwitterClient;
import com.shrikant.mytwitter.tweetmodels.Tweet;
import org.apache.http.Header;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Shrikant Pandhare
 */
public class ImageTextTweetViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
    @Bind(R.id.ivTweetImage) ImageView mImageViewTweetImage;
    @Bind(R.id.tvTweetTextWithImage) TextView mTextViewTweetWithImage;
    @Bind(R.id.tvUserName) TextView mTextViewUserName;
    @Bind(R.id.tvTwitterHandle) TextView mTextViewTwitterHandle;
    @Bind(R.id.tvTimeSend) TextView mTextViewTimeSend;
    @Bind(R.id.ivProfileImage) ImageView mImageViewProfileImage;
    @Bind(R.id.ivReplyToTweet) ImageView mImageViewReplyToTweet;
    @Bind(R.id.tvRetweets) TextView mTextViewRetweets;
    @Bind(R.id.tvLikes) TextView mTextViewLikes;
    @Bind(R.id.ivRetweet) ImageView mImageViewReTweet;
    @Bind(R.id.ivLike) ImageView mImageViewLike;

    List<Tweet> mTweets;
    Context mContext;

    public ImageTextTweetViewHolder(Context context, View view, List<Tweet> mTweets) {
        super(view);

        this.mTweets = mTweets;
        this.mContext = context;
        // Attach a click listener to the entire row view
        view.setOnClickListener(this);
        ButterKnife.bind(this, view);

        mImageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TwitterClient twitterClient = TwitterApplication.getRestClient();
                Log.i("ImageTextTweetView", v.getTag().toString());

                if (!TextUtils.isEmpty(v.getTag().toString())) {
                    String statusId = v.getTag().toString();
                    twitterClient.statusLike(statusId, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            String newCount = "";
                            try {
                                Gson gson = new GsonBuilder().create();
                                JsonObject jsonObject = gson.fromJson(responseString, JsonObject.class);
                                if (jsonObject.has("favorite_count")) {
                                    newCount = jsonObject.get("favorite_count").getAsString();
                                    Log.i("Likes call", "count of likes" + newCount);
                                }
                            } catch (JsonParseException e) {
                                Log.d("Async onSuccess", "Json parsing error:" + e.getMessage(), e);
                            }

                            if (!TextUtils.isEmpty(newCount)) {
                                mTextViewLikes.setText(newCount);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.w("AsyncHttpClient", "HTTP Request failure: " + statusCode + " " +
                                    throwable.getMessage());
                        }
                    });
                }
            }
        });

        mImageViewReTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwitterClient twitterClient = TwitterApplication.getRestClient();
                Log.i("ImageTextTweetView", v.getTag().toString());

                if (!TextUtils.isEmpty(v.getTag().toString())) {
                    String statusId = v.getTag().toString();
                    twitterClient.statusRetweet(statusId, new TextHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            String newCount = "";
                            try {
                                Gson gson = new GsonBuilder().create();
                                JsonObject jsonObject = gson.fromJson(responseString, JsonObject.class);
                                if (jsonObject.has("retweet_count")) {
                                    newCount = jsonObject.get("retweet_count").getAsString();
                                    Log.i("Likes call", "count of retweets" + newCount);
                                }
                            } catch (JsonParseException e) {
                                Log.d("Async onSuccess", "Json parsing error:" + e.getMessage(), e);
                            }

                            if (!TextUtils.isEmpty(newCount)) {
                                mTextViewRetweets.setText(newCount);
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            Log.w("AsyncHttpClient", "HTTP Request failure: " + statusCode + " " +
                                    throwable.getMessage());
                        }
                    });
                }
            }
        });
    }

    // Handles the row being being clicked
    @Override
    public void onClick(View view) {

        int position = getLayoutPosition(); // gets item position
        Tweet tweet = mTweets.get(position);
        // We can access the data within the views
        Toast.makeText(mContext, "Loading tweet...", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(mContext, TweetDetailActivity.class);
        i.putExtra("tweet",tweet);
        mContext.startActivity(i);
    }
}
