package com.a4bit.meilani.jamury4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by root on 7/22/18.
 */

public class AboutActivity extends AppCompatActivity {

//    @BindView(R.id.textDeteksi) TextView insDeteksi;
//    @BindView(R.id.textPeringatan) TextView warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        TextView tentang = (TextView) findViewById (R.id.tentang);
        TextView namaku = (TextView) findViewById (R.id.namaku);


        tentang.setText("Jamury adalah aplikasi untuk mengidentifikasi jenis jamur.Jamur yang dideteksi merupakan jamur Basidiomycota Makro yang sering dijumpai di Indonesia . Output dari aplikasi ini berupa jenis jamur  , status jamur apakah bisa dimakan atau tidak , serta kegunaan dan ciri-ciri dari jamur tersebut");

        namaku.setText("Aplikasi ini dibangun oleh Meilani Wulandari , dengan didampingi oleh dosen pembimbing Entin Martiana Kusumaningtyas,S.Kom , M.Kom dan Aliridho Barakhbah S.Kom , Ph.D.");

    }


}
