
package com.shrikant.mytwitter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class ExtendedEntities implements Parcelable {

    @SerializedName("media")
    @Expose
    private List<Medium__> media = new ArrayList<Medium__>();

    /**
     * 
     * @return
     *     The media
     */
    public List<Medium__> getMedia() {
        return media;
    }

    /**
     * 
     * @param media
     *     The media
     */
    public void setMedia(List<Medium__> media) {
        this.media = media;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.media);
    }

    public ExtendedEntities() {
    }

    protected ExtendedEntities(Parcel in) {
        this.media = new ArrayList<Medium__>();
        in.readList(this.media, List.class.getClassLoader());
    }

    public static final Parcelable.Creator<ExtendedEntities> CREATOR = new Parcelable.Creator<ExtendedEntities>() {
        public ExtendedEntities createFromParcel(Parcel source) {
            return new ExtendedEntities(source);
        }

        public ExtendedEntities[] newArray(int size) {
            return new ExtendedEntities[size];
        }
    };
}
