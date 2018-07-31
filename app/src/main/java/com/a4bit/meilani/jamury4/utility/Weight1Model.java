package com.a4bit.meilani.jamury4.utility;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 7/31/18.
 */

public class Weight1Model implements Parcelable{
    private int id;
    private String weight_1;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeight_1() {
        return weight_1;
    }

    public void setWeight_1(String weight_1) {
        this.weight_1 = weight_1;
    }

    public Weight1Model() {
    }

    public Weight1Model(String weight_1) {
        this.weight_1 = weight_1;
    }

    public Weight1Model(int id, String weight_1) {
        this.id = id;
        this.weight_1 = weight_1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.weight_1);
    }

    protected Weight1Model(Parcel in) {
        this.id = in.readInt();
        this.weight_1 = in.readString();
    }

    public static final Creator<Weight1Model> CREATOR = new Creator<Weight1Model>() {
        @Override
        public Weight1Model createFromParcel(Parcel source) {
            return new Weight1Model(source);
        }

        @Override
        public Weight1Model[] newArray(int size) {
            return new Weight1Model[size];
        }
    };
}
