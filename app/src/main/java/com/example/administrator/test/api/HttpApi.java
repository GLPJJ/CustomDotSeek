package com.example.administrator.test.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.administrator.test.exce.HttpMyException;
import com.example.administrator.test.modle.ModleT;
import com.example.administrator.test.modle.MovieEntity;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by glp on 2016/7/1.
 */

public class HttpApi {

    public static final String Tag = "HttpApi";

    //https://api.douban.com/v2/movie/top250?start=0&count=10
    public static final String sBaseUrl = "https://api.douban.com/v2/movie/";//"https://www.baidu.com/"

    private static final int DEFAULT_TIMEOUT = 5;

    static HttpApi api = null;
    public static HttpApi getInstance(){
        if(api == null)
            api = new HttpApi();
        return api;
    }

    public Retrofit getRetrofitIns(){
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .baseUrl(sBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())//转换Json
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//返回被观察者
                .build();
        return retrofit;
    }

    //提供对 通用返回数据的转换。
    private class HttpResultFunc<T> implements Func1<ModleT<T>,T>{

        @Override
        public T call(ModleT<T> tModleT) {
            Log.e(Tag,"HttpResultFunc, "+" , Thread = "+Thread.currentThread().toString());
            if(tModleT.result != 0 )
                throw new HttpMyException(tModleT.result,tModleT.msg);
            else
                return tModleT.data;
        }
    }

    public interface MovieService{
        @GET("top250")
        Observable<ModleT<MovieEntity>> getTopMovie(@Query("start") int start, @Query("count") int count);
        //或者是
        //Observable<ModleT<List<MovieEntity>>> getTopMovieList(@Query("start") int start, @Query("count") int count);

        @GET("top250")
        Call<ModleT<MovieEntity>> getTopMovieCall(@Query("start") int start, @Query("count") int count);
    }

    Call<ModleT<MovieEntity>> call;
    /**
     * 用于获取豆瓣电影Top250的数据
     * @param subscribeAction 订阅触发的某个操作,比如初始化等待框
     * @param subscriber 观察者对象
     * @param start 起始位置
     * @param count 获取长度
     */
    public void getTopMovie(@NonNull Action0 subscribeAction,@NonNull Subscriber<MovieEntity> subscriber, int start, int count){

        getRetrofitIns().create(MovieService.class).getTopMovie(start, count)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(subscribeAction)//运行在后面跟的最近的subscribeOn指定的线程
                .subscribeOn(AndroidSchedulers.mainThread())
                .map(new HttpResultFunc<MovieEntity>())//对返回的Http通用数据进行转换
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }
    //...
}
