package com.a4bit.meilani.jamury4;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.a4bit.meilani.jamury4.utility.JamurHelper;
import com.a4bit.meilani.jamury4.utility.JamurModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by root on 2/23/18.
 */

public class CardViewJamurAdapter extends RecyclerView.Adapter<CardViewJamurAdapter.CardViewViewHolder> {

    public static final String EXTRA_JAMUR ="extra_jamur";
    private ArrayList<JamurModel> listJamur;
    private Context context;

    public CardViewJamurAdapter(Context context){
        this.context = context;
    }

    public ArrayList<JamurModel> getListJamur(){
        return listJamur;
    }

    public void setListJamur(ArrayList<JamurModel> listJamur){
        this.listJamur = listJamur;
        notifyDataSetChanged();
    }

    @Override
    public CardViewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.jamur_item, parent, false);

        return new CardViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardViewViewHolder holder, final int position) {
        final JamurModel jamur = getListJamur().get(position);
        int imgResourceId = context.getResources().getIdentifier(jamur.getImg_name(), "drawable", context.getPackageName());
        if(imgResourceId != 0)
        Picasso.with(context).load(imgResourceId).into(holder.img_jamur);
        Log.d("loggy", jamur.getImg_name() + "--" + imgResourceId);
        holder.name_txt.setText(jamur.getMushroom_name());
        holder.status_txt.setText(jamur.getStatus());
    }


    @Override
    public int getItemCount() {
        if(getListJamur() == null)
            return 0;
        return getListJamur().size();
    }
    public JamurModel getItem(int position) {
        return getListJamur().get(position);
    }

    class CardViewViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.img_jamur) ImageView img_jamur;
        @BindView(R.id.name_txt) TextView name_txt;
        @BindView(R.id.status_txt) TextView status_txt;

        CardViewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, JamurDetailActivity.class);
                    int posisi = getAdapterPosition();
                    intent.putExtra(EXTRA_JAMUR, listJamur.get(posisi));
                    context.startActivity(intent);

                }
            });
        }
    }
}