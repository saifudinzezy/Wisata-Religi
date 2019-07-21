package com.example.wisatareligi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wisatareligi.R;
import com.example.wisatareligi.menu.add.AddWisata;
import com.example.wisatareligi.menu.detail.DetailWisata;
import com.example.wisatareligi.model.insert.ResponseInsert;
import com.example.wisatareligi.model.wisata.WisataItem;
import com.example.wisatareligi.network.retro.RetroClient;
import com.example.wisatareligi.network.service.ApiService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.wisatareligi.helper.contans.Constan.BASE_URL_IMAGE;
import static com.example.wisatareligi.helper.contans.Constan.DATA;

public class WisataAdapter extends RecyclerView.Adapter<WisataAdapter.ViewHolder> {
    private Context context;
    private List<WisataItem> list;
    private OnFunction listener;

    public WisataAdapter(Context context, List<WisataItem> list) {
        this.context = context;
        this.list = list;
    }

    public WisataAdapter(Context context, List<WisataItem> list, OnFunction listener) {
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
                //
                final CharSequence[] items = {"Lihat", "Ubah", "Hapus"};
                new AlertDialog.Builder(context).setTitle("-Pilih Aksi-")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                switch (item) {
                                    case 0:
                                        WisataItem data = list.get(position);
                                        Intent intent = new Intent(context, DetailWisata.class);
                                        intent.putExtra(DATA, data);
                                        context.startActivity(intent);
                                        break;
                                    case 1:
                                        WisataItem data2 = list.get(position);
                                        Intent intent2 = new Intent(context, AddWisata.class);
                                        intent2.putExtra(DATA, data2);
                                        context.startActivity(intent2);
                                        break;
                                    case 2:
                                        listener.onDelete(list.get(position));
                                        break;
                                }

                                dialog.dismiss();
                            }
                        }).show();
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

    public interface OnFunction {
        void onDelete(WisataItem data);
    }

    public void hapusData(final String id, final String tabel, final String cari) {
        ApiService apiService = RetroClient.getApiService();
        Call<ResponseInsert> call = apiService.delete(tabel, cari, id);
        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                try {
                    if (response.body().getCode() == 200) {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {

            }
        });
    }
}