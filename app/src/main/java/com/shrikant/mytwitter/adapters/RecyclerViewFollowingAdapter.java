package com.shrikant.mytwitter.adapters;

import com.bumptech.glide.Glide;
import com.shrikant.mytwitter.R;
import com.shrikant.mytwitter.tweetmodels.User;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by spandhare on 2/27/16.
 */
public class RecyclerViewFollowingAdapter
        extends RecyclerView.Adapter<RecyclerViewFollowingAdapter.ViewHolder> {

    List<User> mUsers;
    Context mContext;
    public RecyclerViewFollowingAdapter(Context context, List<User> users ) {
        mUsers = users;
        mContext = context;
    }

    // ... constructor and member variables
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
//        @Bind(R.id.ivFollowUserPhoto) ImageView mImageViewFollowingUserPhoto;
//        @Bind(R.id.tvFollowUserName) TextView mTextViewFollowingUserName;
//        @Bind(R.id.tvFollowUserScreenName) TextView mTextViewFollowingScreenName;
//        @Bind(R.id.tvUserTagLine) TextView mTextViewFollowingTagline;

        ImageView mImageViewFollowingUserPhoto;
        TextView mTextViewFollowingUserName;
        TextView mTextViewFollowingScreenName;
        TextView mTextViewFollowingTagline;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview

        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            //ButterKnife.bind(this, itemView);
            mImageViewFollowingUserPhoto = (ImageView)itemView.findViewById(R.id.ivFollowUserPhoto);
            mTextViewFollowingUserName = (TextView)itemView.findViewById(R.id.tvFollowUserName);
            mTextViewFollowingScreenName = (TextView)itemView.findViewById(R.id.tvFollowUserScreenName);
            mTextViewFollowingTagline = (TextView)itemView.findViewById(R.id.tvFollowUserTagline);
        }
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public RecyclerViewFollowingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View followingView = inflater.inflate(R.layout.following_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(followingView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(RecyclerViewFollowingAdapter.ViewHolder viewHolder,
                                 int position) {
        // Get the data model based on position
        User user = mUsers.get(position);

        viewHolder.mTextViewFollowingUserName.setText(user.getUserName());
        viewHolder.mTextViewFollowingScreenName.setText("@" + user.getTwitterHandle());
        viewHolder.mTextViewFollowingTagline.setText(user.getTagLine());

        if (!TextUtils.isEmpty(user.getProfileImageUrl())) {
            Glide.with(mContext).load(user.getProfileImageUrl()).fitCenter()
                    .into(viewHolder.mImageViewFollowingUserPhoto);
        }
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}
