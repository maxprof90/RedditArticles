package com.maxprof90.redditarticles.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Children implements Parcelable {
    @Expose
    @SerializedName("data")
    private Data data;
    @Expose
    @SerializedName("kind")
    private String kind;

    public Children(Data data, String kind) {
        this.data = data;
        this.kind = kind;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.data, flags);
        dest.writeString(this.kind);
    }


    protected Children(Parcel in) {
        this.data = in.readParcelable(Data.class.getClassLoader());
        this.kind = in.readString();
    }

    public static final Parcelable.Creator<Children> CREATOR = new Parcelable.Creator<Children>() {
        @Override
        public Children createFromParcel(Parcel source) {
            return new Children(source);
        }

        @Override
        public Children[] newArray(int size) {
            return new Children[size];
        }
    };

    @Override
    public String toString() {
        return "Children{" +
                "data=" + data +
                ", kind='" + kind + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Children)) return false;
        Children children = (Children) o;
        return data.equals(children.data) &&
                kind.equals(children.kind);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, kind);
    }
}
