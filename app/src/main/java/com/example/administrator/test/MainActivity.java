package com.example.administrator.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.example.administrator.test.view.RewardRecyclerAdapter;
import com.jakewharton.rxbinding.view.RxView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    public static final String Tag = "MainActivity";

    Subscription mTextSubscription;

    @BindView(R.id.img) ImageView mImg;
    @BindView(R.id.editText) EditText mEditText;
    @BindView(R.id.list) ListView mList;
    @BindView(R.id.recycler) RecyclerView mRecycler;
    @BindView(R.id.button2) Button mBtn2;

    RewardListAdapter mAdapter;
    RewardRecyclerAdapter mAdapter2;

    static boolean mIsShow = true;

    @OnClick(R.id.button) void onClickBtn(){
        int count = Integer.parseInt("0"+mEditText.getText().toString());
        mAdapter.setCount(count);
        mAdapter.notifyDataSetChanged();

        mAdapter2.setCount(count);
        mAdapter2.notifyDataSetChanged();

        mIsShow = !mIsShow;
        mImg.setVisibility(mIsShow?View.VISIBLE:View.GONE);
    }

    @OnClick(R.id.buttonNext) void onClickNext(){
        startActivity(new Intent(this,AActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ButterKnife.bind(this);

        Glide.with(this)
                .load("http://img3.duitang.com/uploads/item/201406/24/20140624172202_JfevR.jpeg")
                //.centerCrop()
                .placeholder(R.mipmap.de)
                .crossFade()
                .into(mImg);

        //mList = (ListView) findViewById(R.id.list);

        mAdapter = new RewardListAdapter(this,0);
        mList.setAdapter(mAdapter);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        //mRecycler.setHasFixedSize(true);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        //mRecycler.setLayoutManager(new GridLayoutManager(this, 2));//这里用线性宫格显示 类似于grid view
        //mRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));//这里用线性宫格显示 类似于瀑布流
        mAdapter2 = new RewardRecyclerAdapter(0);
        mRecycler.setAdapter(mAdapter2);


        //订阅之后必须在适当的时候取消订阅
        mTextSubscription = RxView.clicks(mBtn2)
                .throttleFirst(1, TimeUnit.SECONDS)//1秒后才能点击,防止手抖，对手抖有要求的按钮
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.e(Tag,"Click TextView");

                        /***
                         *
                         * Schedulers.immediate();//当前线程
                         * Schedulers.newThread();//新线程
                         * Schedulers.io();//IO线程，用于文件，数据库，网络IO
                         * Schedulers.computation()//计算线程，只有计算
                         */

                        Observable.just("Hello")//Observable.just("Hello","1")
                                .subscribeOn(Schedulers.io())//开辟新线程
                                .observeOn(Schedulers.io())
                                .map(new Func1<String, String>() {
                                    @Override
                                    public String call(String s) {
                                        Log.e(Tag,"map1 , "+s+" , Thread = "+Thread.currentThread().toString());

                                        //同步 OKHttp Get
                                        OkHttpClient client = new OkHttpClient();
                                        Request request = new Request.Builder()
                                                //.url("http://publicobject.com/helloworld.txt")
                                                .url("https://www.baidu.com/")
                                                .build();

                                        Response response = null;
                                        try {
                                            response  = client.newCall(request).execute();

                                            Headers responseHeaders = response.headers();
                                            for (int i = 0; i < responseHeaders.size(); i++) {
                                                Log.d(Tag,"Get Headers : "+responseHeaders.name(i) + ": " + responseHeaders.value(i));
                                            }

                                            Log.d(Tag,"Get Body : "+response.body().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }


                                        //OkHttp Post
                                        OkHttpClient client1 = new OkHttpClient();

                                        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                                        String json = "{'winCondition':'HIGH_SCORE',"
                                                + "'name':'Bowling',"
                                                + "'round':4,"
                                                + "'lastSaved':1367702411696,"
                                                + "'dateStarted':1367702378785,"
                                                + "'players':["
                                                + "{'name':'" + "player1" + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                                                + "{'name':'" + "player2" + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                                                + "]}";
                                        RequestBody body = RequestBody.create(JSON, json);
                                        Request request1 = new Request.Builder()
                                                .url("http://www.roundsapp.com/post")
                                                .post(body)
                                                .build();
                                        //response = null;
                                        try {
                                            response = client1.newCall(request1).execute();

                                            Log.d(Tag,"POST : "+response.body().string());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                        return s+" map1";
                                    }
                                })
                                .observeOn(Schedulers.newThread())
                                .map(new Func1<String, String>() {
                                    @Override
                                    public String call(String s) {
                                        Log.e(Tag,"map2 , "+s+" , Thread = "+Thread.currentThread().toString());
                                        return s+" ,map2";
                                    }
                                })
//                              .flatMap(new Func1<String, Observable<?>>() {
//                                  @Override
//                                  public Observable<?> call(String s) {
//                                      return null;
//                                  }
//                              })
                                .observeOn(AndroidSchedulers.mainThread())
                                //.subscribe(subscriber);
                                .subscribe(new Action1<String>() {
                                    @Override
                                    public void call(String s) {
                                        Log.e(Tag, "call , " + s + " , Thread = " + Thread.currentThread().toString());
                                    }
                                }, new Action1<Throwable>() {
                                    @Override
                                    public void call(Throwable throwable) {
                                        throwable.printStackTrace();
                                        Log.e(Tag, "call , Error" + " , Thread = " + Thread.currentThread().toString());
                                    }
                                }, new Action0() {
                                    @Override
                                    public void call() {
                                        Log.e(Tag, "call , Completed" + " , Thread = " + Thread.currentThread().toString());
                                    }
                                });
                    }
                });

        //观察者
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                Log.d(Tag,"onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(Tag,"onError");
                e.printStackTrace();
            }

            @Override
            public void onNext(String s) {
                Log.d(Tag,"onNext , "+s+" , Thread = "+Thread.currentThread().toString());
            }
        };

        //被观察者
        Observable observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("Hello");
                subscriber.onNext("1");
                subscriber.onNext("2");
                subscriber.onCompleted();
            }
        });

        //绑定监听
        //observable.subscribe(subscriber);//基本触发事件

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if( mTextSubscription!= null && !mTextSubscription.isUnsubscribed())
            mTextSubscription.unsubscribe();
    }
}
