package com.shrikant.mytwitter.adapters;

import com.bumptech.glide.Glide;
import com.shrikant.mytwitter.R;
import com.shrikant.mytwitter.models.Tweet;
import com.shrikant.mytwitter.utils.Util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Shrikant Pandhare
 */
public class ComplexRecyclerViewArticleAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // The items to display in your RecyclerView
    private static List<Tweet> sTweets;
    private static Context mContext;

    private final int TEXTONLY = 0, TEXT_PLUS_IMAGE = 1;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ComplexRecyclerViewArticleAdapter(Context context, LinkedList<Tweet> tweets) {
        this.sTweets = tweets;
        mContext = context;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.sTweets.size();
    }

    @Override
    public int getItemViewType(int position) {
        Tweet tweet = sTweets.get(position);
//        if (TextUtils.isEmpty(article.getArticleThumbnailUrl())){
//            return TEXTONLY;
//        }
        //return TEXT_PLUS_IMAGE;

        if (tweet.getExtendedEntities() != null && tweet.getExtendedEntities().getMedia() != null
                && tweet.getExtendedEntities().getMedia().size() > 0
                && tweet.getExtendedEntities().getMedia().get(0).getType().equals("photo")) {
            return TEXT_PLUS_IMAGE;
        }

        return TEXTONLY;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case TEXTONLY:
                View v1 = inflater.inflate(R.layout.view_template_textonly_tweet,
                        viewGroup, false);
                viewHolder = new TextOnlyTweetViewHolder(mContext, v1, sTweets);
                break;
            case TEXT_PLUS_IMAGE:
                View v2 = inflater.inflate(R.layout.view_template_image_tweet,
                        viewGroup, false);
                viewHolder = new ImageTextTweetViewHolder(mContext, v2, sTweets);
                break;
            default:
                v2 = inflater.inflate(R.layout.view_template_textonly_tweet,
                        viewGroup, false);
                viewHolder = new ImageTextTweetViewHolder(mContext, v2, sTweets);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case TEXTONLY:
                TextOnlyTweetViewHolder vh1 = (TextOnlyTweetViewHolder) viewHolder;
                configureTextOnlyTweetViewHolder(vh1, position);
                break;
            case TEXT_PLUS_IMAGE:
                ImageTextTweetViewHolder vh2 = (ImageTextTweetViewHolder) viewHolder;
                configureImageTextTweetViewHolder(vh2, position);
                break;
            default:
                ImageTextTweetViewHolder vh = (ImageTextTweetViewHolder) viewHolder;
                configureImageTextTweetViewHolder(vh, position);
                break;
        }
    }

    private void configureTextOnlyTweetViewHolder(TextOnlyTweetViewHolder viewHolder, int position) {
//        Article article = articles.get(position);
//        viewHolder.tvArticleText.setText(article.getHeadline().getMain());
        Tweet tweet = sTweets.get(position);

        viewHolder.mTextViewTweetTextOnly.setText(tweet.getText());
        viewHolder.mTextViewUserName.setText(tweet.getUser().getName());
        viewHolder.mTextViewTwitterHandle.setText( "@" + tweet.getUser().getScreenName());

        if (!TextUtils.isEmpty(tweet.getUser().getProfileImageUrl())) {
            Glide.with(mContext).load(tweet.getUser().getProfileImageUrl())
                    .placeholder(R.mipmap.ic_wifi)
                    .fitCenter()
                    .into(viewHolder.mImageViewProfileImage);
        }

        viewHolder.mTextViewTimeSend.setText(Util.getRelativeTimeAgo(tweet.getCreatedAt()));
    }

    private void configureImageTextTweetViewHolder(ImageTextTweetViewHolder viewHolder,
                                                   int position) {

        Tweet tweet = sTweets.get(position);

        viewHolder.mTextViewTweetWithImage.setText(tweet.getText());

        if (tweet.getExtendedEntities() != null && tweet.getExtendedEntities().getMedia() != null
                && tweet.getExtendedEntities().getMedia().size() > 0
                && tweet.getExtendedEntities().getMedia().get(0).getType().equals("photo")) {

            if (!TextUtils.isEmpty(tweet.getExtendedEntities().getMedia().get(0).getMediaUrl())) {
                Glide.with(mContext).load(tweet.getExtendedEntities().getMedia().get(0).getMediaUrl())
                        .placeholder(R.mipmap.ic_wifi)
                        .into(viewHolder.mImageViewTweetImage);
            }
        }
        viewHolder.mTextViewUserName.setText(tweet.getUser().getName());
        viewHolder.mTextViewTwitterHandle.setText( "@" + tweet.getUser().getScreenName());

        if (!TextUtils.isEmpty(tweet.getUser().getProfileImageUrl())) {
            Glide.with(mContext).load(tweet.getUser().getProfileImageUrl())
                    .placeholder(R.mipmap.ic_wifi)
                    .fitCenter()
                    .into(viewHolder.mImageViewProfileImage);
        }

        viewHolder.mTextViewTimeSend.setText(Util.getRelativeTimeAgo(tweet.getCreatedAt()));
    }

    // Clean all elements of the recycler
    public void clear() {
        sTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items
    public void addAll(List<Tweet> list) {
        sTweets.addAll(list);
        notifyDataSetChanged();
    }
}