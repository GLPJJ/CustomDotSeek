package com.example.administrator.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2016/6/22.
 */

public class RewardListAdapter extends BaseAdapter {

    Context mContext;
    RewardAllot mAllot;

    public void setCount(int mCount) {
        this.mCount = mCount;
    }

    int mCount;

    public RewardListAdapter(Context context,int count){
        mAllot = RewardAllot.GetInstance();
        mContext = context;
        mCount = count;
    }

    @Override
    public int getCount() {
        return mAllot.getListSize(mCount);
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder vh = null;
        if(view == null)
        {
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_list_percent,null);
            vh = new ViewHolder();
            ButterKnife.bind(vh,view);
            view.setTag(vh);
        }
        else
        {
            vh = (ViewHolder)view.getTag();
        }

        if(vh.rank != null)
        {
            vh.rank.setText(mAllot.getRewardRank(i));
            vh.percent.setText(""+String.format("%.3f",mAllot.getRewardPercent(i,mCount))+"%");
        }


        return view;
    }

    static class ViewHolder{
        @BindView(R.id.text_rank) TextView rank;
        @BindView(R.id.text_percent) TextView percent;
    }
}
