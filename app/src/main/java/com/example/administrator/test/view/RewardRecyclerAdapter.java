package com.example.administrator.test.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.test.R;
import com.example.administrator.test.RewardAllot;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by glp on 2016/7/1.
 */

public class RewardRecyclerAdapter extends RecyclerView.Adapter<RewardRecyclerAdapter.Holder> {

    RewardAllot mAllot;

    public void setCount(int mCount) {
        this.mCount = mCount;
    }

    int mCount;

    public RewardRecyclerAdapter(int count) {
        mAllot = RewardAllot.GetInstance();
        mCount = count;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        //View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_list_percent,parent,false);
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        Holder holder = new Holder(item);

        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        if (holder != null) {
            if (holder.rank != null) {
                holder.rank.setText(mAllot.getRewardRank(position));
                holder.percent.setText("" + String.format("%.3f", mAllot.getRewardPercent(position, mCount)) + "%");
            }
        }
    }

    @Override
    public int getItemCount() {
        return mAllot.getListSize(mCount);
    }

    public static class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_rank)
        TextView rank;
        @BindView(R.id.text_percent)
        TextView percent;

        public Holder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
