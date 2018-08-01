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
 * Created by root on 8/1/18.
 */

public class Hasil0Activity extends AppCompatActivity {
    @BindView(R.id.gambar)
    ImageView gambar;
    @BindView(R.id.gambar_camera) ImageView gambar_camera;
    @BindView(R.id.namaLokal)
    TextView namaLokal;
    @BindView(R.id.namaKamera) TextView namaKamera;
    @BindView(R.id.statusRacun) TextView statusRacun;
    @BindView(R.id.statusMakan) TextView statusMakan;
    @BindView(R.id.kegunaan) TextView kegunaan;
    @BindView(R.id.habitat) TextView habitat;
    @BindView(R.id.warna) TextView warna;
    @BindView(R.id.bentukPayung) TextView bentukPayung;
    @BindView(R.id.tips) TextView tips;

    Bitmap bmp_capture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hasil_layout);

        ButterKnife.bind(this); //buat binding view

        Log.d("pindah", "sudah sampai");
//        jamur = getIntent().getParcelableExtra(EXTRA_JAMUR);
        bmp_capture = getIntent().getParcelableExtra("CaptureImg");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Hasil Identifikasi");

        gambar_camera.setImageBitmap(bmp_capture);
        gambar.setImageDrawable(getDrawable(R.drawable.empty));


        namaLokal.setText("Data Tidak Ditemukan");
        namaKamera.setText("Gambar Hasil Kamera");
        statusRacun.setText("");
        statusMakan.setText("");
        kegunaan.setText("-");
        habitat.setText("-");
        warna.setText("-");
        bentukPayung.setText("-");
        tips.setText("-");
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
