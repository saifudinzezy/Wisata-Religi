package com.example.wisatareligi.menu.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.wisatareligi.R;
import com.example.wisatareligi.adapter.WisataAdapter;
import com.example.wisatareligi.menu.add.AddAcara;
import com.example.wisatareligi.menu.add.AddWisata;
import com.example.wisatareligi.model.wisata.ResponseWisata;
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

import static com.example.wisatareligi.helper.contans.Koneksi.SUCCESS;

public class Petilasan extends AppCompatActivity implements SearchView.OnQueryTextListener{

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.loader)
    AVLoadingIndicatorView loader;
    List<WisataItem> hasilPesan;
    WisataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petilasan);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("Wisata Religi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv.setLayoutManager(new LinearLayoutManager(this));
        getWisata();
    }

    private void getWisata() {
        loader.setVisibility(View.VISIBLE);
        ApiService apiService = RetroClient.getApiService();
        Call<ResponseWisata> call = apiService.getWisata();
        call.enqueue(new Callback<ResponseWisata>() {
            @Override
            public void onResponse(Call<ResponseWisata> call, Response<ResponseWisata> response) {
                hasilPesan = response.body().getWisata();
                Log.e("Tag", "Hasil List :" + new Gson().toJson(hasilPesan));
                if (response.body().getResponse().equalsIgnoreCase(SUCCESS)) {
                    try {
                        //
                        ArrayList<WisataItem> list = new ArrayList<>();
                        list.addAll(hasilPesan);
                        adapter = new WisataAdapter(Petilasan.this, list);
                        //  swipeMain.setRefreshing(false);
                        rv.setAdapter(adapter);
                        loader.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Toast.makeText(Petilasan.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Tag", "Gagal req data ");
                    loader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseWisata> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(Petilasan.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.  search, menu);
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
            adapter = new WisataAdapter(Petilasan.this, hasilPesan);
            rv.setAdapter(adapter);
            return false;
        }

        ArrayList<WisataItem> filteredValues = new ArrayList<>(hasilPesan);
        for (WisataItem value : hasilPesan) {
            if (!value.getNama().toLowerCase().contains(newText.toLowerCase())) {
                filteredValues.remove(value);
            }
        }
        adapter.setList(filteredValues);
        return false;
    }

    @OnClick(R.id.add)
    public void onViewClicked() {
        startActivity(new Intent(Petilasan.this, AddWisata.class));
    }
}
