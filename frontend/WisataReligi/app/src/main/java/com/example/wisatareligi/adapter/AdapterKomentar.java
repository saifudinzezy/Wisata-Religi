package com.example.wisatareligi.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wisatareligi.R;
import com.example.wisatareligi.model.komentar.KomentarItem;
import com.example.wisatareligi.session.Session;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wisatareligi.helper.contans.Constan.BASE_URL_IMAGE;
import static com.example.wisatareligi.helper.contans.Logins.NAME;

/**
 * Created by USER on 10/08/2018.
 */

public class AdapterKomentar extends RecyclerView.Adapter<AdapterKomentar.MyViewHolder> {
    private List<KomentarItem> itemList;
    private Context context;
    private Session session;

    public AdapterKomentar(List<KomentarItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
        session = new Session(context);
    }

    public List<KomentarItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<KomentarItem> itemList) {
        this.itemList = itemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_komentar, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.setContent(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvSenderUsername)
        TextView tvSenderUsername;
        @BindView(R.id.tvSenderMessage)
        TextView tvSenderMessage;
        @BindView(R.id.itemSender)
        CardView itemSender;
        @BindView(R.id.img_profile)
        CircularImageView imgProfile;
        @BindView(R.id.ln_sender)
        LinearLayout lnSender;
        @BindView(R.id.img_profile2)
        CircularImageView imgProfile2;
        @BindView(R.id.tvReceiverUsername)
        TextView tvReceiverUsername;
        @BindView(R.id.tvReceiverMessage)
        TextView tvReceiverMessage;
        @BindView(R.id.itemReceiver)
        CardView itemReceiver;
        @BindView(R.id.ln_receiver)
        LinearLayout lnReceiver;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setContent(KomentarItem item) {
//
            //jika dia user
            if (!item.getNama().equalsIgnoreCase(session.getSessionString(NAME))) {
                lnReceiver.setVisibility(View.GONE);
                tvReceiverUsername.setVisibility(View.GONE);

                tvSenderMessage.setText(item.getPesan());
                tvSenderUsername.setText(item.getNama());
                final String url = BASE_URL_IMAGE + item.getFoto();
                Glide.with(context).load(url)
                        .thumbnail(0.5f)
                        //.crossFade()
                        //.diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgProfile);
            } else {
                //jika diadmin
                lnSender.setVisibility(View.GONE);
                tvSenderUsername.setVisibility(View.GONE);

                tvReceiverMessage.setText(item.getPesan());
                tvReceiverUsername.setText("Saya");
                final String url = BASE_URL_IMAGE + item.getFoto();
                Glide.with(context).load(url)
                        .thumbnail(0.5f)
                        //.crossFade()
                        //.diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(imgProfile2);
            }
        }
    }
}