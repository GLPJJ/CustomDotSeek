package com.example.administrator.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.test.api.HttpApi;
import com.example.administrator.test.modle.MovieEntity;
import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by glp on 2016/7/1.
 */

public class AActivity extends AppCompatActivity {

    public static final String Tag = "AActivity";

    @BindView(R.id.textView) TextView mText;
    @BindView(R.id.progressBar) ProgressBar mProgress;

    @BindView(R.id.button4) Button mBtn;
    @OnClick(R.id.button3) void onBtn3(){
        if(mSubscriber!=null && !mSubscriber.isUnsubscribed())
            mSubscriber.unsubscribe();
    }

    MovieSubScribe mSubscriber;
    class MovieSubScribe extends  Subscriber<MovieEntity> {

        @Override
        public void onCompleted() {
            mSubscriber.unsubscribe();
            Log.e(Tag,"Subscriber onCompleted, "+" , Thread = "+Thread.currentThread().toString());
            mProgress.setVisibility(View.GONE);
            Toast.makeText(AActivity.this,"成功",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onError(Throwable e) {
            mSubscriber.unsubscribe();
            Log.e(Tag,"Subscriber onError, "+" , Thread = "+Thread.currentThread().toString());
            mProgress.setVisibility(View.GONE);
            Toast.makeText(AActivity.this,"异常",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onNext(MovieEntity movieEntity) {
            Log.e(Tag,"Subscriber onNext, "+" , Thread = "+Thread.currentThread().toString());
            mProgress.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        RxView.clicks(mBtn).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        Log.e(Tag,"Button Click, "+" , Thread = "+Thread.currentThread().toString());
                        mSubscriber = new MovieSubScribe();
                        //不知道为何，第二次点击按钮，Http请求没有去执行。。。
                        //终于找到上面的元凶了。订阅者如果是同一个就不会触发订阅，所以我们每次点击需要new一个新的订阅者，这样才能完成我们的要求-_-
                        HttpApi.getInstance().getTopMovie(new Action0() {
                            @Override
                            public void call() {
                                Log.e(Tag,"Action0 Call, "+" , Thread = "+Thread.currentThread().toString());
                                mProgress.setVisibility(View.VISIBLE);
                            }
                        },mSubscriber,0,10);
                    }
                });
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if(mSubscriber!=null && !mSubscriber.isUnsubscribed())
            mSubscriber.unsubscribe();
    }
}
