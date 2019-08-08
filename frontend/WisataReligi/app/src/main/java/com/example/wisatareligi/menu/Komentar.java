package com.example.wisatareligi.menu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wisatareligi.R;
import com.example.wisatareligi.adapter.AdapterKomentar;
import com.example.wisatareligi.model.insert.ResponseInsert;
import com.example.wisatareligi.model.komentar.KomentarItem;
import com.example.wisatareligi.model.komentar.ResponseKomentar;
import com.example.wisatareligi.model.wisata.WisataItem;
import com.example.wisatareligi.network.ApiService;
import com.example.wisatareligi.network.RetroClient;
import com.example.wisatareligi.session.Session;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.wisatareligi.helper.contans.Constan.DATA;
import static com.example.wisatareligi.helper.contans.Logins.ID;

public class Komentar extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_pesan)
    RecyclerView rvPesan;
    @BindView(R.id.swipe_main)
    SmartRefreshLayout swipeMain;
    @BindView(R.id.edt_message)
    EditText edtMessage;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.itemSender)
    CardView itemSender;
    @BindView(R.id.form_message)
    LinearLayout formMessage;
    @BindView(R.id.constrain_main)
    ConstraintLayout constrainMain;
    SimpleDateFormat dateFormat;
    Date date;
    Session session;
    WisataItem data;
    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komentar);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Komentar");
        getSupportActionBar().setSubtitle("dari Pengunjung");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        session = new Session(this);
        dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        date = new Date();

        //
        data = getIntent().getParcelableExtra(DATA);
        if (data != null) {
            try {
                id = data.getIdWisata();
                getPesan(data.getIdWisata());
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        rvPesan.setLayoutManager(layoutManager);

        swipeMain.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getPesan(id);
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        swipeMain.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                getPesan(id);
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });

        // hide fab
        rvPesan.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && formMessage.getVisibility() == View.VISIBLE) {
                    formMessage.setVisibility(View.GONE);
                } else if (dy < 0 && formMessage.getVisibility() != View.VISIBLE) {
                    formMessage.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getPesan(String id) {
        ApiService apiService = RetroClient.getApiService();
        Call<ResponseKomentar> call = apiService.getKomentar(id);
        call.enqueue(new Callback<ResponseKomentar>() {
            @Override
            public void onResponse(Call<ResponseKomentar> call, Response<ResponseKomentar> response) {
                List<KomentarItem> hasilPesan = response.body().getKomentar();
                Log.e("Tag", "Hasil List :" + new Gson().toJson(hasilPesan));
                if (response.body().isStatus() == true) {
                    AdapterKomentar adapterPesan = new AdapterKomentar(hasilPesan, Komentar.this);
                    //  swipeMain.setRefreshing(false);
                    rvPesan.setAdapter(adapterPesan);
                } else {
                    Log.e("Tag", "Gagal req data ");
                    Toast.makeText(Komentar.this, "Tidak Ada Komentar!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseKomentar> call, Throwable t) {
                Log.e("Tag", "Gagal jaringan " + t.getMessage());

            }
        });

        // hide fab
        rvPesan.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && formMessage.getVisibility() == View.VISIBLE) {
                    formMessage.setVisibility(View.GONE);
                } else if (dy < 0 && formMessage.getVisibility() != View.VISIBLE) {
                    formMessage.setVisibility(View.VISIBLE);
                }
            }
        });
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

    @OnClick(R.id.btn_send)
    public void onViewClicked() {
        String pesan = edtMessage.getText().toString();

        if (pesan.isEmpty()) {
            edtMessage.setError("Field TIdak Bisa Kosong");
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(Komentar.this);
            progressDialog.setMessage("Send Pesan");
            progressDialog.show();

            ApiService apiService = RetroClient.getApiService();
            Call<ResponseInsert> call = apiService.insertKomentar(session.getSessionString(ID), id, edtMessage.getText().toString(), dateFormat.format(date));
            call.enqueue(new Callback<ResponseInsert>() {
                @Override
                public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                    if (response.body().getCode() == 200) {
                        progressDialog.dismiss();
                        edtMessage.setText("");
                        getPesan(id);
                    } else {
                        Toast.makeText(Komentar.this, "Gagal Kirim Data", Toast.LENGTH_SHORT).show();
                    }
                }

                //
                @Override
                public void onFailure(Call<ResponseInsert> call, Throwable t) {
                    Log.e("Tag", "Error jaringan saat insert :" + t.getMessage());
                    Toast.makeText(Komentar.this, "Error jaringan saat insert", Toast.LENGTH_SHORT).show();


                }
            });

        }
    }
}