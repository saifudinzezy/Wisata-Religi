package com.example.wisatareligi.menu.menu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.wisatareligi.R;
import com.example.wisatareligi.menu.add.AddAcara;
import com.example.wisatareligi.menu.add.AddGaleri;

import butterknife.OnClick;

public class Galeri extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeri);
    }

    @OnClick(R.id.add)
    public void onViewClicked() {
        startActivity(new Intent(Galeri.this, AddGaleri.class));
    }
}
