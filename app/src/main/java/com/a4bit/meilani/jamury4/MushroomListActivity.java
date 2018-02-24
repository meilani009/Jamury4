package com.a4bit.meilani.jamury4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.a4bit.meilani.jamury4.utility.JamurHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MushroomListActivity extends AppCompatActivity {
    @BindView(R.id.rv_jamur)RecyclerView rv_jamur;

    CardViewJamurAdapter adapter;
    private JamurHelper jamurHelper;

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
        adapter.setListJamur(jamurHelper.getAllData());
        jamurHelper.close();
    }
}
