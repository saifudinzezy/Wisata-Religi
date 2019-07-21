package com.example.wisatareligi.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.wisatareligi.R;
import com.example.wisatareligi.model.galeri.GaleriItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wisatareligi.helper.alert.AlertCustom.dialogForm;
import static com.example.wisatareligi.helper.contans.Constan.BASE_URL_IMAGE;

public class GaleriAdapter extends RecyclerView.Adapter<GaleriAdapter.ViewHolder> {
    private Activity context;
    private List<GaleriItem> list;
    private LaporanAdapterListener listener;

    public GaleriAdapter(Activity context, List<GaleriItem> list) {
        this.context = context;
        this.list = list;
    }

    public GaleriAdapter(Activity context, List<GaleriItem> list, LaporanAdapterListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public List<GaleriItem> getList() {
        return list;
    }

    public void setList(List<GaleriItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_thumbnail, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final String url = BASE_URL_IMAGE + list.get(position).getNamaFoto();
        Glide.with(context).load(url)
                .thumbnail(0.5f)
                //.crossFade()
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(viewHolder.image);

        viewHolder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                View mView = context.getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                ImageView photoView = mView.findViewById(R.id.imageView);
//                photoView.setImageResource(R.drawable.nature);
                Glide.with(context).load(BASE_URL_IMAGE + list.get(position).getNamaFoto())
                        //.crossFade()
                        //.diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(photoView);

                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();*/
                dialogForm(url, context);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image)
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface LaporanAdapterListener {
        void onSelect(int index, GaleriItem data, View view);
    }
}