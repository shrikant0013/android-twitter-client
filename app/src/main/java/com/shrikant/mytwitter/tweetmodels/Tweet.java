package com.shrikant.mytwitter.tweetmodels;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by spandhare on 2/20/16.
 */

@Table(name = "Tweets")
public class Tweet extends Model implements Parcelable {

    public static final String CREATED_AT = "created_at";
    public static final String ID_STR = "id_str";
    public static final String TEXT = "text";
    public static final String FAVOURITE_COUNT = "favorite_count";
    public static final String RETWEET_COUNT = "retweet_count";
    public static final String MEDIA = "media";
    public static final String TYPE = "type";
    public static final String MEDIA_URL = "media_url";
    public static final String EXTENDED_ENTITIES = "extended_entities";
    public static final String VIDEO = "video";
    public static final String PHOTO = "video";
    public static final String VIDEO_INFO = "video_info";
    public static final String VARIANTS = "variants";
    public static final String CONTENT_TYPE = "content_type";
    public static final String URL = "url";
    public static final String USER = "user";
    public static final String ENTITIES = "entities";
    public static final String VIDEO_MP4 = "video/mp4";

    @Column(name = "IdStr")
    private String idStr = "";

    @Column(name = "Created_At")
    private String created_at = "";

    @Column(name = "Text")
    private String text = "";

    @Column(name = "Retweet_Count")
    private String retweet_count = "";

    @Column(name = "Favourite_Count")
    private String favourite_count = "";

    @Column(name = "Media_Type")
    private String media_type = "";

    @Column(name = "Media_Url")
    private String media_url = "";

    @Column(name = "Video_Url")
    private String video_url = "";

    @Column(name = "Media_Content_Type")
    private String media_content_type = "";

//    @Column(name = "User",  onUpdate = Column.ForeignKeyAction.CASCADE,
//            onDelete = Column.ForeignKeyAction.CASCADE)
    private User user;

    @Column(name = "UserId")
    long user_id;

    public Tweet() {
        super();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getRetweet_count() {
        return retweet_count;
    }

    public void setRetweet_count(String retweet_count) {
        this.retweet_count = retweet_count;
    }

    public String getFavourite_count() {
        return favourite_count;
    }

    public void setFavourite_count(String favourite_count) {
        this.favourite_count = favourite_count;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getMedia_content_type() {
        return media_content_type;
    }

    public void setMedia_content_type(String media_content_type) {
        this.media_content_type = media_content_type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


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

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public static Tweet fromJsonObjectToTweet(JsonObject jsonTweetObject) {
        Tweet tweet = new Tweet();
        if (jsonTweetObject.has(ID_STR)) {
            tweet.setIdStr(jsonTweetObject.get(ID_STR).getAsString());
        }
        if (jsonTweetObject.has(TEXT)) {
            tweet.setText(jsonTweetObject.get(TEXT).getAsString());
        }
        if (jsonTweetObject.has(CREATED_AT)) {
            tweet.setCreated_at(jsonTweetObject.get(CREATED_AT).getAsString());
        }
        if (jsonTweetObject.has(FAVOURITE_COUNT)) {
            tweet.setFavourite_count(jsonTweetObject.get(FAVOURITE_COUNT).getAsString());
        }
        if (jsonTweetObject.has(RETWEET_COUNT)) {
            tweet.setRetweet_count(jsonTweetObject.get(RETWEET_COUNT).getAsString());
        }

        if (jsonTweetObject.has(USER)) {
            User newUser = User.fromJsonObjectToUser(jsonTweetObject.get(USER).getAsJsonObject());
            tweet.setUser(newUser);
            tweet.setUser_id(newUser.getUser_id());
        }

        if (jsonTweetObject.has(ENTITIES)) {
            JsonObject jsonEntitiesObject = jsonTweetObject.get(ENTITIES).getAsJsonObject();

            if (jsonEntitiesObject != null && jsonEntitiesObject.has(MEDIA)) {
                JsonArray jsonMediaArray = jsonEntitiesObject.get(MEDIA).getAsJsonArray();

                if (jsonMediaArray != null && jsonMediaArray.size() > 0) {
                    JsonObject jsonMediaObject = jsonMediaArray.get(0).getAsJsonObject();

                    if (jsonMediaObject != null && jsonMediaObject.has(MEDIA_URL)) {
                        tweet.setMedia_url(jsonMediaObject.get(MEDIA_URL).getAsString());
                    }
                    if (jsonMediaObject != null && jsonMediaObject.has(TYPE)) {
                        tweet.setMedia_type(jsonMediaObject.get(TYPE).getAsString());
                    }
                }
            }
        }

        if (jsonTweetObject.has(EXTENDED_ENTITIES)) {
            JsonObject jsonExtendedEntitiesObject = jsonTweetObject.get(EXTENDED_ENTITIES)
                    .getAsJsonObject();

            if (jsonExtendedEntitiesObject != null && jsonExtendedEntitiesObject.has(MEDIA)) {
                JsonArray jsonMediaArray = jsonExtendedEntitiesObject.get(MEDIA).getAsJsonArray();

                if (jsonMediaArray != null && jsonMediaArray.size() > 0) {
                    JsonObject jsonMediaObject = jsonMediaArray.get(0).getAsJsonObject();

                    if (jsonMediaObject != null && jsonMediaObject.has(MEDIA_URL)) {

                    }

                    if (jsonMediaObject != null && jsonMediaObject.has(VIDEO_INFO)) {
                        tweet.setMedia_type(jsonMediaObject.get(TYPE).getAsString());
                        JsonObject jsonVideoInfoObject = jsonMediaObject.get(VIDEO_INFO)
                                .getAsJsonObject();

                        if (jsonVideoInfoObject != null && jsonVideoInfoObject.has(VARIANTS)) {
                            JsonArray jsonVariantsArray = jsonVideoInfoObject.get(VARIANTS)
                                    .getAsJsonArray();

                            if (jsonVariantsArray != null && jsonVariantsArray.size() > 0) {

                                for (int i = 0; i < jsonVariantsArray.size() ; i++) {
                                    JsonObject jsonVariantObject = jsonVariantsArray.get(i)
                                            .getAsJsonObject();

                                    if (jsonVariantObject != null &&
                                            jsonVariantObject.has(CONTENT_TYPE)) {
                                        if (jsonVariantObject.get(CONTENT_TYPE).getAsString()
                                                .equals(VIDEO_MP4)){
                                            tweet.setVideo_url(jsonVariantObject.get(URL)
                                                    .getAsString());
                                            tweet.setMedia_content_type(
                                                    jsonVariantObject.get(CONTENT_TYPE)
                                                            .getAsString());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return tweet;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.idStr);
        dest.writeString(this.created_at);
        dest.writeString(this.text);
        dest.writeString(this.retweet_count);
        dest.writeString(this.favourite_count);
        dest.writeString(this.media_type);
        dest.writeString(this.media_url);
        dest.writeString(this.video_url);
        dest.writeString(this.media_content_type);
        dest.writeParcelable(this.user, flags);
    }

    protected Tweet(Parcel in) {
        this.idStr = in.readString();
        this.created_at = in.readString();
        this.text = in.readString();
        this.retweet_count = in.readString();
        this.favourite_count = in.readString();
        this.media_type = in.readString();
        this.media_url = in.readString();
        this.video_url = in.readString();
        this.media_content_type = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
