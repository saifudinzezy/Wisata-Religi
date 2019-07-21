package com.example.wisatareligi.menu.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wisatareligi.R;
import com.example.wisatareligi.model.acara.AcaraItem;
import com.example.wisatareligi.model.wisata.WisataItem;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.wisatareligi.helper.contans.Constan.BASE_URL_IMAGE;
import static com.example.wisatareligi.helper.contans.Constan.DATA;
import static com.example.wisatareligi.helper.date.CariBulan.cariNamaBulan;
import static com.example.wisatareligi.helper.date.ConvertDate.customTanggal;

public class DetailAcara extends AppCompatActivity {

    @BindView(R.id.tanggal)
    TextView tanggal;
    @BindView(R.id.judul)
    TextView judul;
    /*@BindView(R.id.toolbar)
    Toolbar toolbar;*/
    @BindView(R.id.ust)
    TextView ust;
    @BindView(R.id.duror)
    TextView duror;
    @BindView(R.id.jam)
    TextView jam;
    @BindView(R.id.alamat)
    TextView alamat;
    @BindView(R.id.desk)
    TextView desk;
    AcaraItem data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_acara);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = getIntent().getParcelableExtra(DATA);
        if (data != null) {
            try {
                String month = cariNamaBulan(customTanggal(data.getTanggal(), "yyyy-MM-dd", "MM"));
                String date = customTanggal(data.getTanggal(), "yyyy-MM-dd", "dd");

                desk.setText(data.getDesk());
                ust.setText(data.getPembicara());
                duror.setText(data.getDuror());
                jam.setText(data.getWaktu()+" WIB");
                alamat.setText(data.getAlamat());
                judul.setText(data.getNama());
                desk.setText(data.getDesk());
                tanggal.setText(date+"\n"+month);
                getSupportActionBar().setTitle(data.getNama());
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
}
