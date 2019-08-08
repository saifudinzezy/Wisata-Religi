package com.example.wisatareligi.menu.main.menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wisatareligi.R;
import com.example.wisatareligi.adapter.GaleriAdapter2;
import com.example.wisatareligi.adapter.spiner.CustomAdapter;
import com.example.wisatareligi.menu.add.AddGaleri;
import com.example.wisatareligi.model.galeri.GaleriItem;
import com.example.wisatareligi.model.galeri.ResponseGaleri;
import com.example.wisatareligi.model.wisata.ResponseWisata;
import com.example.wisatareligi.model.wisata.WisataItem;
import com.example.wisatareligi.network.retro.RetroClient;
import com.example.wisatareligi.network.service.ApiService;
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

import static com.example.wisatareligi.helper.contans.Koneksi.SUCCESS;

public class Galeri extends AppCompatActivity implements SearchView.OnQueryTextListener, GaleriAdapter2.OnFunction {
    @BindView(R.id.spiner)
    Spinner spiner;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.loader)
    AVLoadingIndicatorView loader;
    @BindView(R.id.add)
    FloatingActionButton add;
    List<WisataItem> hasilPesanSpiner;
    CustomAdapter adapterSpiner;
    List<GaleriItem> hasilPesan;
    GaleriAdapter2 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeri);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Galeri");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv.setLayoutManager(new GridLayoutManager(Galeri.this, 2));
        //spinner
        getWisata();
        spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (hasilPesanSpiner.get(position).getIdWisata().equalsIgnoreCase("0")) {
                        getAcara("0");
                        //Toast.makeText(Acara.this, "0", Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(Acara.this, hasilPesanSpiner.get(position).getIdWisata(), Toast.LENGTH_SHORT).show();
                        getAcara(hasilPesanSpiner.get(position).getIdWisata());
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // hide fab
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && add.getVisibility() == View.VISIBLE) {
                    add.hide();
                } else if (dy < 0 && add.getVisibility() != View.VISIBLE) {
                    add.show();
                }
            }
        });
    }

    @OnClick(R.id.add)
    public void onViewClicked() {
        startActivity(new Intent(Galeri.this, AddGaleri.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Cari..");
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (newText == null || newText.trim().isEmpty()) {
            adapter = new GaleriAdapter2(Galeri.this, hasilPesan, this);
            rv.setAdapter(adapter);
            return false;
        }

        ArrayList<GaleriItem> filteredValues = new ArrayList<>(hasilPesan);
        for (GaleriItem value : hasilPesan) {
            if (!value.getDeskFoto().toLowerCase().contains(newText.toLowerCase())) {
                filteredValues.remove(value);
            }
        }
        adapter.setList(filteredValues);
        return false;
    }

    private void getWisata() {
        loader.setVisibility(View.GONE);
        ApiService apiService = RetroClient.getApiService();
        Call<ResponseWisata> call = apiService.getWisata();
        call.enqueue(new Callback<ResponseWisata>() {
            @Override
            public void onResponse(Call<ResponseWisata> call, Response<ResponseWisata> response) {
                hasilPesanSpiner = new ArrayList<>();
                hasilPesanSpiner.add(new WisataItem("0", "-Semua Wisata-"));
                hasilPesanSpiner.addAll(response.body().getWisata());

                if (response.body().getResponse().equalsIgnoreCase(SUCCESS)) {
                    try {
                        //load jika ada data baru
                        adapterSpiner = new CustomAdapter(Galeri.this, hasilPesanSpiner);
                        spiner.setAdapter(adapterSpiner);
                        adapterSpiner.notifyDataSetChanged();
                    } catch (Exception e) {

                    }
                } else {
                    Log.e("Tag", "Gagal req data ");
                    loader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseWisata> call, Throwable t) {
                loader.setVisibility(View.GONE);
            }
        });
    }

    private void delete(final GaleriItem data) {
        //buat object alarm
        AlertDialog.Builder aleBuilder = new AlertDialog.Builder(Galeri.this);
        //settting judul dan pesan
        aleBuilder.setTitle("Hapus Data");
        aleBuilder
                .setMessage("Apakah anda yakin ingin menghapus data ini?")
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.hapusData(data.getIdGaleri(), "galeri", "id_galeri");
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

    @Override
    public void onDelete(GaleriItem data) {
        delete(data);
    }

    @Override
    public void onRefresh() {
        getWisata();
    }

    private void getAcara(String cek) {
        loader.setVisibility(View.VISIBLE);
        rv.setVisibility(View.VISIBLE);
        ApiService apiService = RetroClient.getApiService();
        Call<ResponseGaleri> call;

        if (cek.equalsIgnoreCase("0")) {
            call = apiService.getGaleri();
        } else {
            call = apiService.getGaleriId(cek);
        }

        call.enqueue(new Callback<ResponseGaleri>() {
            @Override
            public void onResponse(Call<ResponseGaleri> call, Response<ResponseGaleri> response) {
                hasilPesan = response.body().getGaleri();
                Log.e("Tag", "Hasil List :" + new Gson().toJson(hasilPesan));
                if (response.body().getResponse().equalsIgnoreCase(SUCCESS)) {
                    try {
                        //
                        ArrayList<GaleriItem> list = new ArrayList<>();
                        list.addAll(hasilPesan);
                        adapter = new GaleriAdapter2(Galeri.this, list, Galeri.this);
                        rv.setAdapter(adapter);
                        loader.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Toast.makeText(Galeri.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Tag", "Gagal req data ");
                    loader.setVisibility(View.GONE);
                    rv.setVisibility(View.GONE);
                    Toast.makeText(Galeri.this, "Data Kosong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseGaleri> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(Galeri.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
