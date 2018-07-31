package com.a4bit.meilani.jamury4.utility;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 7/31/18.
 */

public class Weight2Model implements Parcelable{
    private int id;
    private String weight_2;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeight_2() {
        return weight_2;
    }

    public void setWeight_2(String weight_2) {
        this.weight_2 = weight_2;
    }

    public Weight2Model() {
    }

    public Weight2Model(String weight_2) {
        this.weight_2 = weight_2;
    }

    public Weight2Model(int id, String weight_2) {
        this.id = id;
        this.weight_2 = weight_2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.weight_2);
    }

    protected Weight2Model(Parcel in) {
        this.id = in.readInt();
        this.weight_2 = in.readString();
    }

    public static final Creator<Weight2Model> CREATOR = new Creator<Weight2Model>() {
        @Override
        public Weight2Model createFromParcel(Parcel source) {
            return new Weight2Model(source);
        }

        @Override
        public Weight2Model[] newArray(int size) {
            return new Weight2Model[size];
        }
    };
}
