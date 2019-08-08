package com.example.wisatareligi.menu.main;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wisatareligi.R;
import com.example.wisatareligi.helper.cropimge.ImagePickerActivity;
import com.example.wisatareligi.menu.loginregist.Login;
import com.example.wisatareligi.model.insert.ResponseInsert;
import com.example.wisatareligi.network.ApiService;
import com.example.wisatareligi.network.RetroClient;
import com.example.wisatareligi.session.Session;
import com.glide.slider.library.svg.GlideApp;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
import static com.example.wisatareligi.helper.contans.Logins.ALAMAT;
import static com.example.wisatareligi.helper.contans.Logins.FOTO;
import static com.example.wisatareligi.helper.contans.Logins.ID;
import static com.example.wisatareligi.helper.contans.Logins.NAME;
import static com.example.wisatareligi.helper.contans.Logins.PASSWORD;
import static com.example.wisatareligi.helper.contans.Logins.TGL_LAHIR;
import static com.example.wisatareligi.helper.contans.Logins.TMP_LAHIR;
import static com.example.wisatareligi.helper.function.CekEditText.editText;
import static com.example.wisatareligi.helper.function.GetEditText.getEditText;
import static com.example.wisatareligi.session.Session.SP_SUDAH_LOGIN2;

public class Profil extends AppCompatActivity {

    @BindView(R.id.img_profile)
    CircularImageView imgProfile;
    @BindView(R.id.img_plus)
    CircularImageView imgPlus;
    @BindView(R.id.logo_container)
    LinearLayout logoContainer;
    @BindView(R.id.input_email)
    EditText inputEmail;
    @BindView(R.id.input_layout_email)
    TextInputLayout inputLayoutEmail;
    @BindView(R.id.input_tmp)
    EditText inputTmp;
    @BindView(R.id.input_tgl)
    EditText inputTgl;
    @BindView(R.id.ln_tgl)
    LinearLayout lnTgl;
    @BindView(R.id.input_alamat)
    EditText inputAlamat;
    @BindView(R.id.input_pass)
    EditText inputPass;
    @BindView(R.id.btn_signup)
    AppCompatButton btnSignup;
    @BindView(R.id.btn_keluar)
    AppCompatButton btnKeluar;
    @BindView(R.id.loader)
    AVLoadingIndicatorView loader;
    //
    private static final String TAG = Profil.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    //
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    String partImage = "";
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        ButterKnife.bind(Profil.this);
        getSupportActionBar().setTitle("Profil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadProfileDefault();
        session = new Session(Profil.this);
        setValue();
        // Clearing older images from cache directory
        // don't call Profil.this line if you want to choose multiple images in the same activity
        // call Profil.this once the bitmap(s) usage is over
        ImagePickerActivity.clearCache(Profil.this);
        // inisialisasi
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    }

    private void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);

        GlideApp.with(Profil.this).load(url)
                .into(imgProfile);
        imgProfile.setColorFilter(ContextCompat.getColor(Profil.this, android.R.color.transparent));
    }

    private void loadProfileDefault() {
        GlideApp.with(Profil.this).load(R.drawable.ic_people)
                .into(imgProfile);
        imgProfile.setColorFilter(ContextCompat.getColor(Profil.this, R.color.profile_default_tint));
    }

    @OnClick({R.id.img_profile, R.id.ln_tgl, R.id.btn_signup, R.id.btn_keluar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_profile:
                Dexter.withActivity(Profil.this)
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions();
                                }

                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
                break;
            case R.id.ln_tgl:
                showDateDialog();
                break;
            case R.id.btn_signup:
                cekValue();
                break;
            case R.id.btn_keluar:
                android.app.AlertDialog.Builder aleBuilder = new android.app.AlertDialog.Builder(Profil.this);
                //settting judul dan pesan
                aleBuilder.setTitle("Keluar");
                aleBuilder
                        .setMessage("Apakah anda yakin ingin keluar?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                session.saveSPBoolean(SP_SUDAH_LOGIN2, false);
                                startActivity(new Intent(Profil.this, Login.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                finish();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //cancel
                                dialog.cancel();
                            }
                        });
                android.app.AlertDialog alertDialog = aleBuilder.create();
                alertDialog.show();
                break;
        }
    }

    //dialog
    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(Profil.this, new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                launchCameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                launchGalleryIntent();
            }
        });
    }

    //launch camera
    private void launchCameraIntent() {
        Intent intent = new Intent(Profil.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    //launch gallery
    private void launchGalleryIntent() {
        Intent intent = new Intent(Profil.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    //onActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update Profil.this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(Profil.this.getContentResolver(), uri);
                    Log.e("path", "" + saveImage(bitmap));
                    partImage = saveImage(bitmap);
                    // loading profile image from local cache
                    loadProfile(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //setting permission

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Profil.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        }));
        builder.setNegativeButton(getString(android.R.string.cancel), (new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }));
        builder.show();
    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    //get real path
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
            MediaScannerConnection.scanFile(Profil.this,
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

    //Profil
    private void Profil(String id, String nama, String pass, String tmp, String tgl, String hapus, String part, String alamat) {
        File imageFile = new File(part);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-file"), imageFile);
        //parm 1 samakan dengan parameter di backend
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);
        //eksekusi
        ApiService service = RetroClient.getApiService();
        Call<ResponseInsert> call = service.updateUser1(Integer.parseInt(id), nama, partImage, tmp, hapus, pass, tgl, alamat);
        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                try {
                    if (response.body().getCode() == 200) {
                        Toast.makeText(Profil.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        setSession();
                    } else {
                        Toast.makeText(Profil.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(Profil.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {
                Toast.makeText(Profil.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateWithField(String id, String nama, String pass, String tmp, String tgl, String alamat) {
        ApiService service = RetroClient.getApiService();
        Call<ResponseInsert> call = service.updateUser2(Integer.parseInt(id), nama, tmp, pass, tgl, alamat);

        call.enqueue(new Callback<ResponseInsert>() {
            @Override
            public void onResponse(Call<ResponseInsert> call, Response<ResponseInsert> response) {
                if (response.body().getCode() == 200) {
                    Toast.makeText(Profil.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    setSession();
                } else {
                    Toast.makeText(Profil.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseInsert> call, Throwable t) {
                Toast.makeText(Profil.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //date
    private void showDateDialog() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(Profil.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                inputTgl.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void cekValue() {
        if ((editText(inputEmail) && editText(inputAlamat)) && (editText(inputPass) && editText(inputTmp)) &&
                !inputTgl.getText().toString().equalsIgnoreCase("08/08/2019")) {
            if (partImage.isEmpty()) {
                updateWithField(session.getSessionString(ID), getEditText(inputEmail), getEditText(inputPass), getEditText(inputTmp),
                        getEditText(inputTgl), getEditText(inputAlamat));
            } else {
                Profil(session.getSessionString(ID), getEditText(inputEmail), getEditText(inputPass), getEditText(inputTmp),
                        getEditText(inputTgl), session.getSessionString(FOTO), partImage, getEditText(inputAlamat));
            }
        }
    }

    private void setValue() {
        inputEmail.setText(session.getSessionString(NAME));
        inputAlamat.setText(session.getSessionString(ALAMAT));
        inputTmp.setText(session.getSessionString(TMP_LAHIR));
        inputTgl.setText(session.getSessionString(TGL_LAHIR));
        inputPass.setText(session.getSessionString(PASSWORD));
        final String url = BASE_URL_IMAGE + session.getSessionString(FOTO);
        Glide.with(Profil.this).load(url)
                .thumbnail(0.5f)
                //.crossFade()
                //.diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; Profil.this adds items to the action bar if it is present.
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

    private void setSession() {
        session.saveSessionString(NAME, getEditText(inputEmail));
        session.saveSessionString(TMP_LAHIR, getEditText(inputTmp));
        session.saveSessionString(TGL_LAHIR, getEditText(inputTgl));
        session.saveSessionString(PASSWORD, getEditText(inputPass));
//        session.saveSessionString(FOTO, getEditText(inputTmp));
        session.saveSessionString(ALAMAT, getEditText(inputAlamat));
    }
}
