package com.mrc.reports.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;



public class Config {


    /**Network request links*/
    public static final String BASE_URL = "http://mrc.co.tz/auth/api/";
    public static final String IMAGE_URL = "http://";

    /**
     * Api to perform server query operations
     */
    public interface MrcApi{


        //login user details to server
        @FormUrlEncoded
        @POST("signin")
        Call<ResponseBody> singInUser(@Field("username") String phone, @Field("password") String password);

        //get Parameters
        @GET("get_sys_parameters")
        Call<ResponseBody> getParameters();

        //Update data details to server
        @FormUrlEncoded
        @POST("pos_register")
        Call<ResponseBody> posRegister(@Field("token") String token, @Field("details") String details, @Field("materials") String materials,@Field("pictures") String pictures);


        //Sync data details to server
        @FormUrlEncoded
        @POST("pos_sync_data")
        Call<ResponseBody> dataSync(@Field("token") String token, @Field("username") String username, @Field("data") String data);


    }
}
