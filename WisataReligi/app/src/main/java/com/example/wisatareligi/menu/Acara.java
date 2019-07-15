package com.example.wisatareligi.menu;

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
import com.example.wisatareligi.adapter.AcaraAdapter;
import com.example.wisatareligi.model.acara.AcaraItem;
import com.example.wisatareligi.model.acara.ResponseAcara;
import com.example.wisatareligi.network.ApiService;
import com.example.wisatareligi.network.RetroClient;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.wisatareligi.helper.contans.Koneksi.SUCCESS;

public class Acara extends AppCompatActivity implements SearchView.OnQueryTextListener{

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.loader)
    AVLoadingIndicatorView loader;
    List<AcaraItem> hasilPesan;
    AcaraAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acara);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Acara");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv.setLayoutManager(new LinearLayoutManager(this));
        getAcara();
    }

    private void getAcara() {
        loader.setVisibility(View.VISIBLE);
        ApiService apiService = RetroClient.getApiService();
        Call<ResponseAcara> call = apiService.getAcara();
        call.enqueue(new Callback<ResponseAcara>() {
            @Override
            public void onResponse(Call<ResponseAcara> call, Response<ResponseAcara> response) {
                hasilPesan = response.body().getAcara();
                Log.e("Tag", "Hasil List :" + new Gson().toJson(hasilPesan));
                if (response.body().getResponse().equalsIgnoreCase(SUCCESS)) {
                    try {
                        //
                        ArrayList<AcaraItem> list = new ArrayList<>();
                        list.addAll(hasilPesan);
                        adapter = new AcaraAdapter(Acara.this, list);
                        //  swipeMain.setRefreshing(false);
                        rv.setAdapter(adapter);
                        loader.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Toast.makeText(Acara.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Tag", "Gagal req data ");
                    loader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseAcara> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(Acara.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
            adapter = new AcaraAdapter(Acara.this, hasilPesan);
            rv.setAdapter(adapter);
            return false;
        }

        ArrayList<AcaraItem> filteredValues = new ArrayList<>(hasilPesan);
        for (AcaraItem value : hasilPesan) {
            if (!value.getNama().toLowerCase().contains(newText.toLowerCase())) {
                filteredValues.remove(value);
            }
        }
        adapter.setList(filteredValues);
        return false;
    }
}
