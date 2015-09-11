package com.chdev.ks.minx;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.leonardoxh.customfont.FontText;

/**
 * Created by Kartik_ch on 8/28/2015.
 */
public class MainFragment extends Fragment {

    FontText welcome_title_txt, welcome_body_txt, network_txt;
    LinearLayout imageLayout;
    ImageView imageFace;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        welcome_title_txt= (FontText) getView().findViewById(R.id.welcome_title_txt);
        welcome_body_txt= (FontText) getView().findViewById(R.id.welcome_body_txt);
        network_txt= (FontText) getView().findViewById(R.id.network_txt);
        imageLayout= (LinearLayout) getView().findViewById(R.id.imageLayout);
        imageFace= (ImageView) getView().findViewById(R.id.imageFace);
        testNetworkAvailability();
    }

    private void testNetworkAvailability() {
        //To check if internet connection is available or not
        Boolean connection = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            if (networkInfos != null) {
                for (int i = 0; i < networkInfos.length; i++) {
                    if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
                        connection = true;
                    }
                }
            }
        }
        if (!connection) {
            imageFace.setImageResource(R.drawable.sad_face);
            network_txt.setVisibility(View.VISIBLE);
        }
    }
}
