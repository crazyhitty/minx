package com.chdev.ks.minx;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.balysv.materialripple.MaterialRippleLayout;

import butterknife.ButterKnife;

/**
 * Created by Kartik_ch on 8/15/2015.
 */
public class HomeFragment extends Fragment {

    String search_url, title, body;
    MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Log.d("test","true");
        ButterKnife.bind(getActivity());
        mainActivity = (MainActivity) getActivity();
        search_url = mainActivity.getSearchTxt();
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoadWebData loadWebData = new LoadWebData(search_url, getActivity());
        if (!search_url.equals("")) {
            //Toast.makeText(getActivity(), search_url, Toast.LENGTH_SHORT).show();
            loadWebData.execute();
        }
    }

}
