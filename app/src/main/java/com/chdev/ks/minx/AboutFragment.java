package com.chdev.ks.minx;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.balysv.materialripple.MaterialRippleLayout;

/**
 * Created by Kartik_ch on 8/27/2015.
 */
public class AboutFragment extends Fragment {

    //MaterialRippleLayout rippleImgBtnMinx;
    Button btnGithub, btnGooglePlus, btnMail;
    ListView listViewLibrary;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnGithub= (Button) getView().findViewById(R.id.btn_github);
        btnGooglePlus= (Button) getView().findViewById(R.id.btn_google_plus);
        btnMail= (Button) getView().findViewById(R.id.btn_mail);

        btnGithub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getResources().getString(R.string.github_website)));
                startActivity(intent);
            }
        });

        btnGooglePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getResources().getString(R.string.google_plus_website)));
                startActivity(intent);
            }
        });

        btnMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(getResources().getString(R.string.mail_website)));
                startActivity(intent);
            }
        });

        listViewLibrary= (ListView) getView().findViewById(R.id.libraries_list);
        listViewLibrary.setAdapter(new LibraryListAdapter(getActivity().getApplicationContext()));
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        //inflater.inflate(R.menu.menu_none, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }*/
}
