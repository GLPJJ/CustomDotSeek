package com.example.administrator.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.editText) EditText mEditText;
    @BindView(R.id.list) ListView mList;

    RewardListAdapter mAdapter;

    @OnClick(R.id.button) void onClickBtn(){
        int count = Integer.parseInt("0"+mEditText.getText().toString());
        mAdapter.setCount(count);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ButterKnife.bind(this);

        //mList = (ListView) findViewById(R.id.list);

        mAdapter = new RewardListAdapter(this,0);
        mList.setAdapter(mAdapter);
    }
}
