package com.aliouswang.alayout;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by aliouswang on 2017/10/11.
 */

public class DefaultAdapter extends RecyclerView.Adapter<DefaultViewHolder>{

    private String[] data = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19"};

    private int[] colors = {Color.RED, Color.GREEN, Color.BLUE};

    @Override
    public DefaultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.default_item, parent, false);
        return new DefaultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DefaultViewHolder holder, int position) {
        TextView textView = holder.itemView.findViewById(R.id.textview);
        textView.setText(data[position]);
        textView.setBackgroundColor(colors[position % colors.length]);
    }

    @Override
    public int getItemCount() {
        return data.length;
    }
}
