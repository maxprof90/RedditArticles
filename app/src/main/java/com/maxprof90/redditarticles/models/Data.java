package com.maxprof90.redditarticles.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class Data implements Parcelable{

    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("is_video")
    @Expose
    private boolean isVideo;

    @SerializedName("children")
    @Expose
    List<Children> children;

    @SerializedName("icon_url")
    @Expose
    private String icon_url;

    @SerializedName("created_utc")
    @Expose
    private double createdUtc;

    @SerializedName("num_comments")
    @Expose
    private int numComments;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("after")
    @Expose
    private String after;

    @SerializedName("url_overridden_by_dest")
    @Expose
    private String imgURl;
    private Bitmap thumbBitmap;

    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    public String getAuthor() {
        return author;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public void setVideo(boolean video) {
        isVideo = video;
    }

    public List<Children> getChildren() {
        return children;
    }

    public void setChildren(List<Children> children) {
        this.children = children;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public double getCreatedUtc() {
        return createdUtc;
    }

    public void setCreatedUtc(double createdUtc) {
        this.createdUtc = createdUtc;
    }

    public int getNumComments() {
        return numComments;
    }

    public void setNumComments(int numComments) {
        this.numComments = numComments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getImgURl() {
        return imgURl;
    }

    public void setImgURl(String imgURl) {
        this.imgURl = imgURl;
    }

    public Bitmap getThumbBitmap() {
        return thumbBitmap;
    }

    public void setThumbBitmap(Bitmap thumbBitmap) {
        this.thumbBitmap = thumbBitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.isVideo ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.children);
        dest.writeString(this.thumbnail);
        dest.writeString(this.author);
        dest.writeInt(this.numComments);
        dest.writeDouble(this.createdUtc);
        dest.writeString(this.title);
        dest.writeString(this.name);
        dest.writeString(this.after);
        dest.writeString(this.imgURl);
    }

    protected Data(Parcel in) {
        this.isVideo = in.readByte() != 0;
        this.children = in.createTypedArrayList(Children.CREATOR);
        this.thumbnail = in.readString();
        this.author = in.readString();
        this.numComments = in.readInt();
        this.createdUtc = in.readDouble();
        this.title = in.readString();
        this.name = in.readString();
        this.after = in.readString();
        this.imgURl = in.readString();
    }

    public static final Parcelable.Creator<Data> CREATOR = new Parcelable.Creator<Data>() {
        @Override
        public Data createFromParcel(Parcel source) {
            return new Data(source);
        }

        @Override
        public Data[] newArray(int size) {
            return new Data[size];
        }
    };

    public Data(boolean isVideo, String thumbnail, String author, int numComments, double createdUtc, String title,  String name,  String after, String imgURl) {
        this.isVideo = isVideo;
        this.thumbnail = thumbnail;
        this.author = author;
        this.numComments = numComments;
        this.createdUtc = createdUtc;
        this.title = title;
        this.name = name;
        this.after = after;
        this.imgURl = imgURl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Data)) return false;
        Data data = (Data) o;
        return author.equals(data.author)  &&
                title.equals(data.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, title);
    }

}