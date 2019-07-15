package com.example.wisatareligi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wisatareligi.R;
import com.example.wisatareligi.menu.detail.DetailWisata;
import com.example.wisatareligi.model.wisata.WisataItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wisatareligi.helper.contans.Constan.BASE_URL_IMAGE;
import static com.example.wisatareligi.helper.contans.Constan.DATA;

public class WisataAdapter extends RecyclerView.Adapter<WisataAdapter.ViewHolder> {
    private Context context;
    private List<WisataItem> list;
    private LaporanAdapterListener listener;

    public WisataAdapter(Context context, List<WisataItem> list) {
        this.context = context;
        this.list = list;
    }

    public WisataAdapter(Context context, List<WisataItem> list, LaporanAdapterListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public List<WisataItem> getList() {
        return list;
    }

    public void setList(List<WisataItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_petilasan, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.judul.setText(list.get(position).getNama());
        viewHolder.desc.setText(list.get(position).getAlamat());
        Glide.with(context).load(BASE_URL_IMAGE + list.get(position).getFoto())
                .thumbnail(0.5f)
                //.crossFade()
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.img);
        viewHolder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WisataItem data = list.get(position);
                Intent intent = new Intent(context, DetailWisata.class);
                intent.putExtra(DATA, data);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.judul)
        TextView judul;
        @BindView(R.id.desc)
        TextView desc;
        @BindView(R.id.content)
        CardView content;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface LaporanAdapterListener {
        void onSelect(int index, WisataItem data, View view);
    }
}