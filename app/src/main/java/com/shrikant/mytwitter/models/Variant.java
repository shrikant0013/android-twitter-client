
package com.shrikant.mytwitter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class Variant implements Parcelable {

    @SerializedName("content_type")
    @Expose
    private String contentType;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("bitrate")
    @Expose
    private Integer bitrate;

    /**
     * 
     * @return
     *     The contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * 
     * @param contentType
     *     The content_type
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * 
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 
     * @return
     *     The bitrate
     */
    public Integer getBitrate() {
        return bitrate;
    }

    /**
     * 
     * @param bitrate
     *     The bitrate
     */
    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.contentType);
        dest.writeString(this.url);
        dest.writeValue(this.bitrate);
    }

    public Variant() {
    }

    protected Variant(Parcel in) {
        this.contentType = in.readString();
        this.url = in.readString();
        this.bitrate = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Variant> CREATOR = new Parcelable.Creator<Variant>() {
        public Variant createFromParcel(Parcel source) {
            return new Variant(source);
        }

        public Variant[] newArray(int size) {
            return new Variant[size];
        }
    };
}
