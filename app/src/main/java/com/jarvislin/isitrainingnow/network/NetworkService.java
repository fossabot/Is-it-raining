package com.jarvislin.isitrainingnow.network;

import android.content.Context;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

import com.jarvislin.isitrainingnow.BuildConfig;
import com.jarvislin.isitrainingnow.model.WeatherStation;

/**
 * Created by JarvisLin on 2017/1/7.
 */

public class NetworkService {
    private static final String RAINING_OPEN_DATA_API_URL = "http://opendata.epa.gov.tw/ws/Data/RainTenMin/";

    private OpenDataApi openDataApi;

    public NetworkService() {
        init();
    }

    private void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(new ToStringConverterFactory())
                .baseUrl(RAINING_OPEN_DATA_API_URL)
                .client(new OkHttpClient.Builder()
                        .readTimeout(30, TimeUnit.SECONDS)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(BuildConfig.DEBUG
                                ? HttpLoggingInterceptor.Level.BODY
                                : HttpLoggingInterceptor.Level.NONE))
                        .build())
                .build();

        openDataApi = retrofit.create(OpenDataApi.class);
    }


    public Observable<ArrayList<WeatherStation>> fetchWeatherStation() {
        return openDataApi.weatherStation();
    }
}
