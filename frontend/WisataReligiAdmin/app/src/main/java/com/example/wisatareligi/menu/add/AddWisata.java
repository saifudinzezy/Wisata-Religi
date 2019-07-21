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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wisatareligi.R;
import com.example.wisatareligi.menu.main.menu.Petilasan;
import com.example.wisatareligi.model.insert.ResponseInsert;
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
import static com.example.wisatareligi.helper.function.CekEditText.editText;
import static com.example.wisatareligi.helper.function.FuncEditText.getEditText;
import static com.example.wisatareligi.helper.function.FuncEditText.setEditText;

public class AddWisata extends AppCompatActivity {

    @BindView(R.id.edt_nama)
    EditText edtNama;
    @BindView(R.id.edt_desk)
    EditText edtDesk;
    @BindView(R.id.edt_alamat)
    EditText edtAlamat;
    @BindView(R.id.edt_lat)
    EditText edtLat;
    @BindView(R.id.edt_long)
    EditText edtLong;
    @BindView(R.id.txt_image)
    TextView txtImage;
    @BindView(R.id.loader)
    AVLoadingIndicatorView loader;
    @BindView(R.id.simpan)
    Button simpan;
    @BindView(R.id.image)
    ImageView image;
    private int GALLERY = 1, CAMERA = 2;
    String partImage = "";
    boolean update = false;
    WisataItem data;
    private static final String IMAGE_DIRECTORY = "/demonuts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wisata);
        ButterKnife.bind(this);

        requestMultiplePermissions();
        getSupportActionBar().setTitle("Tambah Data");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        data = getIntent().getParcelableExtra(DATA);
        if (data != null) {
            try {
                setEditText(edtNama, data.getNama());
                setEditText(edtAlamat, data.getAlamat());
                setEditText(edtDesk, data.getDesk());
                setEditText(edtAlamat, data.getAlamat());
                setEditText(edtLat, data.getLatitude());
                setEditText(edtLong, data.getLongitude());
                Glide.with(this).load(BASE_URL_IMAGE + data.getFoto())
                        .thumbnail(0.5f)
                        //.crossFade()
                        //.diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(image);
                update = true;
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
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    //open camera
    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
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
                    Toast.makeText(AddWisata.this, "Image Saved! " + path, Toast.LENGTH_SHORT).show();
                    partImage = path;
                    image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AddWisata.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(thumbnail);
            partImage = saveImage(thumbnail);
            Toast.makeText(AddWisata.this, "Image Saved! " + thumbnail, Toast.LENGTH_SHORT).show();
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

    //insert
    private void insert(String nama, String desk, String alamat, String latitude, String longitude, String part) {
        File imageFile = new File(part);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-file"), imageFile);
        //parm 1 samakan dengan parameter di backend
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);
        //eksekusi
        ApiService service = RetroClient.getApiService();
        Call<ResponseInsert> call = service.insertWisata(nama, alamat, desk, latitude, longitude, partImage);
        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                if (response.body().getCode() == 200) {
                    Toast.makeText(AddWisata.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddWisata.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {
                Toast.makeText(AddWisata.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cekValue() {
        if ((editText(edtNama) && editText(edtDesk)) && (editText(edtAlamat) && editText(edtLat)) && editText(edtLong)) {
            //is update
            if (update) {
                if (partImage.isEmpty()) {
                    updateWithField(data.getIdWisata(), getEditText(edtNama), getEditText(edtDesk), getEditText(edtAlamat), getEditText(edtLat), getEditText(edtLong));
                } else {
                    updateWithImage(data.getIdWisata(), getEditText(edtNama), getEditText(edtDesk), getEditText(edtAlamat), getEditText(edtLat), getEditText(edtLong),
                            partImage, data.getFoto());
                }
            } else {
                if (partImage.isEmpty()) {
                    Toast.makeText(this, "Masukan Gambar!", Toast.LENGTH_SHORT).show();
                } else {
                    insert(getEditText(edtNama), getEditText(edtDesk), getEditText(edtAlamat), getEditText(edtLat), getEditText(edtLong), partImage);
                }
            }
        }
    }

    public void updateWithImage(String id, String nama, String desk, String alamat, String latitude, String longitude, String part, String hapus) {
        File imageFile = new File(part);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-file"), imageFile);
        //parm 1 samakan dengan parameter di backend
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);
        //eksekusi
        ApiService service = RetroClient.getApiService();
        Call<ResponseInsert> call = service.updateWisata1(Integer.parseInt(id), partImage, nama, alamat, hapus, desk, latitude, longitude);
        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                if (response.body().getCode() == 200) {
                    Toast.makeText(AddWisata.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AddWisata.this, Petilasan.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    Toast.makeText(AddWisata.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {
                Toast.makeText(AddWisata.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateWithField(String id, String nama, String desk, String alamat, String latitude, String longitude) {
        ApiService service = RetroClient.getApiService();
        Call<ResponseInsert> call = service.updateWisata2(nama, id, alamat, desk, latitude, longitude);

        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                try {
                    if (response.body().getCode() == 200) {
                        Toast.makeText(AddWisata.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddWisata.this, Petilasan.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    } else {
                        Toast.makeText(AddWisata.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(AddWisata.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {
                Toast.makeText(AddWisata.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
