package com.example.wisatareligi.menu.add;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.wisatareligi.R;
import com.example.wisatareligi.adapter.spiner.CustomAdapter;
import com.example.wisatareligi.model.acara.AcaraItem;
import com.example.wisatareligi.model.insert.ResponseInsert;
import com.example.wisatareligi.model.wisata.ResponseWisata;
import com.example.wisatareligi.model.wisata.WisataItem;
import com.example.wisatareligi.network.retro.RetroClient;
import com.example.wisatareligi.network.service.ApiService;
import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.wisatareligi.helper.contans.Constan.DATA;
import static com.example.wisatareligi.helper.contans.Koneksi.SUCCESS;
import static com.example.wisatareligi.helper.function.CekEditText.editText;
import static com.example.wisatareligi.helper.function.Clear.clearEdt;
import static com.example.wisatareligi.helper.function.FuncEditText.getEditText;
import static com.example.wisatareligi.helper.function.FuncEditText.setEditText;

public class AddAcara extends AppCompatActivity {

    @BindView(R.id.edt_nama_acara)
    EditText edtNamaAcara;
    @BindView(R.id.edt_deskripsi)
    EditText edtDeskripsi;
    @BindView(R.id.edt_pembicara)
    EditText edtPembicara;
    @BindView(R.id.edt_alamat)
    EditText edtAlamat;
    @BindView(R.id.edt_duror)
    EditText edtDuror;
    @BindView(R.id.tanggal)
    TextView tanggal;
    @BindView(R.id.jam)
    TextView jam;
    @BindView(R.id.loader)
    AVLoadingIndicatorView loader;
    @BindView(R.id.simpan)
    Button simpan;
    @BindView(R.id.txt_id)
    TextView txtId;
    @BindView(R.id.spiner)
    Spinner spiner;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    List<WisataItem> hasilPesanSpiner;
    CustomAdapter adapterSpiner;
    boolean flag, update = false;
    AcaraItem data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_acara);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Tambah Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // inisialisasi
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        data = getIntent().getParcelableExtra(DATA);
        if (data != null) {
            try {
                spiner.setVisibility(View.GONE);
                txtId.setText(data.getIdAcara());
                setEditText(edtNamaAcara, data.getNama());
                setEditText(edtDeskripsi, data.getDesk());
                setEditText(edtPembicara, data.getPembicara());
                setEditText(edtAlamat, data.getAlamat());
                setEditText(edtDuror, data.getDuror());
                tanggal.setText(data.getTanggal());
                jam.setText(data.getWaktu());
                flag = true;
                update = true;
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {
            //spinner
            getWisata();
            spiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        if (hasilPesanSpiner.get(position).getIdWisata().equalsIgnoreCase("0")) {
                            Toast.makeText(AddAcara.this, "Pilih Dulu", Toast.LENGTH_SHORT).show();
                            flag = false;
                        } else {
                            txtId.setText(hasilPesanSpiner.get(position).getIdWisata());
                            flag = true;
                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
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
                finish();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.tanggal, R.id.jam, R.id.simpan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tanggal:
                showDateDialog();
                break;
            case R.id.jam:
                showTimeDialog();
                break;
            case R.id.simpan:
                cekValue();
                break;
        }
    }

    private void showTimeDialog() {
        //Calendar untuk mendapatkan waktu saat ini
        Calendar calendar = Calendar.getInstance();

        //Initialize TimePicker Dialog
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                jam.setText(hourOfDay + ":" + minute);
            }
        },
                /**
                 * Tampilkan jam saat ini ketika TimePicker pertama kali dibuka
                 */
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE),

                /**
                 * Cek apakah format waktu menggunakan 24-hour format
                 */
                DateFormat.is24HourFormat(this));

        timePickerDialog.show();
    }

    private void showDateDialog() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                tanggal.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void insert(String id, String nama, String desk, String pembicara, String alamat, String duror, String tanggal, String waktu) {
        loader.setVisibility(View.VISIBLE);
        ApiService apiService = RetroClient.getApiService();
        Call<ResponseInsert> call = apiService.insertAcara(id, nama, desk, pembicara, alamat, duror, tanggal, waktu);
        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                Log.e("Tag", "Hasil List :" + new Gson().toJson(response.body()));
                if (response.body().getCode() == 200) {
                    try {
                        //pesan
                        Toast.makeText(AddAcara.this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show();
                        kosongkan();
                        loader.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Toast.makeText(AddAcara.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Tag", "Gagal req data ");
                    loader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(AddAcara.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatee(String id, String nama, String desk, String pembicara, String alamat, String duror, String tanggal, String waktu) {
        loader.setVisibility(View.VISIBLE);
        ApiService apiService = RetroClient.getApiService();
        Call<ResponseInsert> call = apiService.updateAcara(id, nama, desk, pembicara, alamat, duror, tanggal, waktu);
        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                Log.e("Tag", "Hasil List :" + new Gson().toJson(response.body()));
                if (response.body().getCode() == 200) {
                    try {
                        //pesan
                        Toast.makeText(AddAcara.this, "Data berhasil ubah!", Toast.LENGTH_SHORT).show();
                        loader.setVisibility(View.GONE);
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(AddAcara.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Tag", "Gagal req data ");
                    loader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(AddAcara.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void notif(String notif) {
        Toast.makeText(this, notif, Toast.LENGTH_SHORT).show();
    }

    private boolean text(TextView textView, String value) {
        if (textView.getText().toString().equalsIgnoreCase(value)) {
            notif(value);
            return false;
        } else {
            return true;
        }
    }

    private void kosongkan() {
        clearEdt(edtNamaAcara);
        clearEdt(edtDeskripsi);
        clearEdt(edtPembicara);
        clearEdt(edtAlamat);
        clearEdt(edtDuror);
    }

    private void getWisata() {
        loader.setVisibility(View.GONE);
        ApiService apiService = RetroClient.getApiService();
        Call<ResponseWisata> call = apiService.getWisata();
        call.enqueue(new Callback<ResponseWisata>() {
            @Override
            public void onResponse(Call<ResponseWisata> call, Response<ResponseWisata> response) {
                hasilPesanSpiner = new ArrayList<>();
                hasilPesanSpiner.add(new WisataItem("0", "-Pilih Wisata Religi-"));
                hasilPesanSpiner.addAll(response.body().getWisata());

                if (response.body().getResponse().equalsIgnoreCase(SUCCESS)) {
                    try {
                        //load jika ada data baru
                        adapterSpiner = new CustomAdapter(AddAcara.this, hasilPesanSpiner);
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

    private void cekValue() {
        if ((editText(edtNamaAcara) && editText(edtDeskripsi)) && (editText(edtPembicara) && editText(edtAlamat)) &&
                (editText(edtDuror) && text(tanggal, getString(R.string.tanggal))) && (text(jam, getString(R.string.waktu)) && flag)) {
            //is update
            if (update) {
                updatee(txtId.getText().toString(), getEditText(edtNamaAcara), getEditText(edtDeskripsi), getEditText(edtPembicara), getEditText(edtAlamat), getEditText(edtDuror),
                        tanggal.getText().toString(), jam.getText().toString());
            } else {
                insert(txtId.getText().toString(), getEditText(edtNamaAcara), getEditText(edtDeskripsi), getEditText(edtPembicara), getEditText(edtAlamat), getEditText(edtDuror),
                        tanggal.getText().toString(), jam.getText().toString());
            }
        }
    }
}