package com.example.wisatareligi.menu.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.example.wisatareligi.R;
import com.example.wisatareligi.menu.menu.Acara;
import com.example.wisatareligi.menu.menu.Petilasan;
import com.example.wisatareligi.menu.menu.Tentang;
import com.example.wisatareligi.model.wisata.ResponseWisata;
import com.example.wisatareligi.model.wisata.WisataItem;
import com.example.wisatareligi.network.ApiService;
import com.example.wisatareligi.network.RetroClient;
import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.BaseSliderView;
import com.glide.slider.library.SliderTypes.TextSliderView;
import com.glide.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.wisatareligi.helper.contans.Constan.BASE_URL_IMAGE;
import static com.example.wisatareligi.helper.contans.Koneksi.SUCCESS;

public class MainActivity extends AppCompatActivity implements ViewPagerEx.OnPageChangeListener {

    @BindView(R.id.slider)
    SliderLayout slider;
    @BindView(R.id.petilasan)
    CardView petilasan;
    @BindView(R.id.event)
    CardView event;
    @BindView(R.id.tentang)
    CardView tentang;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.galeri)
    CardView galeri;
    @BindView(R.id.account)
    CardView account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //slider
        slider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);

        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(4000);
        slider.addOnPageChangeListener(MainActivity.this);

        getWisata();
    }

    private void getWisata() {
        ApiService apiService = RetroClient.getApiService();
        Call<ResponseWisata> call = apiService.getWisata();
        call.enqueue(new Callback<ResponseWisata>() {
            @Override
            public void onResponse(Call<ResponseWisata> call, Response<ResponseWisata> response) {
                List<WisataItem> hasilPesan = response.body().getWisata();
                Log.e("Tag", "Hasil List :" + new Gson().toJson(hasilPesan));
                if (response.body().getResponse().equalsIgnoreCase(SUCCESS)) {
                    try {
                        for (int i = 0; i < hasilPesan.size(); i++) {
                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.centerCrop();

                            TextSliderView sliderView = new TextSliderView(MainActivity.this);
                            // if you want show image only / without description text use DefaultSliderView instead

                            // initialize SliderLayout
                            sliderView
                                    .image(BASE_URL_IMAGE + hasilPesan.get(i).getFoto())
                                    .description(hasilPesan.get(i).getNama())
                                    .setRequestOption(requestOptions)
                                    .setBackgroundColor(Color.WHITE)
                                    .setProgressBarVisible(true)
                                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                                        @Override
                                        public void onSliderClick(BaseSliderView slider) {
                                            Toast.makeText(MainActivity.this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                            //add your extra information
                            sliderView.bundle(new Bundle());
                            sliderView.getBundle().putString("extra", hasilPesan.get(i).getNama());
                            slider.addSlider(sliderView);
                        }
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Tag", "Gagal req data ");
                }
            }

            @Override
            public void onFailure(Call<ResponseWisata> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onStop() {
        slider.stopAutoCycle();
        super.onStop();
    }

    @OnClick({R.id.petilasan, R.id.event, R.id.galeri, R.id.account, R.id.tentang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.galeri:
                break;
            case R.id.account:
                break;
            case R.id.petilasan:
                startActivity(new Intent(MainActivity.this, Petilasan.class));
                break;
            case R.id.event:
                startActivity(new Intent(MainActivity.this, Acara.class));
                break;
            case R.id.tentang:
                startActivity(new Intent(MainActivity.this, Tentang.class));
                break;
        }
    }
}
