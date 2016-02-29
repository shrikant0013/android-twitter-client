package com.shrikant.mytwitter.adapters;

import com.bumptech.glide.Glide;
import com.shrikant.mytwitter.R;
import com.shrikant.mytwitter.tweetmodels.Message;
import com.shrikant.mytwitter.utils.Util;

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
 * Created by spandhare on 2/28/16.
 */
public class RecyclerViewMessagesAdapter
    extends RecyclerView.Adapter<RecyclerViewMessagesAdapter.ViewHolder> {

        List<Message> mMessages;
        Context mContext;
        public RecyclerViewMessagesAdapter(Context context, List<Message> messages) {
            mMessages = messages;
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

            ImageView mImageViewMessageSenderPhoto;
            TextView mTextViewMessageSenderUserName;
            TextView mTextViewMessageSenderScreenName;
            TextView mTextViewMessageText;
            TextView mTextViewMessageTimeSend;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview

            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);
                //ButterKnife.bind(this, itemView);
                mImageViewMessageSenderPhoto = (ImageView)itemView.findViewById(R.id.ivMessageSender);
                mTextViewMessageSenderUserName = (TextView)itemView.findViewById(R.id.tvMessageSenderName);
                mTextViewMessageSenderScreenName = (TextView)itemView.findViewById(R.id.tvMessageSenderScreenName);
                mTextViewMessageText = (TextView)itemView.findViewById(R.id.tvMessageText);
                mTextViewMessageTimeSend = (TextView)itemView.findViewById(R.id.tvMessageTimeSend);

            }
        }

        // Usually involves inflating a layout from XML and returning the holder
        @Override
        public RecyclerViewMessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
        int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View followingView = inflater.inflate(R.layout.message_item, parent, false);

            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(followingView);
            return viewHolder;
        }

        // Involves populating data into the item through holder
        @Override
        public void onBindViewHolder(RecyclerViewMessagesAdapter.ViewHolder viewHolder,
        int position) {
            // Get the data model based on position
            Message message = mMessages.get(position);

            viewHolder.mTextViewMessageSenderUserName.setText(message.getUser().getUserName());
            viewHolder.mTextViewMessageSenderScreenName.setText("@" + message.getUser().getTwitterHandle());
            viewHolder.mTextViewMessageText.setText(message.getText());
            viewHolder.mTextViewMessageTimeSend.setText(Util.getRelativeTimeAgo(message.getCreated_at()));

            if (!TextUtils.isEmpty(message.getUser().getProfileImageUrl())) {
                Glide.with(mContext).load(message.getUser().getProfileImageUrl()).fitCenter()
                        .into(viewHolder.mImageViewMessageSenderPhoto);
            }
        }

        // Return the total count of items
        @Override
        public int getItemCount() {
            return mMessages.size();
        }
}
