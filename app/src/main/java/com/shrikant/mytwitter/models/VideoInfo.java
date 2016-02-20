
package com.shrikant.mytwitter.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class VideoInfo implements Parcelable {

    @SerializedName("aspect_ratio")
    @Expose
    private List<Integer> aspectRatio = new ArrayList<Integer>();
    @SerializedName("duration_millis")
    @Expose
    private Integer durationMillis;
    @SerializedName("variants")
    @Expose
    private List<Variant> variants = new ArrayList<Variant>();

    /**
     * 
     * @return
     *     The aspectRatio
     */
    public List<Integer> getAspectRatio() {
        return aspectRatio;
    }

    /**
     * 
     * @param aspectRatio
     *     The aspect_ratio
     */
    public void setAspectRatio(List<Integer> aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    /**
     * 
     * @return
     *     The durationMillis
     */
    public Integer getDurationMillis() {
        return durationMillis;
    }

    /**
     * 
     * @param durationMillis
     *     The duration_millis
     */
    public void setDurationMillis(Integer durationMillis) {
        this.durationMillis = durationMillis;
    }

    /**
     * 
     * @return
     *     The variants
     */
    public List<Variant> getVariants() {
        return variants;
    }

    /**
     * 
     * @param variants
     *     The variants
     */
    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.aspectRatio);
        dest.writeValue(this.durationMillis);
        dest.writeTypedList(variants);
    }

    public VideoInfo() {
    }

    protected VideoInfo(Parcel in) {
        this.aspectRatio = new ArrayList<Integer>();
        in.readList(this.aspectRatio, List.class.getClassLoader());
        this.durationMillis = (Integer) in.readValue(Integer.class.getClassLoader());
        this.variants = in.createTypedArrayList(Variant.CREATOR);
    }

    public static final Parcelable.Creator<VideoInfo> CREATOR = new Parcelable.Creator<VideoInfo>() {
        public VideoInfo createFromParcel(Parcel source) {
            return new VideoInfo(source);
        }

        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };
}
