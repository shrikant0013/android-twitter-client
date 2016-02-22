package com.shrikant.mytwitter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import com.bumptech.glide.Glide;
import com.loopj.android.http.TextHttpResponseHandler;
import com.shrikant.mytwitter.tweetmodels.Tweet;

import org.apache.http.Header;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TweetDetailActivity extends AppCompatActivity {

    private Tweet mTweet;
    private TwitterClient mTwitterClient;

    @Bind(R.id.ivDetails_ProfileImage) ImageView mImageViewDetailsProfileImage;
    @Bind(R.id.tvDetails_UserName) TextView mTextViewDetailsUserName;
    @Bind(R.id.tvDetails_TwitterHandle) TextView mTextViewDetailsTwitterHandle;
    @Bind(R.id.tvDetails_Text) TextView mTextViewDetailsText;
    @Bind(R.id.ivDetails_TweetImage) ImageView mImageViewDetailsTweetImage;
    @Bind(R.id.tvDetails_TimeSend) TextView mTextViewDetailsTimeSend;
    @Bind(R.id.tvDetails_Retweets_Count) TextView mTextViewDetailsRetweetCount;
    @Bind(R.id.tvDetails_Like_Count) TextView mTextViewDetailsLikeCount;
    @Bind(R.id.etDetails_ReplyToText) EditText mEditTextDetailsReplyToText;
    @Bind(R.id.rlDetails_SendAction) RelativeLayout mRelativeLayoutSendAction;
    @Bind(R.id.btnDetails_SendTweet) Button mButtonDetailsTweetSend;
    @Bind(R.id.tvDetails_Character_Count) TextView mTextViewDetailsCharacterCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        Toolbar toolbarDetails = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarDetails);
        ButterKnife.bind(this);

        getSupportActionBar().setLogo(R.mipmap.ic_twitter_logo);
        getSupportActionBar().setTitle("Tweet details..");

        mEditTextDetailsReplyToText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditTextDetailsReplyToText.setText("@" + mTweet.getUser().getTwitterHandle() + " ");
                mEditTextDetailsReplyToText.setSelection(mEditTextDetailsReplyToText.getText().length());
                mRelativeLayoutSendAction.setVisibility(View.VISIBLE);
            }
        });

        mEditTextDetailsReplyToText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                long length = 0;
                if (s.length() > 140) {
                    mTextViewDetailsCharacterCount.setTextColor(Color.RED);
                    length = 140 - s.length();
                    mButtonDetailsTweetSend.setEnabled(false);
                } else {
                    mTextViewDetailsCharacterCount.setTextColor(Color.BLACK);
                    length = s.length();
                    mButtonDetailsTweetSend.setEnabled(true);
                }

                mTextViewDetailsCharacterCount.setText("" + length);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mTwitterClient = TwitterApplication.getRestClient();
        mTweet = getIntent().getParcelableExtra("tweet");
        loadPage();
    }

    private void loadPage() {
        mTextViewDetailsText.setText(mTweet.getText());
        mTextViewDetailsUserName.setText(mTweet.getUser().getUserName());
        mTextViewDetailsTwitterHandle.setText("@" + mTweet.getUser().getTwitterHandle());

        if (!TextUtils.isEmpty(mTweet.getUser().getProfileImageUrl())) {
            Glide.with(this).load(mTweet.getUser().getProfileImageUrl())
                    .placeholder(R.mipmap.ic_wifi)
                    .fitCenter()
                    .into(mImageViewDetailsProfileImage);
        }

        mTextViewDetailsTimeSend.setText(mTweet.getCreated_at());

        if (!TextUtils.isEmpty(mTweet.getMedia_url()) &&
                mTweet.getMedia_type().equals("photo")){
            mImageViewDetailsTweetImage.setVisibility(View.VISIBLE);
            Glide.with(this).load(mTweet.getMedia_url())
                    .placeholder(R.mipmap.ic_wifi)
                    .into(mImageViewDetailsTweetImage);
        }

        mTextViewDetailsRetweetCount.setText(mTweet.getRetweet_count() + " Retweets");
        mTextViewDetailsLikeCount.setText(mTweet.getFavourite_count() + " Likes");

        mEditTextDetailsReplyToText.setText("Reply to " + mTweet.getUser().getUserName());
        mEditTextDetailsReplyToText.setSelection(mEditTextDetailsReplyToText.getText().length());
    }

    @OnClick(R.id.btnDetails_SendTweet)
    public void sendTweet(View view) {
        String tweetText = mEditTextDetailsReplyToText.getText().toString();

        mTwitterClient.sendTweet(tweetText, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Tweet composeTweet = new Tweet();
                if (responseString != null) {
                    Log.i("TweetDetailActivity", responseString);
                    try {
                        Gson gson = new GsonBuilder().create();
                        JsonObject jsonObject = gson.fromJson(responseString, JsonObject.class);
                        if (jsonObject != null) {
                            composeTweet = Tweet.fromJsonObjectToTweet(jsonObject);
                            Log.i("TweetDetailActivity", composeTweet.getText());
                        }
                    } catch (JsonParseException e) {
                        Log.d("TweetDetailActivity ",
                                "Json parsing error:" + e.getMessage(), e);
                    }
                }

                // Prepare data intent
                Intent data = new Intent();
                // Pass relevant data back as a result
                data.putExtra("tweet", composeTweet);
                data.putExtra("code", 200); // ints work too
                // Activity finished ok, return the data
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                Log.w("TweetDetailActivity", "HTTP Request failure: " + statusCode + " " +
                        throwable.getMessage());
            }
        });
    }
}
