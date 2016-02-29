package com.shrikant.mytwitter.adapters;

import com.shrikant.mytwitter.R;
import com.shrikant.mytwitter.TweetDetailActivity;
import com.shrikant.mytwitter.tweetmodels.Tweet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
public class TextOnlyTweetViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    @Bind(R.id.tvTweetTextOnly) TextView mTextViewTweetTextOnly;
    @Bind(R.id.tvUserName) TextView mTextViewUserName;
    @Bind(R.id.tvTwitterHandle) TextView mTextViewTwitterHandle;
    @Bind(R.id.tvTimeSend) TextView mTextViewTimeSend;
    @Bind(R.id.ivProfileImage) ImageView mImageViewProfileImage;
    @Bind(R.id.ivReplyToTweet) ImageView mImageViewReplyToTweet;

    List<Tweet> mTweets;
    Context mContext;

    public TextOnlyTweetViewHolder(Context context, View view, List<Tweet> mTweets) {
        super(view);

        this.mTweets = mTweets;
        this.mContext = context;
        // Attach a click listener to the entire row view
        view.setOnClickListener(this);
        ButterKnife.bind(this, view);
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
