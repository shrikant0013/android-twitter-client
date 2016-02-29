package com.shrikant.mytwitter.fragments;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import com.loopj.android.http.TextHttpResponseHandler;
import com.shrikant.mytwitter.R;
import com.shrikant.mytwitter.TwitterApplication;
import com.shrikant.mytwitter.TwitterClient;
import com.shrikant.mytwitter.tweetmodels.Message;

import org.apache.http.Header;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by spandhare on 2/28/16.
 */
public class MessageComposeFragment extends DialogFragment {

    private OnMessageComposedListener mOnMessageComposedListener;

    // Define the events that the fragment will use to communicate
    public interface OnMessageComposedListener {
        // This can be any number of events to be sent to the activity
        void updateMessages(Message composedMessage );
    }

    private TwitterClient mTwitterClient;
    @Bind(R.id.btnComposeMessageSend) Button mButtonMessageComposeSend;
    @Bind(R.id.btnComposeMessageCancel) Button mButtonMessageComposeCancel;
    @Bind(R.id.etComposeMessageSendersName) EditText mEditTextMessageComposeSenderName;
    @Bind(R.id.etComposeMessageText) EditText mEditTextMessageComposeText;

    public static MessageComposeFragment newInstance() {
        MessageComposeFragment messageComposeFragment = new MessageComposeFragment();
        return messageComposeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.compose_message, container, false);
        ButterKnife.bind(this, rootView);

        mTwitterClient = TwitterApplication.getRestClient();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.btnComposeMessageSend)
    public void sendComposedMessage(View view) {
        String messageText = mEditTextMessageComposeText.getText().toString();
        String senderName = mEditTextMessageComposeSenderName.getText().toString();

        mTwitterClient.sendMessage(messageText, senderName, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Message newMessage = new Message();
                if (responseString != null) {
                    Log.i("MessageComposeFragment", responseString);
                    try {
                        Gson gson = new GsonBuilder().create();
                        JsonObject jsonObject = gson.fromJson(responseString, JsonObject.class);
                        if (jsonObject != null) {
                            newMessage = Message.fromJsonObjectToMessage(jsonObject);
                            Log.i("MessageComposeFragment", newMessage.getText());
                        }
                    } catch (JsonParseException e) {
                        Log.d("Message onSuccess", "Json parsing error:" + e.getMessage(), e);
                    }
                }
                //((TimelineActivity) getActivity()).updateStatus(composeTweet);
                mOnMessageComposedListener.updateMessages(newMessage);
                dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.w("MessageComposeFragment", "HTTP Request failure: " + statusCode + " " +
                        throwable.getMessage());
            }
        });
    }

    @OnClick(R.id.btnComposeMessageCancel)
    public void cancelMessageCompose(View view) {
        dismiss();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnMessageComposedListener) {
            mOnMessageComposedListener = (OnMessageComposedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement MessageComposeFragment.OnMessageComposedListener");
        }
    }
}
