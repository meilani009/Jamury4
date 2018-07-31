package com.a4bit.meilani.jamury4.utility;

import android.provider.BaseColumns;

import javax.print.DocFlavor;

/**
 * Created by root on 2/23/18.
 */

public class DatabaseContract {
    public static String TABLE_JAMURY = "table_jamury";
    public static String TABLE_WARNA = "table_warna";
    public static String TABLE_BENTUK = "table_bentuk";
    public static String TABLE_WEIGHT_1 = "table_weight_1";
    public static String TABLE_WEIGHT_2 = "table_weight_2";
    public static String TABLE_WEIGHT_3 = "table_weight_3";

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
        static String COOK = "cook";
    }

    static final class WarnaColumns implements BaseColumns{
        static String EKS_WARNA = "eksWarna";
    }

    static final class BentukColumns implements BaseColumns {
        static String EKS_BENTUK = "eksBentuk";
    }

    static final class Weight1Coloumns implements BaseColumns{
        static String Weight_1 = "weight1";
    }
    static final class Weight2Coloumns implements BaseColumns{
        static String Weight_2 = "weight2";

    }static final class Weight3Coloumns implements BaseColumns{
        static String Weight_3 = "weight3";
    }
}
