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
import android.widget.TextView;
import android.widget.Toast;

import com.example.wisatareligi.R;
import com.example.wisatareligi.menu.add.AddAcara;
import com.example.wisatareligi.menu.detail.DetailAcara;
import com.example.wisatareligi.model.acara.AcaraItem;
import com.example.wisatareligi.model.insert.ResponseInsert;
import com.example.wisatareligi.network.retro.RetroClient;
import com.example.wisatareligi.network.service.ApiService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.wisatareligi.helper.contans.Constan.DATA;
import static com.example.wisatareligi.helper.date.CariBulan.cariNamaBulan;
import static com.example.wisatareligi.helper.date.ConvertDate.customTanggal;

public class AcaraAdapter extends RecyclerView.Adapter<AcaraAdapter.ViewHolder> {
    private Context context;
    private List<AcaraItem> list;
    private OnFunction listener;

    public AcaraAdapter(Context context, List<AcaraItem> list) {
        this.context = context;
        this.list = list;
    }

    public AcaraAdapter(Context context, List<AcaraItem> list, OnFunction listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public List<AcaraItem> getList() {
        return list;
    }

    public void setList(List<AcaraItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_acara, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        String month = cariNamaBulan(customTanggal(list.get(position).getTanggal(), "yyyy-MM-dd", "MM"));
        String date = customTanggal(list.get(position).getTanggal(), "yyyy-MM-dd", "dd");

        viewHolder.judul.setText(list.get(position).getNama());
        viewHolder.desc.setText(list.get(position).getDesk());
        viewHolder.date.setText(date + "\n" + month);
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
                                        AcaraItem data = list.get(position);
                                        Intent intent = new Intent(context, DetailAcara.class);
                                        intent.putExtra(DATA, data);
                                        context.startActivity(intent);
                                        break;
                                    case 1:
                                        AcaraItem data2 = list.get(position);
                                        Intent intent2 = new Intent(context, AddAcara.class);
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
        @BindView(R.id.date)
        TextView date;
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
        void onDelete(AcaraItem data);
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