package com.chdev.ks.minx.DatabaseManager;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Kartik_ch on 8/29/2015.
 */
public class DataBaseOperations {
    //String title, url, body;
    Context context;
    ArrayList<String> titleArrLst = new ArrayList<>();
    ArrayList<String> urlArrLst = new ArrayList<>();
    ArrayList<String> bodyArrLst = new ArrayList<>();
    ArrayList<String> dateArrLst = new ArrayList<>();

    public DataBaseOperations(Context context) {
        this.context = context;
    }

    public void saveDataInDB(String title, String url, String body) {
        // ' (single apostrophe) doesn't work with sqlite database in insertion, instead of it, use ''(double apostrophe). tldr : store ' as '' otherwise it won't work
        title=title.replace("'","''");
        body=body.replace("'","''");
        Date date = new Date();
        CharSequence initTime = DateFormat.format("yyyy-MM-dd hh:mm:ss", date.getTime());
        String time = initTime.toString();
        DataBaseAdapter dbAdapter = new DataBaseAdapter(context);
        dbAdapter.createDatabase();
        dbAdapter.open();
        String query = "INSERT INTO webpage (title, url, desc, date) " +
                "VALUES ('" + title + "', '" + url + "', '" + body + "', '" + time + "')";
        dbAdapter.executeQuery(query);
        dbAdapter.close();
        Toast.makeText(context, "Data saved successfully", Toast.LENGTH_SHORT).show();
    }

    public void retrieveFromDB() {
        DataBaseAdapter dbAdapter = new DataBaseAdapter(context);
        dbAdapter.createDatabase();
        dbAdapter.open();
        String query = "SELECT * FROM webpage";
        Cursor cursor = dbAdapter.selectQuery(query);
        if (cursor != null && cursor.getCount() != 0) {
            if (cursor.moveToFirst()) {
                do {
                    titleArrLst.add(cursor.getString(cursor.getColumnIndex("title")));
                    urlArrLst.add(cursor.getString(cursor.getColumnIndex("url")));
                    bodyArrLst.add(cursor.getString(cursor.getColumnIndex("desc")));
                    dateArrLst.add(cursor.getString(cursor.getColumnIndex("date")));
                } while (cursor.moveToNext());
            }
        }
        dbAdapter.close();
        Toast.makeText(context, "Data retrieved successfully", Toast.LENGTH_SHORT).show();
    }

    public void deleteFromDB(String selectedUrl){
        DataBaseAdapter dbAdapter = new DataBaseAdapter(context);
        dbAdapter.createDatabase();
        dbAdapter.open();
        String query = "DELETE FROM webpage " +
                "WHERE url='"+selectedUrl+"'";
        dbAdapter.executeQuery(query);
        dbAdapter.close();
        Toast.makeText(context, "Data deleted successfully", Toast.LENGTH_SHORT).show();
    }

    public void deleteAllFromDB(){
        DataBaseAdapter dbAdapter=new DataBaseAdapter(context);
        dbAdapter.createDatabase();
        dbAdapter.open();
        String query="DELETE FROM webpage";
        dbAdapter.executeQuery(query);
        dbAdapter.close();
        Toast.makeText(context, "Database deleted successfully", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<String> getTitleArrLst() {
        return titleArrLst;
    }

    public ArrayList<String> getUrlArrLst() {
        return urlArrLst;
    }

    public ArrayList<String> getBodyArrLst() {
        return bodyArrLst;
    }

    public ArrayList<String> getDateArrLst() {
        return dateArrLst;
    }
}
