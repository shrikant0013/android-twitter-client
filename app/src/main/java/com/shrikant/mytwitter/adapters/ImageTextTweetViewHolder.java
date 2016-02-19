package com.shrikant.mytwitter.adapters;

import com.shrikant.mytwitter.R;
import com.shrikant.mytwitter.models.Tweet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Shrikant Pandhare
 */
public class ImageTextTweetViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.ivTweetImage) ImageView mImageViewTweetImage;
    @Bind(R.id.tvTweetTextWithImage) TextView mTextViewTweetWithImage;
    @Bind(R.id.tvUserName) TextView mTextViewUserName;
    @Bind(R.id.tvTwitterHandle) TextView mTextViewTwitterHandle;
    @Bind(R.id.tvTimeSend) TextView mTextViewTimeSend;
    @Bind(R.id.ivProfileImage) ImageView mImageViewProfileImage;

    List<Tweet> mTweets;
    Context mContext;

    public ImageTextTweetViewHolder(Context context, View view, List<Tweet> mTweets) {
        super(view);

        this.mTweets = mTweets;
        this.mContext = context;
        // Attach a click listener to the entire row view
        //view.setOnClickListener(this);
        ButterKnife.bind(this, view);
    }

    // Handles the row being being clicked
//    @Override
//    public void onClick(View view) {
//        int position = getLayoutPosition(); // gets item position
//        Article article = articles.get(position);
//        // We can access the data within the views
//        Toast.makeText(mContext, "Loading article...", Toast.LENGTH_SHORT).show();
//
//        Intent i = new Intent(mContext, ArticleActivity.class);
//        i.putExtra("webUrl", article.webUrl);
//        mContext.startActivity(i);
//    }
//
//    public ImageView getIvArticle() {
//        return ivArticle;
//    }
//
//    public void setIvArticle(ImageView ivArticle) {
//        this.ivArticle = ivArticle;
//    }
//
//    public TextView getTvArticle() {
//        return tvArticle;
//    }
//
//    public void setTvArticle(TextView tvArticle) {
//        this.tvArticle = tvArticle;
//    }
}
