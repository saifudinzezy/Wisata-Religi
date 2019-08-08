package com.example.wisatareligi.menu.loginregist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wisatareligi.R;
import com.example.wisatareligi.menu.main.MainActivity;
import com.example.wisatareligi.model.login.ResponseLogin;
import com.example.wisatareligi.model.login.UserItem;
import com.example.wisatareligi.network.ApiService;
import com.example.wisatareligi.network.RetroClient;
import com.example.wisatareligi.session.Session;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.wisatareligi.helper.contans.Logins.ALAMAT;
import static com.example.wisatareligi.helper.contans.Logins.EMAIL;
import static com.example.wisatareligi.helper.contans.Logins.FOTO;
import static com.example.wisatareligi.helper.contans.Logins.ID;
import static com.example.wisatareligi.helper.contans.Logins.NAME;
import static com.example.wisatareligi.helper.contans.Logins.PASSWORD;
import static com.example.wisatareligi.helper.contans.Logins.TGL_LAHIR;
import static com.example.wisatareligi.helper.contans.Logins.TMP_LAHIR;
import static com.example.wisatareligi.session.Session.SP_SUDAH_LOGIN;
import static com.example.wisatareligi.session.Session.SP_SUDAH_LOGIN2;

public class Login extends AppCompatActivity {

    @BindView(R.id.logo_container)
    LinearLayout logoContainer;
    @BindView(R.id.input_email)
    TextInputEditText inputEmail;
    @BindView(R.id.input_layout_email)
    TextInputLayout inputLayoutEmail;
    @BindView(R.id.input_password)
    TextInputEditText inputPassword;
    @BindView(R.id.input_layout_password)
    TextInputLayout inputLayoutPassword;
    @BindView(R.id.btn_signin)
    AppCompatButton btnSignin;
    @BindView(R.id.btn_fadtar)
    AppCompatButton btnDaftar;
    @BindView(R.id.loader)
    AVLoadingIndicatorView loader;
    Session sharedLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        sharedLogin = new Session(this);

        //cek apakah user sudah login
        if (sharedLogin.getSPSudahLogin()) {
            if (sharedLogin.getSPSudahLogin2()) {
                //cek login kedua
                startActivity(new Intent(Login.this, MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
            //cache
            inputEmail.setText(sharedLogin.getSessionString(NAME));
            inputPassword.setText(sharedLogin.getSessionString(PASSWORD));
        }
    }

    @OnClick({R.id.btn_signin, R.id.btn_fadtar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_signin:
                if (TextUtils.isEmpty(inputEmail.getText().toString())) {
                    inputEmail.setError("Nama Kosong");
                } else if (TextUtils.isEmpty(inputPassword.getText().toString())) {
                    inputPassword.setError("Password Kosong");
                } else if (!TextUtils.isEmpty(inputPassword.getText().toString()) && !TextUtils.isEmpty(inputEmail.getText().toString())) {
                    login(inputEmail.getText().toString(), inputPassword.getText().toString());
                }
                break;
            case R.id.btn_fadtar:
                startActivity(new Intent(Login.this, Register.class));
                break;
        }
    }

    private void login(String nama, String password) {
        loader.setVisibility(View.VISIBLE);
        //eksekusi
        ApiService service = RetroClient.getApiService();
        Call<ResponseLogin> login = service.login(nama, password);
        login.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                //cek code apakah sukses atau tidak
                try {
                    if (response.body().getCode() == 200) {
                        Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        List<UserItem> dataUser = response.body().getUser();
                        //simpan di sharef pref
                        //simpan id_user, nama_user, email dan password
                        sharedLogin.saveSessionString(ID, dataUser.get(0).getId());
                        sharedLogin.saveSessionString(NAME, inputEmail.getText().toString());
                        sharedLogin.saveSessionString(TMP_LAHIR, dataUser.get(0).getTmpLahir());
                        sharedLogin.saveSessionString(TGL_LAHIR, dataUser.get(0).getTglLahir());
                        sharedLogin.saveSessionString(PASSWORD, inputPassword.getText().toString());
                        sharedLogin.saveSessionString(FOTO, dataUser.get(0).getFoto());
                        sharedLogin.saveSessionString(ALAMAT, dataUser.get(0).getAlamat());
                        //buat cache
                        sharedLogin.saveSPBoolean(SP_SUDAH_LOGIN, true);
                        //cek login
                        sharedLogin.saveSPBoolean(SP_SUDAH_LOGIN2, true);
                        loader.setVisibility(View.INVISIBLE);
                        //buka home jika berhasil login
                        startActivity(new Intent(Login.this, MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                        //hancurkan activity
                        finish();
                    } else {
                        loader.setVisibility(View.INVISIBLE);
                        Toast.makeText(Login.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(Login.this, /*"Akun tidak ditemukan"*/ e.getMessage(), Toast.LENGTH_SHORT).show();
                    loader.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                //hilangkan loading
                loader.setVisibility(View.INVISIBLE);
                Toast.makeText(Login.this, "Koneksi Gagal Keserver", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
