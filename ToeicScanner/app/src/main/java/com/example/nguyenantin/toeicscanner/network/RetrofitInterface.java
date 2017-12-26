package com.example.nguyenantin.toeicscanner.network;

import com.example.nguyenantin.toeicscanner.model.NewSky;
import com.example.nguyenantin.toeicscanner.model.Response;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by nguyenantin on 12/26/17.
 */

public interface RetrofitInterface {

    @POST("authenticate")
    Observable<Response> pass();

    @GET("newsky/{made}")
    Observable<NewSky> getProfile(@Path("made") String made);
}
