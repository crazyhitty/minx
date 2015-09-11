package com.chdev.ks.minx;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.balysv.materialripple.MaterialRippleLayout;
import com.github.leonardoxh.customfont.FontText;

/**
 * Created by Kartik_ch on 8/28/2015.
 */
public class LibraryListAdapter extends BaseAdapter{
    Context context;
    LayoutInflater layoutInflater;
    String[] libraryName, libraryDesc, libraryUrl;
    public LibraryListAdapter(Context context) {
        this.context=context;
        layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        libraryName=context.getResources().getStringArray(R.array.library_name);
        libraryDesc=context.getResources().getStringArray(R.array.library_desc);
        libraryUrl=context.getResources().getStringArray(R.array.library_url);
    }

    @Override
    public int getCount() {
        return libraryName.length;
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
        FontText libraryOwnerTxt=(FontText)view.findViewById(R.id.library_name_txt);
        FontText libraryDescTxt=(FontText)view.findViewById(R.id.library_desc_txt);
        libraryOwnerTxt.setText("\n"+libraryName[position]);
        libraryDescTxt.setText(libraryDesc[position] + "\n");
        ripple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context, libraryName[position], Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(libraryUrl[position]));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return view;
    }
}
