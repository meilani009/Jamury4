package com.a4bit.meilani.jamury4;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.a4bit.meilani.jamury4.utility.JamurModel;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.a4bit.meilani.jamury4.CardViewJamurAdapter.EXTRA_JAMUR;

/**
 * Created by root on 4/26/18.
 */

public class HasilActivity  extends AppCompatActivity {
    @BindView(R.id.gambar) ImageView gambar;
    @BindView(R.id.gambar_camera) ImageView gambar_camera;
    @BindView(R.id.namaLokal) TextView namaLokal;
    @BindView(R.id.namaKamera) TextView namaKamera;
    @BindView(R.id.statusRacun) TextView statusRacun;
    @BindView(R.id.statusMakan) TextView statusMakan;
    @BindView(R.id.kegunaan) TextView kegunaan;
    @BindView(R.id.habitat) TextView habitat;
    @BindView(R.id.warna) TextView warna;
    @BindView(R.id.bentukPayung) TextView bentukPayung;

    JamurModel jamur;
    Bitmap bmp_capture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hasil_layout);

        ButterKnife.bind(this); //buat binding view

        Log.d("pindah", "sudah sampai");
        jamur = getIntent().getParcelableExtra(EXTRA_JAMUR);
        bmp_capture = getIntent().getParcelableExtra("CaptureImg");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Hasil Identifikasi");

        gambar_camera.setImageBitmap(bmp_capture);

        int imgResourceId = this.getResources().getIdentifier(jamur.getImg_name(), "drawable", this.getPackageName());
        if(imgResourceId != 0)
            Picasso.with(this).load(imgResourceId).into(gambar);

        namaLokal.setText(jamur.getMushroom_name());
        namaKamera.setText("Gambar Hasil Kamera");
        statusRacun.setText(jamur.getStatus());
        statusMakan.setText(jamur.getEdibility());
        kegunaan.setText(jamur.getUsability());
        habitat.setText(jamur.getHabitat());
        warna.setText(jamur.getColor());
        bentukPayung.setText(jamur.getCap_shape());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
