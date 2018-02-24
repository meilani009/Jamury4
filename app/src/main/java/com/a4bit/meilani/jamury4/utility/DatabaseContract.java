package com.a4bit.meilani.jamury4.utility;

import android.provider.BaseColumns;

/**
 * Created by root on 2/23/18.
 */

public class DatabaseContract {
    public static String TABLE_JAMURY = "table_jamury";

    static final class DictionaryColumns implements BaseColumns {
        static String IMG_NAME = "imgName";
        static String RANGE = "range";
        static String MUSHROOM_NAME = "mushroomName";
        static String STATUS = "status";
        static String EDIBILITY = "edibility";
        static String USABILITY = "usability";
        static String HABITAT = "habitat";
        static String COLOR = "color";
        static String CAP_SHAPE = "capShape";
    }
}
