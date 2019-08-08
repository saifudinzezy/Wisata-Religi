package com.example.wisatareligi.menu.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wisatareligi.R;
import com.example.wisatareligi.adapter.GaleriAdapter;
import com.example.wisatareligi.menu.Komentar;
import com.example.wisatareligi.model.galeri.GaleriItem;
import com.example.wisatareligi.model.galeri.ResponseGaleri;
import com.example.wisatareligi.model.wisata.WisataItem;
import com.example.wisatareligi.network.ApiService;
import com.example.wisatareligi.network.RetroClient;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.wisatareligi.helper.contans.Constan.BASE_URL_IMAGE;
import static com.example.wisatareligi.helper.contans.Constan.DATA;
import static com.example.wisatareligi.helper.contans.Koneksi.SUCCESS;

public class DetailWisata extends AppCompatActivity {

    @BindView(R.id.img)
    ImageView img;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.galeri)
    RecyclerView galeri;
    @BindView(R.id.txt_desk)
    WebView desk;
    @BindView(R.id.lokasi)
    FloatingActionButton lokasi;
    WisataItem data;
    @BindView(R.id.loader)
    AVLoadingIndicatorView loader;
    @BindView(R.id.no_image)
    TextView noImage;
    @BindView(R.id.komentar)
    FloatingActionButton komentar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_wisata);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        galeri.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
                false));

        data = getIntent().getParcelableExtra(DATA);
        if (data != null) {
            try {
                desk.getSettings().setJavaScriptEnabled(true);
                desk.loadData(data.getDesk(), "text/html; charset=utf-8", "UTF-8");

                Glide.with(this)
                        .load(BASE_URL_IMAGE + data.getFoto())
                        .into(img);
                getSupportActionBar().setTitle(data.getNama());

                getGaleriId(data.getIdWisata());
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
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

    private void getGaleriId(String id) {
        loader.setVisibility(View.VISIBLE);
        ApiService apiService = RetroClient.getApiService();
        Call<ResponseGaleri> call = apiService.getGaleriId(id);
        call.enqueue(new Callback<ResponseGaleri>() {
            @Override
            public void onResponse(Call<ResponseGaleri> call, Response<ResponseGaleri> response) {
                List<GaleriItem> list = new ArrayList<>();
                list = response.body().getGaleri();
                Log.e("Tag", "Hasil List :" + new Gson().toJson(response.body().getGaleri()));

                if (response.body().getResponse().equalsIgnoreCase(SUCCESS)) {
                    try {
                        GaleriAdapter adapter = new GaleriAdapter(DetailWisata.this, list);
                        //  swipeMain.setRefreshing(false);
                        galeri.setAdapter(adapter);
                        loader.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Toast.makeText(DetailWisata.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        noImage.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e("Tag", "Gagal req data ");
                    loader.setVisibility(View.GONE);
                    noImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseGaleri> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(DetailWisata.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick({R.id.lokasi, R.id.komentar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.lokasi:
                try {
                    if (data != null) {
                        //maps
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + data.getLatitude() + "," + data.getLongitude() + "");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);
                    }
                } catch (Exception e) {
                    Snackbar.make(img, "Tidak ada aplikasi google maps, silahkan download dulu", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                break;
            case R.id.komentar:
                Intent intent = new Intent(DetailWisata.this, Komentar.class);
                intent.putExtra(DATA, data);
                startActivity(intent);
                break;
        }
    }
}
