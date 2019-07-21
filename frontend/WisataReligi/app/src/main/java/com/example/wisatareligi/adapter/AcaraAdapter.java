package com.example.wisatareligi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wisatareligi.R;
import com.example.wisatareligi.menu.detail.DetailAcara;
import com.example.wisatareligi.menu.detail.DetailWisata;
import com.example.wisatareligi.model.acara.AcaraItem;
import com.example.wisatareligi.model.wisata.WisataItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wisatareligi.helper.contans.Constan.DATA;
import static com.example.wisatareligi.helper.date.CariBulan.cariNamaBulan;
import static com.example.wisatareligi.helper.date.ConvertDate.customTanggal;

public class AcaraAdapter extends RecyclerView.Adapter<AcaraAdapter.ViewHolder> {
    private Context context;
    private List<AcaraItem> list;
    private LaporanAdapterListener listener;

    public AcaraAdapter(Context context, List<AcaraItem> list) {
        this.context = context;
        this.list = list;
    }

    public AcaraAdapter(Context context, List<AcaraItem> list, LaporanAdapterListener listener) {
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
        viewHolder.date.setText(date+"\n"+month);
        viewHolder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcaraItem data = list.get(position);
                Intent intent = new Intent(context, DetailAcara.class);
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

    public interface LaporanAdapterListener {
        void onSelect(int index, AcaraItem data, View view);
    }
}