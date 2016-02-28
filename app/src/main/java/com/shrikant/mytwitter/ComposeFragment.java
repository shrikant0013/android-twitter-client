package com.shrikant.mytwitter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import com.bumptech.glide.Glide;
import com.loopj.android.http.TextHttpResponseHandler;
import com.shrikant.mytwitter.tweetmodels.Tweet;

import org.apache.http.Header;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by spandhare on 2/21/16.
 */
public class ComposeFragment  extends DialogFragment {

    private TwitterClient mTwitterClient;
    @Bind(R.id.btnTweetSent) Button mButtonTweetSend;
    @Bind(R.id.tvCharacterCount) TextView mTextViewCharCount;
    @Bind(R.id.etComposeBody) EditText mEditTextComposeBody;
    @Bind(R.id.ivComposeUserProfileImage) ImageView mImageViewUserProfileImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.content_compose, container, false);
        ButterKnife.bind(this, rootView);

        //getDialog().setTitle("Update Status");

        mTwitterClient = TwitterApplication.getRestClient();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mTextViewCharCount.setTextColor(Color.BLACK);
        mEditTextComposeBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                long length = 0;
                if (s.length() > 140) {
                    mTextViewCharCount.setTextColor(Color.RED);
                    length = 140 - s.length();
                    mButtonTweetSend.setEnabled(false);
                } else {
                    mTextViewCharCount.setTextColor(Color.BLACK);
                    length = s.length();
                    mButtonTweetSend.setEnabled(true);
                }

                mTextViewCharCount.setText("" + length);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        if (TimelineActivity.me != null && !TextUtils.isEmpty(TimelineActivity.me.getMyProfileImageUrl())) {
            Glide.with(getContext()).load(TimelineActivity.me.getMyProfileImageUrl())
                    .placeholder(R.mipmap.ic_wifi)
                    .fitCenter()
                    .into(mImageViewUserProfileImage);
        }
    }


    @OnClick(R.id.btnTweetSent)
    public void sendTweet (View view){
        String tweetText = mEditTextComposeBody.getText().toString();

        mTwitterClient.sendTweet(tweetText, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Tweet composeTweet = new Tweet();
                if (responseString != null) {
                    Log.i("ComposeFragment", responseString);
                    try {
                        Gson gson = new GsonBuilder().create();
                        JsonObject jsonObject = gson.fromJson(responseString, JsonObject.class);
                        if (jsonObject != null) {
                            composeTweet = Tweet.fromJsonObjectToTweet(jsonObject);
                            Log.i("ComposeFragment", composeTweet.getText());
                        }
                    } catch (JsonParseException e) {
                        Log.d("Compose tweet onSuccess", "Json parsing error:" + e.getMessage(), e);
                    }
                }
                //((TimelineActivity) getActivity()).updateStatus(composeTweet);
                dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                Log.w("ComposeActivity", "HTTP Request failure: " + statusCode + " " +
                        throwable.getMessage());
            }
        });
    }

    @OnClick(R.id.ibDismiss)
    public void dismissFragment(View view) {
        dismiss();
    }
}
