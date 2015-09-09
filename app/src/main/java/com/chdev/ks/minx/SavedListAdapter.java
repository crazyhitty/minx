package com.chdev.ks.minx;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.github.leonardoxh.customfont.FontText;

import java.util.ArrayList;

/**
 * Created by Kartik_ch on 8/29/2015.
 */
public class SavedListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<String> titleArrLst = new ArrayList<>();
    ArrayList<String> urlArrLst = new ArrayList<>();
    ArrayList<String> bodyArrLst = new ArrayList<>();
    ArrayList<String> dateArrLst = new ArrayList<>();
    public SavedListAdapter(Context context, ArrayList<String> titleArrLst, ArrayList<String> urlArrLst, ArrayList<String> bodyArrLst, ArrayList<String> dateArrLst) {
        this.context=context;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.titleArrLst=titleArrLst;
        this.urlArrLst=urlArrLst;
        this.bodyArrLst=bodyArrLst;
        this.dateArrLst=dateArrLst;
    }

    @Override
    public int getCount() {
        return urlArrLst.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        view=layoutInflater.inflate(R.layout.custom_list_single_item, null);
        MaterialRippleLayout ripple=(MaterialRippleLayout)view.findViewById(R.id.ripple);
        final FontText titleTxt=(FontText)view.findViewById(R.id.library_name_txt);
        FontText bodyTxt=(FontText)view.findViewById(R.id.library_desc_txt);
        titleTxt.setText("\n"+titleArrLst.get(position).trim());
        bodyTxt.setText(urlArrLst.get(position)+ "\n");
        ripple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, titleArrLst.get(position).trim(), Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(context, SavedExtndActivity.class);
                intent.putExtra("saved_extnd_title", titleArrLst.get(position));
                intent.putExtra("saved_extnd_url", urlArrLst.get(position));
                intent.putExtra("saved_extnd_body", bodyArrLst.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return view;
    }
}
