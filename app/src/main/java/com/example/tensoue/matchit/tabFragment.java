package com.example.tensoue.matchit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class tabFragment extends Fragment {
    private RecyclerView rv_scoreboard;
    private String level;
    public static tabFragment createInstance(RecyclerView rv_scoreboard,String level)
    {
        tabFragment fragment = new tabFragment();
        fragment.rv_scoreboard = rv_scoreboard;
        fragment.level = level;
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.scoreboard_dialog,container,false);
        TextView tv_level = v.findViewById(R.id.tv_scoreboard);
        String cap = level.substring(0,1).toUpperCase() + level.substring(1);
        tv_level.setText(cap);
        RecyclerView rv_scoreboard = v.findViewById(R.id.recycler_view);
        Methods.showScoreboardRecycler(rv_scoreboard,level);
        return v;
    }
}
