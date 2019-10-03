package com.imastudio.customerapp.network;


import com.imastudio.customerapp.model.modelmakanan.ResponseKategoriMakanan;
import com.imastudio.customerapp.model.modelmakanan.ResponseMakanan;
import com.imastudio.customerapp.model.modelmap.ResponseMap;
import com.imastudio.customerapp.model.modelojekonline.ResponseCheckBooking;
import com.imastudio.customerapp.model.modelojekonline.ResponseDriver;
import com.imastudio.customerapp.model.modelojekonline.ResponseInsertBooking;
import com.imastudio.customerapp.model.modelojekonline.ResponseLogin;
import com.imastudio.customerapp.model.modelojekonline.ResponseRegister;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RestApi {
    //    //todo 2 set endpoint di api.php
//
//    //    //endpoint untuk register
    @FormUrlEncoded
    @POST("daftar")
    Call<ResponseRegister> register(
            @Field("nama") String strnama,
            @Field("password") String strpass,
            @Field("email") String stremail,
            @Field("phone") String strphone
    );

    //    //endpoint untuk login
    @FormUrlEncoded
    @POST("login")
    Call<ResponseLogin> login(
            @Field("device") String strdevice,
            @Field("f_password") String strpass,
            @Field("f_email") String stremail

    );

    @GET("json")
    Call<ResponseMap> getdataMap(
         @Query("origin") String strorigin,
         @Query("destination") String strdestination,
         @Query("key") String strkey
    );

    //    //endpoint untuk insert booking
    @FormUrlEncoded
    @POST("insert_booking")
    Call<ResponseInsertBooking> insertBooking(
            @Field("f_idUser") String striduser,
            @Field("f_latAwal") String strlatawal,
            @Field("f_lngAwal") String strlngawal,
            @Field("f_awal") String strawal,
            @Field("f_latAkhir") String strlattujuan,
            @Field("f_lngAkhir") String strlontujuan,
            @Field("f_akhir") String strtujuan,
            @Field("f_catatan") String strcatatan,
            @Field("f_jarak") String strjarak,
            @Field("f_token") String strtoken,
            @Field("f_device") String strdevice

    );

    //    //endpoint untuk insert booking
    @FormUrlEncoded
    @POST("checkBooking")
    Call<ResponseCheckBooking> CheckBooking(
            @Field("idbooking") String stridBooking


    );
    //    //endpoint untuk cancel booking
    @FormUrlEncoded
    @POST("cancel_booking")
    Call<ResponseCheckBooking> CancelBooking(
            @Field("idbooking") String stridBooking,
            @Field("f_token") String strtoken,
            @Field("f_device") String strdevice


    );

    //    //endpoint untuk detail booking
    @FormUrlEncoded
    @POST("get_driver")
    Call<ResponseDriver> getDetailDriver(
            @Field("f_iddriver") String iddriver
                );

    //    //endpoint untuk kategori makanan
    @GET("kategorimakanan.php")
    Call<ResponseKategoriMakanan> getKategoriMakanan();


    //    //endpoint untuk get data makanan perkategori
    @FormUrlEncoded
    @POST("getdatamakanan.php")
    Call<ResponseMakanan> getMakanan(
            @Field("vsiduser") String iduser,
            @Field("vsidkastrkategorimakanan") String idkategori
    );

}