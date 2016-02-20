
package com.shrikant.mytwitter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class Tweet implements Parcelable {

    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("id_str")
    @Expose
    private String idStr;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("truncated")
    @Expose
    private Boolean truncated;
    @SerializedName("in_reply_to_status_id")
    @Expose
    private long inReplyToStatusId;
    @SerializedName("in_reply_to_status_id_str")
    @Expose
    private String inReplyToStatusIdStr;
    @SerializedName("in_reply_to_user_id")
    @Expose
    private long inReplyToUserId;
    @SerializedName("in_reply_to_user_id_str")
    @Expose
    private String inReplyToUserIdStr;
    @SerializedName("in_reply_to_screen_name")
    @Expose
    private String inReplyToScreenName;
    @SerializedName("user")
    @Expose
    private User user;
//    @SerializedName("geo")
//    @Expose
//    private Object geo;
//    @SerializedName("coordinates")
//    @Expose
//    private Object coordinates;
//    @SerializedName("place")
//    @Expose
//    private Object place;
//    @SerializedName("contributors")
//    @Expose
//    private Object contributors;
    @SerializedName("is_quote_status")
    @Expose
    private Boolean isQuoteStatus;
    @SerializedName("retweet_count")
    @Expose
    private Integer retweetCount;
    @SerializedName("favorite_count")
    @Expose
    private Integer favoriteCount;
    @SerializedName("entities")
    @Expose
    private Entities_ entities;
    @SerializedName("extended_entities")
    @Expose
    private ExtendedEntities extendedEntities;
    @SerializedName("favorited")
    @Expose
    private Boolean favorited;
    @SerializedName("retweeted")
    @Expose
    private Boolean retweeted;
    @SerializedName("possibly_sensitive")
    @Expose
    private Boolean possiblySensitive;
    @SerializedName("possibly_sensitive_appealable")
    @Expose
    private Boolean possiblySensitiveAppealable;
    @SerializedName("lang")
    @Expose
    private String lang;

    /**
     * 
     * @return
     *     The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * 
     * @param createdAt
     *     The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 
     * @return
     *     The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     * @param id
     *     The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     * @return
     *     The idStr
     */
    public String getIdStr() {
        return idStr;
    }

    /**
     * 
     * @param idStr
     *     The id_str
     */
    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    /**
     * 
     * @return
     *     The text
     */
    public String getText() {
        return text;
    }

    /**
     * 
     * @param text
     *     The text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * 
     * @return
     *     The source
     */
    public String getSource() {
        return source;
    }

    /**
     * 
     * @param source
     *     The source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 
     * @return
     *     The truncated
     */
    public Boolean getTruncated() {
        return truncated;
    }

    /**
     * 
     * @param truncated
     *     The truncated
     */
    public void setTruncated(Boolean truncated) {
        this.truncated = truncated;
    }

    /**
     * 
     * @return
     *     The inReplyToStatusId
     */
    public Object getInReplyToStatusId() {
        return inReplyToStatusId;
    }

    /**
     * 
     * @param inReplyToStatusId
     *     The in_reply_to_status_id
     */
    public void setInReplyToStatusId(Long inReplyToStatusId) {
        this.inReplyToStatusId = inReplyToStatusId;
    }

    /**
     * 
     * @return
     *     The inReplyToStatusIdStr
     */
    public Object getInReplyToStatusIdStr() {
        return inReplyToStatusIdStr;
    }

    /**
     * 
     * @param inReplyToStatusIdStr
     *     The in_reply_to_status_id_str
     */
    public void setInReplyToStatusIdStr(String inReplyToStatusIdStr) {
        this.inReplyToStatusIdStr = inReplyToStatusIdStr;
    }

    /**
     * 
     * @return
     *     The inReplyToUserId
     */
    public Object getInReplyToUserId() {
        return inReplyToUserId;
    }

    /**
     * 
     * @param inReplyToUserId
     *     The in_reply_to_user_id
     */
    public void setInReplyToUserId(Long inReplyToUserId) {
        this.inReplyToUserId = inReplyToUserId;
    }

    /**
     * 
     * @return
     *     The inReplyToUserIdStr
     */
    public Object getInReplyToUserIdStr() {
        return inReplyToUserIdStr;
    }

    /**
     * 
     * @param inReplyToUserIdStr
     *     The in_reply_to_user_id_str
     */
    public void setInReplyToUserIdStr(String inReplyToUserIdStr) {
        this.inReplyToUserIdStr = inReplyToUserIdStr;
    }

    /**
     * 
     * @return
     *     The inReplyToScreenName
     */
    public Object getInReplyToScreenName() {
        return inReplyToScreenName;
    }

    /**
     * 
     * @param inReplyToScreenName
     *     The in_reply_to_screen_name
     */
    public void setInReplyToScreenName(String inReplyToScreenName) {
        this.inReplyToScreenName = inReplyToScreenName;
    }

    /**
     * 
     * @return
     *     The user
     */
    public User getUser() {
        return user;
    }

    /**
     * 
     * @param user
     *     The user
     */
    public void setUser(User user) {
        this.user = user;
    }

//    /**
//     *
//     * @return
//     *     The geo
//     */
//    public Object getGeo() {
//        return geo;
//    }
//
//    /**
//     *
//     * @param geo
//     *     The geo
//     */
//    public void setGeo(Object geo) {
//        this.geo = geo;
//    }
//
//    /**
//     *
//     * @return
//     *     The coordinates
//     */
//    public Object getCoordinates() {
//        return coordinates;
//    }
//
//    /**
//     *
//     * @param coordinates
//     *     The coordinates
//     */
//    public void setCoordinates(Object coordinates) {
//        this.coordinates = coordinates;
//    }
//
//    /**
//     *
//     * @return
//     *     The place
//     */
//    public Object getPlace() {
//        return place;
//    }
//
//    /**
//     *
//     * @param place
//     *     The place
//     */
//    public void setPlace(Object place) {
//        this.place = place;
//    }
//
//    /**
//     *
//     * @return
//     *     The contributors
//     */
//    public Object getContributors() {
//        return contributors;
//    }
//
//    /**
//     *
//     * @param contributors
//     *     The contributors
//     */
//    public void setContributors(Object contributors) {
//        this.contributors = contributors;
//    }

    /**
     * 
     * @return
     *     The isQuoteStatus
     */
    public Boolean getIsQuoteStatus() {
        return isQuoteStatus;
    }

    /**
     * 
     * @param isQuoteStatus
     *     The is_quote_status
     */
    public void setIsQuoteStatus(Boolean isQuoteStatus) {
        this.isQuoteStatus = isQuoteStatus;
    }

    /**
     * 
     * @return
     *     The retweetCount
     */
    public Integer getRetweetCount() {
        return retweetCount;
    }

    /**
     * 
     * @param retweetCount
     *     The retweet_count
     */
    public void setRetweetCount(Integer retweetCount) {
        this.retweetCount = retweetCount;
    }

    /**
     * 
     * @return
     *     The favoriteCount
     */
    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    /**
     * 
     * @param favoriteCount
     *     The favorite_count
     */
    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    /**
     * 
     * @return
     *     The entities
     */
    public Entities_ getEntities() {
        return entities;
    }

    /**
     * 
     * @param entities
     *     The entities
     */
    public void setEntities(Entities_ entities) {
        this.entities = entities;
    }

    /**
     * 
     * @return
     *     The extendedEntities
     */
    public ExtendedEntities getExtendedEntities() {
        return extendedEntities;
    }

    /**
     * 
     * @param extendedEntities
     *     The extended_entities
     */
    public void setExtendedEntities(ExtendedEntities extendedEntities) {
        this.extendedEntities = extendedEntities;
    }

    /**
     * 
     * @return
     *     The favorited
     */
    public Boolean getFavorited() {
        return favorited;
    }

    /**
     * 
     * @param favorited
     *     The favorited
     */
    public void setFavorited(Boolean favorited) {
        this.favorited = favorited;
    }

    /**
     * 
     * @return
     *     The retweeted
     */
    public Boolean getRetweeted() {
        return retweeted;
    }

    /**
     * 
     * @param retweeted
     *     The retweeted
     */
    public void setRetweeted(Boolean retweeted) {
        this.retweeted = retweeted;
    }

    /**
     * 
     * @return
     *     The possiblySensitive
     */
    public Boolean getPossiblySensitive() {
        return possiblySensitive;
    }

    /**
     * 
     * @param possiblySensitive
     *     The possibly_sensitive
     */
    public void setPossiblySensitive(Boolean possiblySensitive) {
        this.possiblySensitive = possiblySensitive;
    }

    /**
     * 
     * @return
     *     The possiblySensitiveAppealable
     */
    public Boolean getPossiblySensitiveAppealable() {
        return possiblySensitiveAppealable;
    }

    /**
     * 
     * @param possiblySensitiveAppealable
     *     The possibly_sensitive_appealable
     */
    public void setPossiblySensitiveAppealable(Boolean possiblySensitiveAppealable) {
        this.possiblySensitiveAppealable = possiblySensitiveAppealable;
    }

    /**
     * 
     * @return
     *     The lang
     */
    public String getLang() {
        return lang;
    }

    /**
     * 
     * @param lang
     *     The lang
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.createdAt);
        dest.writeValue(this.id);
        dest.writeString(this.idStr);
        dest.writeString(this.text);
        dest.writeString(this.source);
        dest.writeValue(this.truncated);
        dest.writeValue(this.inReplyToStatusId);
        dest.writeString(this.inReplyToStatusIdStr);
        dest.writeValue(this.inReplyToUserId);
        dest.writeString(this.inReplyToUserIdStr);
        dest.writeString(this.inReplyToScreenName);
        dest.writeParcelable(this.user, flags);
//        dest.writeParcelable(this.geo, flags);
//        dest.writeParcelable(this.coordinates, flags);
//        dest.writeParcelable(this.place, flags);
//        dest.writeParcelable(this.contributors, flags);
        dest.writeValue(this.isQuoteStatus);
        dest.writeValue(this.retweetCount);
        dest.writeValue(this.favoriteCount);
        dest.writeParcelable(this.entities, flags);
        dest.writeParcelable(this.extendedEntities, flags);
        dest.writeValue(this.favorited);
        dest.writeValue(this.retweeted);
        dest.writeValue(this.possiblySensitive);
        dest.writeValue(this.possiblySensitiveAppealable);
        dest.writeString(this.lang);
    }

    public Tweet() {
    }

    protected Tweet(Parcel in) {
        this.createdAt = in.readString();
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.idStr = in.readString();
        this.text = in.readString();
        this.source = in.readString();
        this.truncated = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.inReplyToStatusId = (Long) in.readValue(Long.class.getClassLoader());
        this.inReplyToStatusIdStr = in.readString();
        this.inReplyToUserId = (Long) in.readValue(Long.class.getClassLoader());
        this.inReplyToUserIdStr = in.readString();
        this.inReplyToScreenName = in.readString();
        this.user = in.readParcelable(User.class.getClassLoader());
//        this.geo = in.readParcelable(Object.class.getClassLoader());
//        this.coordinates = in.readParcelable(Object.class.getClassLoader());
//        this.place = in.readParcelable(Object.class.getClassLoader());
//        this.contributors = in.readParcelable(Object.class.getClassLoader());
        this.isQuoteStatus = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.retweetCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.favoriteCount = (Integer) in.readValue(Integer.class.getClassLoader());
        this.entities = in.readParcelable(Entities_.class.getClassLoader());
        this.extendedEntities = in.readParcelable(ExtendedEntities.class.getClassLoader());
        this.favorited = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.retweeted = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.possiblySensitive = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.possiblySensitiveAppealable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.lang = in.readString();
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
