package com.example.wisatareligi.menu.main.menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wisatareligi.R;
import com.example.wisatareligi.menu.login.LoginActivity;
import com.example.wisatareligi.model.insert.ResponseInsert;
import com.example.wisatareligi.network.retro.RetroClient;
import com.example.wisatareligi.network.service.ApiService;
import com.example.wisatareligi.session.Session;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.wisatareligi.helper.contans.Logins.ID;
import static com.example.wisatareligi.helper.contans.Logins.PASSWORD;
import static com.example.wisatareligi.helper.function.CekEditText.editText;
import static com.example.wisatareligi.session.Session.SP_SUDAH_LOGIN2;

public class UbahPass extends AppCompatActivity {

    @BindView(R.id.loader)
    AVLoadingIndicatorView loader;
    @BindView(R.id.simpan)
    Button simpan;
    @BindView(R.id.keluar)
    Button keluar;
    Session session;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.repassword)
    EditText repassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_pass);
        ButterKnife.bind(this);
        session = new Session(this);

        getSupportActionBar().setTitle("Ubah Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.simpan, R.id.keluar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.simpan:
                if (cekPass()) updatee(session.getSessionString(ID), password.getText().toString());
                break;
            case R.id.keluar:
                logout();
                break;
        }
    }

    private void logout() {
        AlertDialog.Builder aleBuilder = new AlertDialog.Builder(UbahPass.this);
        //settting judul dan pesan
        aleBuilder.setTitle("Keluar");
        aleBuilder
                .setMessage("Apakah anda yakin ingin keluar?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        session.saveSPBoolean(SP_SUDAH_LOGIN2, false);
                        startActivity(new Intent(UbahPass.this, LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        finish();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //cancel
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = aleBuilder.create();
        alertDialog.show();
    }

    private boolean cekPass() {
        if (password.getText().toString().equals(repassword.getText().toString())) {
            return true;
        } else {
            Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void updatee(String id, String pass) {
        loader.setVisibility(View.VISIBLE);
        ApiService apiService = RetroClient.getApiService();
        Call<ResponseInsert> call = apiService.updatePass(id, pass);
        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                Log.e("Tag", "Hasil List :" + new Gson().toJson(response.body()));
                if (response.body().getCode() == 200) {
                    try {
                        //pesan
                        Toast.makeText(UbahPass.this, "Data berhasil ubah!", Toast.LENGTH_SHORT).show();
                        loader.setVisibility(View.GONE);
                        session.saveSessionString(PASSWORD, password.getText().toString());
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(UbahPass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Tag", "Gagal req data ");
                    loader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(UbahPass.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
