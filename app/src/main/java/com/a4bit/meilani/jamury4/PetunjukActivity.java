package com.a4bit.meilani.jamury4;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;

/**
 * Created by root on 7/22/18.
 */

public class PetunjukActivity extends AppCompatActivity {

//    @BindView(R.id.textDeteksi) TextView insDeteksi;
//    @BindView(R.id.textPeringatan) TextView warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petunjuk);
        TextView insDeteksi = (TextView) findViewById (R.id.textDeteksi);
        TextView warning = (TextView) findViewById (R.id.textPeringatan);


        warning.setText("APLIKASI INI MASIH DALAM TAHAP PENGEMBANGAN.\n\n"
                + "Dimohon untuk tetap berhati-hati saat berinteraksi dengan jamur yang Anda temukan.\n"
                + "Spesies yang ditampilkan belum tentu tepat dengan jenis yang sebenarnya.\n\n"
                + "Terima kasih");

        String deteksi = "1. Klik tombol Start\n"
                + "2. Klik Select Image\n"
                + "3. Ambil gambar dari kamera atau gallery\n"
                + "4. Potong gambar hingga presisi pada sisi terluar obyek\n"
                + "5. Setelah itu , Klik tombol View Result\n"
                + "6. Tungg hingga proses selesai\n"
                + "7. Lihat hasilnya";

        insDeteksi.setText(deteksi);

    }


}
