package com.example.wisatareligi.menu.add;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wisatareligi.R;
import com.example.wisatareligi.adapter.spiner.CustomAdapter;
import com.example.wisatareligi.menu.main.menu.Galeri;
import com.example.wisatareligi.menu.main.menu.Petilasan;
import com.example.wisatareligi.model.galeri.GaleriItem;
import com.example.wisatareligi.model.insert.ResponseInsert;
import com.example.wisatareligi.model.wisata.ResponseWisata;
import com.example.wisatareligi.model.wisata.WisataItem;
import com.example.wisatareligi.network.retro.RetroClient;
import com.example.wisatareligi.network.service.ApiService;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.wisatareligi.helper.contans.Constan.BASE_URL_IMAGE;
import static com.example.wisatareligi.helper.contans.Constan.DATA;
import static com.example.wisatareligi.helper.contans.Koneksi.SUCCESS;
import static com.example.wisatareligi.helper.function.CekEditText.editText;
import static com.example.wisatareligi.helper.function.FuncEditText.getEditText;
import static com.example.wisatareligi.helper.function.FuncEditText.setEditText;

public class AddGaleri extends AppCompatActivity {

    @BindView(R.id.edt_desk)
    EditText edtDesk;
    @BindView(R.id.txt_image)
    TextView txtImage;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.loader)
    AVLoadingIndicatorView loader;
    @BindView(R.id.simpan)
    Button simpan;
    @BindView(R.id.spiner)
    Spinner spiner;
    @BindView(R.id.txt_id)
    TextView txtId;
    private int GALLERY = 1, CAMERA = 2;
    String partImage = "";
    boolean update, flag = false;
    GaleriItem data;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    List<WisataItem> hasilPesanSpiner;
    CustomAdapter adapterSpiner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_galeri);
        ButterKnife.bind(this);

        requestMultiplePermissions();

        getSupportActionBar().setTitle("Tambah Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = getIntent().getParcelableExtra(DATA);
        if (data != null) {
            try {
                spiner.setVisibility(View.GONE);
                txtId.setText(data.getIdGaleri());
                setEditText(edtDesk, data.getDeskFoto());
                final String url = BASE_URL_IMAGE + data.getNamaFoto();

                Glide.with(this).load(url)
                        .thumbnail(0.5f)
                        //.crossFade()
                        //.diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(image);
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
                            Toast.makeText(AddGaleri.this, "Pilih Dulu", Toast.LENGTH_SHORT).show();
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

    @OnClick({R.id.txt_image, R.id.simpan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txt_image:
                showPictureDialog();
                break;
            case R.id.simpan:
                cekValue();
                break;
        }
    }

    //dialog
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    //open galleri
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    //open camera
    private void takePhotoFromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    //on result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toast.makeText(AddGaleri.this, "Image Saved! " + path, Toast.LENGTH_SHORT).show();
                    partImage = path;
                    image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AddGaleri.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(thumbnail);
            partImage = saveImage(thumbnail);
            Toast.makeText(AddGaleri.this, "Image Saved! " + thumbnail, Toast.LENGTH_SHORT).show();
        }
    }

    //save image
    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    //permission
    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    //
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
                        adapterSpiner = new CustomAdapter(AddGaleri.this, hasilPesanSpiner);
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

    //insert
    private void insert(String id, String desk, String part) {
        File imageFile = new File(part);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-file"), imageFile);
        //parm 1 samakan dengan parameter di backend
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);
        //eksekusi
        ApiService service = RetroClient.getApiService();
        Call<ResponseInsert> call = service.insertGaleri(id, desk, partImage);
        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                if (response.body().getCode() == 200) {
                    Toast.makeText(AddGaleri.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddGaleri.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {
                Toast.makeText(AddGaleri.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateWithImage(String id, String desk, String part, String hapus) {
        File imageFile = new File(part);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-file"), imageFile);
        //parm 1 samakan dengan parameter di backend
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);
        //eksekusi
        ApiService service = RetroClient.getApiService();
        Call<ResponseInsert> call = service.updateGaleri1(Integer.parseInt(id), partImage, hapus, desk);
        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                if (response.body().getCode() == 200) {
                    Toast.makeText(AddGaleri.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddGaleri.this, Galeri.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    Toast.makeText(AddGaleri.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {
                Toast.makeText(AddGaleri.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateWithField(String id, String desk) {
        ApiService service = RetroClient.getApiService();
        Call<ResponseInsert> call = service.updateGaleri2(id, desk);

        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                try {
                    if (response.body().getCode() == 200) {
                        Toast.makeText(AddGaleri.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddGaleri.this, Galeri.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    } else {
                        Toast.makeText(AddGaleri.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(AddGaleri.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {
                Toast.makeText(AddGaleri.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cekValue() {
        if ((editText(edtDesk))) {
            //is update
            if (update) {
                if (partImage.isEmpty()) {
                    updateWithField(data.getIdGaleri(), getEditText(edtDesk));
                } else {
                    updateWithImage(data.getIdGaleri(), getEditText(edtDesk), partImage, data.getNamaFoto());
                }
            } else {
                if (partImage.isEmpty()) {
                    Toast.makeText(this, "Masukan Gambar!", Toast.LENGTH_SHORT).show();
                } else {
                    insert(txtId.getText().toString(), getEditText(edtDesk),  partImage);
                }
            }
        }
    }
}
