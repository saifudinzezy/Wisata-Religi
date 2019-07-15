package com.example.wisatareligi.helper.alert;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.wisatareligi.R;

import static com.example.wisatareligi.helper.contans.Constan.BASE_URL_IMAGE;

public class AlertCustom {

    public static void dialogForm(String urlImage, Activity activity) {
        //buat dialog
        AlertDialog.Builder dialog;
        LayoutInflater inflater;
        View dialogView;
        dialog = new AlertDialog.Builder(activity);
        //buat layout
        inflater = activity.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialog_custom_layout, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);

        //inisialisasi
        ImageView photoView = dialogView.findViewById(R.id.img);

        Glide.with(activity).load(urlImage)
                //.crossFade()
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photoView);

        dialog.setPositiveButton("Tutup", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //keluar
                dialog.dismiss();
            }
        });
        //show dialog
        dialog.show();
    }

}
