package com.a4bit.meilani.jamury4.utility;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 2/23/18.
 */

public class JamurModel implements Parcelable {
    private int id;
    private String img_name;
    private String range;
    private String mushroom_name;
    private String status;
    private String edibility;
    private String usability;
    private String habitat;
    private String color;
    private String cap_shape;
    private String cook;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg_name() {
        return img_name;
    }

    public void setImg_name(String img_name) {
        this.img_name = img_name;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getMushroom_name() {
        return mushroom_name;
    }

    public void setMushroom_name(String mushroom_name) {
        this.mushroom_name = mushroom_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEdibility() {
        return edibility;
    }

    public void setEdibility(String edibility) {
        this.edibility = edibility;
    }

    public String getUsability() {
        return usability;
    }

    public void setUsability(String usability) {
        this.usability = usability;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCap_shape() {
        return cap_shape;
    }

    public void setCap_shape(String cap_shape) {
        this.cap_shape = cap_shape;
    }

    public String getCook() {
        return cook;
    }

    public void setCook(String cook) {
        this.cook = cook;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.img_name);
        dest.writeString(this.range);
        dest.writeString(this.mushroom_name);
        dest.writeString(this.status);
        dest.writeString(this.edibility);
        dest.writeString(this.usability);
        dest.writeString(this.habitat);
        dest.writeString(this.color);
        dest.writeString(this.cap_shape);
        dest.writeString(this.cook);
    }

    public JamurModel() {
    }

    protected JamurModel(Parcel in) {
        this.id = in.readInt();
        this.img_name = in.readString();
        this.range = in.readString();
        this.mushroom_name = in.readString();
        this.status = in.readString();
        this.edibility = in.readString();
        this.usability = in.readString();
        this.habitat = in.readString();
        this.color = in.readString();
        this.cap_shape = in.readString();
        this.cook = in.readString();
    }

    public static final Parcelable.Creator<JamurModel> CREATOR = new Parcelable.Creator<JamurModel>() {
        @Override
        public JamurModel createFromParcel(Parcel source) {
            return new JamurModel(source);
        }

        @Override
        public JamurModel[] newArray(int size) {
            return new JamurModel[size];
        }
    };

    public JamurModel(String img_name, String range, String mushroom_name, String status, String edibility, String usability, String habitat, String color, String cap_shape, String cook) {
        this.img_name = img_name;
        this.range = range;
        this.mushroom_name = mushroom_name;
        this.status = status;
        this.edibility = edibility;
        this.usability = usability;
        this.habitat = habitat;
        this.color = color;
        this.cap_shape = cap_shape;
        this.cook = cook;
    }
}
