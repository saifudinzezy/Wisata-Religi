package com.example.wisatareligi.network;

import android.support.annotation.Nullable;

import com.example.wisatareligi.model.acara.ResponseAcara;
import com.example.wisatareligi.model.galeri.ResponseGaleri;
import com.example.wisatareligi.model.insert.ResponseInsert;
import com.example.wisatareligi.model.komentar.ResponseKomentar;
import com.example.wisatareligi.model.login.ResponseLogin;
import com.example.wisatareligi.model.wisata.ResponseWisata;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    //login user
    @FormUrlEncoded
    @POST("read/user.php")
    Call<ResponseLogin> login(@Field("nama") String nama, @Field("pass") String pass);

    //update
    @Multipart
    @POST("update/user.php")
    Call<ResponseInsert> updateUser1(@Part("id") int id,
                                    @Part("nama") String nama,
                                    @Part MultipartBody.Part image,
                                    @Part("tmp") String tmp,
                                    @Part("hapus") String hapus,
                                    @Part("pass") String pass,
                                    @Part("tgl") String tgl,
                                    @Part("alamat") String alamat);

    @FormUrlEncoded
    @POST("update/user.php")
    Call<ResponseInsert> updateUser2(@Field("id") int id,
                                    @Field("nama") String nama,
                                    @Field("tmp") String tmp,
                                    @Field("pass") String pass,
                                    @Field("alamat") String alamat,
                                    @Field("tgl") String tgl);

    //register
    @Multipart
    @POST("create/user.php")
    Call<ResponseInsert> register(@Part("nama") String nama,
                                  @Part("pass") String pass,
                                  @Part("tmp") String tmp,
                                  @Part("tgl") String tgl,
                                  @Part("alamat") String alamat,
                                  @Part MultipartBody.Part image);

    //komentar
    @FormUrlEncoded
    @POST("read/komentar.php")
    Call<ResponseKomentar> getKomentar(@Field("id") String id);

    //cretae komentar
    @FormUrlEncoded
    @POST("create/komentar.php")
    Call<ResponseInsert> insertKomentar(@Field("id") String id, @Field("id_wisata") String id_wisata, @Field("pesan") String pesan, @Field("waktu") String waktu);

    //wisata
    @GET("read/wisata.php")
    Call<ResponseWisata> getWisata();

    @FormUrlEncoded
    @POST("read/wisata.php")
    Call<ResponseWisata> getWisataId(@Field("id") String id);

    //galeri
    @GET("read/galeri.php")
    Call<ResponseGaleri> getGaleri();

    @FormUrlEncoded
    @POST("read/galeri.php")
    Call<ResponseGaleri> getGaleriId(@Field("id") String id);

    //acara
    @GET("read/acara.php")
    Call<ResponseAcara> getAcara();

    @FormUrlEncoded
    @POST("read/acara.php")
    Call<ResponseAcara> getAcaraId(@Field("id") String id);
}