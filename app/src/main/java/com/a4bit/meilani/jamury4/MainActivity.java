package com.a4bit.meilani.jamury4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.a4bit.meilani.jamury4.utility.JamurHelper;
import com.a4bit.meilani.jamury4.utility.JamurModel;
import com.a4bit.meilani.jamury4.utility.WarnaModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btn_start,btn_daftar,btn_search,btn_manual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start=(Button)findViewById(R.id.start_button);
        btn_daftar=(Button)findViewById(R.id.dft_button);
        btn_search=(Button)findViewById(R.id.search_button);
        btn_manual=(Button)findViewById(R.id.manual_button);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mulai_kamera = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(mulai_kamera);
            }
        });

        btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MushroomListActivity.class);
                startActivity(intent);
            }
        });


    }
}
