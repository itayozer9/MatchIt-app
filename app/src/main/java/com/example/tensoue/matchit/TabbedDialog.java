package com.example.tensoue.matchit;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.TabWidget;
import android.widget.TableLayout;


public class TabbedDialog extends DialogFragment {


        TabLayout tabLayout;
    ViewPager viewPager;

    RecyclerView rv_scoreboard;
    public TabbedDialog(){}

    public TabbedDialog(RecyclerView rv_scoreboard) {
        this.rv_scoreboard = rv_scoreboard;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.tabbed_dialog,container,false);
        tabLayout = (TabLayout) rootview.findViewById(R.id.tabLayout);
        viewPager = (ViewPager) rootview.findViewById(R.id.masterViewPager);
        tabbedDialogAdapter adapter = new tabbedDialogAdapter(getChildFragmentManager());
        adapter.addFragment(getString(R.string.Easy),tabFragment.createInstance(rv_scoreboard,"easy"));
        adapter.addFragment(getString(R.string.Medium),tabFragment.createInstance(rv_scoreboard,"medium"));
        adapter.addFragment(getString(R.string.Hard),tabFragment.createInstance(rv_scoreboard,"hard"));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return rootview;
    }
}
