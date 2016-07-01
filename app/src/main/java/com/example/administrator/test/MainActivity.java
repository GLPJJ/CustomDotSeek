package com.example.administrator.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.example.administrator.test.view.RewardRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.img) ImageView mImg;
    @BindView(R.id.editText) EditText mEditText;
    @BindView(R.id.list) ListView mList;
    @BindView(R.id.recycler) RecyclerView mRecycler;

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
    }
}
