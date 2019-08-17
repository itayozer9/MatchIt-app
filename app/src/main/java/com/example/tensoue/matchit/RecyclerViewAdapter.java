package com.example.tensoue.matchit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<scoreboardRow> rows = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context mContext,ArrayList<scoreboardRow> rows) {
        this.rows = rows;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_rowitem,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d("RecyclerViewAdapter", "onBindViewHolder: called");
        Glide.with(mContext)
                .load(rows.get(position).getProfilePicURL())
                .asBitmap()
                .into(holder.iv_pic);
        holder.tv_username.setText(rows.get(position).getUsername());
        holder.tv_highestscore.setText(String.valueOf(rows.get(position).getHighestScore()));
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_pic;
        TextView tv_username;
        TextView tv_highestscore;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_pic=itemView.findViewById(R.id.iv_pic_scoreboard);
            tv_username = itemView.findViewById(R.id.tv_username_scoreboard);
            tv_highestscore =itemView.findViewById(R.id.tv_highestscore_scoreboard);
            parentLayout = itemView.findViewById(R.id.scoreboard_item_layout);
        }
    }
}
