package com.shrikant.mytwitter.tweetmodels;

import com.google.gson.JsonObject;

/**
 * Created by spandhare on 2/28/16.
 */
public class Message {

    public static final String CREATED_AT = "created_at";
    public static final String ID_STR = "id_str";
    public static final String TEXT = "text";
    public static final String SENDER = "sender";

    private String idStr = "";

    private String created_at = "";

    private String text = "";

    private User user;

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static Message fromJsonObjectToMessage(JsonObject jsonMessageObject) {
        Message message = new Message();

        if (jsonMessageObject.has(ID_STR)) {
            message.setIdStr(jsonMessageObject.get(ID_STR).getAsString());
        }
        if (jsonMessageObject.has(TEXT)) {
            message.setText(jsonMessageObject.get(TEXT).getAsString());
        }
        if (jsonMessageObject.has(CREATED_AT)) {
            message.setCreated_at(jsonMessageObject.get(CREATED_AT).getAsString());
        }

        if (jsonMessageObject.has(SENDER)) {
            User newUser = User.fromJsonObjectToUser(jsonMessageObject.get(SENDER).getAsJsonObject());
            message.setUser(newUser);
        }

        return message;
    }
}
