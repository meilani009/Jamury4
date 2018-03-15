package com.a4bit.meilani.jamury4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.a4bit.meilani.jamury4.utility.JamurHelper;
import com.a4bit.meilani.jamury4.utility.JamurModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MushroomListActivity extends AppCompatActivity {
    @BindView(R.id.rv_jamur)RecyclerView rv_jamur;

    CardViewJamurAdapter adapter;
    private JamurHelper jamurHelper;

    ArrayList<JamurModel> jamurModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mushroom_list);
        ButterKnife.bind(this);

        adapter = new CardViewJamurAdapter(this);
        rv_jamur.setLayoutManager(new LinearLayoutManager(this));
        rv_jamur.setAdapter(adapter);

        jamurHelper = new JamurHelper(this);
        jamurHelper.open();
        jamurModels = jamurHelper.getAllData();
        adapter.setListJamur(jamurModels);
        jamurHelper.close();

    }
}
