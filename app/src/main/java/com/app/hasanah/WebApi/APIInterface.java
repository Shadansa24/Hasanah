package com.app.hasanah.WebApi;


import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {
    //https://9804-156-215-41-105.ngrok-free.app/Takbeer
    @Multipart
    @POST("/Ruku")
    Call<RespnseName> UploadFileRuku(@Part MultipartBody.Part imagefile);

    @Multipart
    @POST("/Sujud")
    Call<RespnseName> UploadFileASujud(@Part MultipartBody.Part imagefile);

    @Multipart
    @POST("/Takbeer")
    Call<RespnseName> UploadFileTakbeer(@Part MultipartBody.Part imagefile);
    @Multipart
    @POST("/Raising")
    Call<RespnseName> UploadFileRaising(@Part MultipartBody.Part imagefile);
}
