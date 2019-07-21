package com.example.wisatareligi.network.service;

import com.example.wisatareligi.model.acara.ResponseAcara;
import com.example.wisatareligi.model.galeri.ResponseGaleri;
import com.example.wisatareligi.model.insert.ResponseInsert;
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
    //TODO read
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

    //login
    @FormUrlEncoded
    @POST("read/admin.php")
    Call<ResponseLogin> login(@Field("email") String email, @Field("pass") String pass);

    //TODO delete
    @FormUrlEncoded
    @POST("delete/delete.php")
    Call<ResponseInsert> delete(@Field("tabel") String tabel, @Field("cari") String cari, @Field("id_data") String idData);

    //TODO create data
    @FormUrlEncoded
    @POST("create/acara.php")
    Call<ResponseInsert> insertAcara(@Field("id") String id, @Field("nama") String nama, @Field("desk") String desk, @Field("pembicara") String pembicara,
                                     @Field("alamat") String alamat, @Field("duror") String duror, @Field("tanggal") String tanggal, @Field("waktu") String waktu);

    @Multipart
    @POST("create/wisata.php")
    Call<ResponseInsert> insertWisata(@Part("nama") String nama,
                                      @Part("alamat") String alamat,
                                      @Part("desk") String desk,
                                      @Part("lat") String latitude,
                                      @Part("long") String longitude,
                                      @Part MultipartBody.Part image);

    @Multipart
    @POST("create/galeri.php")
    Call<ResponseInsert> insertGaleri(@Part("id") String nama,
                                      @Part("desk") String desk,
                                      @Part MultipartBody.Part image);

    //TODO update data
    //acara
    @FormUrlEncoded
    @POST("update/acara.php")
    Call<ResponseInsert> updateAcara(@Field("id") String id, @Field("nama") String nama, @Field("desk") String desk, @Field("pembicara") String pembicara,
                                     @Field("alamat") String alamat, @Field("duror") String duror, @Field("tanggal") String tanggal, @Field("waktu") String waktu);

    //wisata
    @Multipart
    @POST("update/wisata.php")
    Call<ResponseInsert> updateWisata1(@Part("id_wisata") int id,
                                       @Part MultipartBody.Part image,
                                       @Part("nama") String nama,
                                       @Part("alamat") String alamat,
                                       @Part("hapus") String hapus,
                                       @Part("desk") String desk,
                                       @Part("lat") String latitude,
                                       @Part("long") String longitude);

    @FormUrlEncoded
    @POST("update/wisata.php")
    Call<ResponseInsert> updateWisata2(@Field("nama") String nama,
                                       @Field("id_wisata") String id,
                                       @Field("alamat") String alamat,
                                       @Field("desk") String desk,
                                       @Field("lat") String latitude,
                                       @Field("long") String longitude);

    //galeri
    @Multipart
    @POST("update/galeri.php")
    Call<ResponseInsert> updateGaleri1(@Part("id") int id,
                                       @Part MultipartBody.Part image,
                                       @Part("hapus") String hapus,
                                       @Part("desk") String desk);

    @FormUrlEncoded
    @POST("update/galeri.php")
    Call<ResponseInsert> updateGaleri2(@Field("id") String id,
                                       @Field("desk") String desk);

    //pass
    @FormUrlEncoded
    @POST("update/admin.php")
    Call<ResponseInsert> updatePass(@Field("id") String id,
                                       @Field("pass") String pass);
}