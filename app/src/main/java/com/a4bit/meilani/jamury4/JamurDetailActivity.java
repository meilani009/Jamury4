package com.a4bit.meilani.jamury4;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 3/15/18.
 */

public class JamurDetailActivity extends AppCompatActivity {
    @BindView(R.id.gambar) ImageView gambar;
    @BindView(R.id.namaLokal) TextView namaLokal;
    @BindView(R.id.statusRacun) TextView statusRacun;
    @BindView(R.id.statusMakan) TextView statusMakan;
    @BindView(R.id.kegunaan) TextView kegunaan;
    @BindView(R.id.habitat) TextView habitat;
    @BindView(R.id.warna) TextView warna;
    @BindView(R.id.bentukPayung) TextView bentukPayung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jamur_detail);

        ButterKnife.bind(this);



    }
}
