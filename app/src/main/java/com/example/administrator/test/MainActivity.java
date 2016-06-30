package com.example.administrator.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.img) ImageView mImg;
    @BindView(R.id.editText) EditText mEditText;
    @BindView(R.id.list) ListView mList;

    RewardListAdapter mAdapter;

    static boolean mIsShow = true;

    @OnClick(R.id.button) void onClickBtn(){
        int count = Integer.parseInt("0"+mEditText.getText().toString());
        mAdapter.setCount(count);
        mAdapter.notifyDataSetChanged();

        mIsShow = !mIsShow;
        mImg.setVisibility(mIsShow?View.VISIBLE:View.GONE);
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
    }
}
