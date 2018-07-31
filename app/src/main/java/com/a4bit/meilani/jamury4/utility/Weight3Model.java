package com.a4bit.meilani.jamury4.utility;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 7/31/18.
 */

public class Weight3Model implements Parcelable{
    private int id;
    private String weight_3;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeight_3() {
        return weight_3;
    }

    public void setWeight_3(String weight_3) {
        this.weight_3 = weight_3;
    }

    public Weight3Model() {
    }

    public Weight3Model(String weight_3) {
        this.weight_3 = weight_3;
    }

    public Weight3Model(int id, String weight_3) {
        this.id = id;
        this.weight_3 = weight_3;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.weight_3);
    }

    protected Weight3Model(Parcel in) {
        this.id = in.readInt();
        this.weight_3 = in.readString();
    }

    public static final Creator<Weight3Model> CREATOR = new Creator<Weight3Model>() {
        @Override
        public Weight3Model createFromParcel(Parcel source) {
            return new Weight3Model(source);
        }

        @Override
        public Weight3Model[] newArray(int size) {
            return new Weight3Model[size];
        }
    };
}
