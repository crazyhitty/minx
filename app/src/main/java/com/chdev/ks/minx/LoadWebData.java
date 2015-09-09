package com.chdev.ks.minx;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.chdev.ks.minx.DatabaseManager.DataBaseAdapter;
import com.github.leonardoxh.customfont.FontText;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Kartik_ch on 8/15/2015.
 */
public class LoadWebData extends AsyncTask<Void, Void, String> {
    String search_url, title, body, link;
    String[] linkArr;
    String sharedPrefs;
    Elements paragraphs;
    Elements links;
    Document document;
    FragmentActivity activity;
    ProgressWheel progressViewLoading;
    FontText title_txt, body_txt;
    Button btn_view_links;
    MainActivity mainActivity;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public LoadWebData(String search_url, FragmentActivity activity) {
        super();
        this.search_url = search_url;
        this.activity = activity;
        progressViewLoading = (ProgressWheel) (activity).findViewById(R.id.progressViewLoading);
        title_txt = (FontText) (activity).findViewById(R.id.title_txt);
        body_txt = (FontText) (activity).findViewById(R.id.body_txt);
        btn_view_links = (Button) (activity).findViewById(R.id.btn_view_links);
        mainActivity=(MainActivity)activity;
        sharedPrefs=activity.getResources().getString(R.string.sharedPrefs);
        sharedPreferences=activity.getSharedPreferences(sharedPrefs, Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            document = Jsoup.connect(search_url).get();
            title = document.title();
            paragraphs = document.select("p");
            links = document.select("a[href]");
            body = convertParagraphsToString();
            link = convertLinksToString();
            linkArr = convertLinksToStringArr();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(activity, "Error : "+e.toString(), Toast.LENGTH_LONG).show();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressViewLoading.setVisibility(View.VISIBLE);
        btn_view_links.setVisibility(View.INVISIBLE);
        title_txt.setText("");
        body_txt.setText("");
        clearSharedPrefs();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            progressViewLoading.setVisibility(View.INVISIBLE);
            btn_view_links.setVisibility(View.VISIBLE);
            setLoadedValues();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, activity.getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
            //Toast.makeText(activity, "Error : "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void setLoadedValues() {
        setFontSize();
        title_txt.setText(title);
        body_txt.setText(body);
        //links_txt.setText(link);
        showTitleBody(title_txt, body_txt);
        loadLinks();
        setDataIntoSharedPrefs(search_url, title, body);
        title = "";
        body = "";
    }

    private void setFontSize() {
        //setting title font
        SettingsPreferences settingsPreferencesTitle=new SettingsPreferences(activity, "title");
        String titleFont=settingsPreferencesTitle.retrieveFontPreferences();
        title_txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(titleFont));
        //setting body font
        SettingsPreferences settingsPreferencesBody=new SettingsPreferences(activity, "body");
        String bodyFont=settingsPreferencesBody.retrieveFontPreferences();
        body_txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, Float.parseFloat(bodyFont));
    }

    private void loadLinks() {
        btn_view_links.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(activity)
                        .title(R.string.dialog_link_title)
                        .items(linkArr)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                String[] linkSingle = text.toString().split("http:");
                                String linkFinal="http:"+linkSingle[1];
                                //linkSingle[1]
                                //Toast.makeText(activity, "http:"+linkSingle[1].trim(), Toast.LENGTH_SHORT).show();
                                LoadWebData loadWebData=new LoadWebData(linkFinal, activity);
                                //change search text field with new url
                                mainActivity.search_txt.setText(linkFinal);
                                if(!search_url.equals("")) {
                                    //Toast.makeText(getActivity(), search_url, Toast.LENGTH_SHORT).show();
                                    loadWebData.execute();
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private String convertParagraphsToString() {
        String para = "";
        for (int i = 0; i < paragraphs.size(); i++) {
            para += paragraphs.get(i).text().trim() + "\n\n";
        }
        return para;
    }

    private String convertLinksToString() {
        String allLinks = "";
        for (int i = 0; i < links.size(); i++) {
            if (links.get(i).text().trim().isEmpty()) {
                allLinks += links.get(i).attr("abs:href") + "\n\n";
            } else {
                allLinks += links.get(i).text() + " : " + links.get(i).attr("abs:href") + "\n\n";
            }
        }
        return allLinks;
    }

    private String[] convertLinksToStringArr() {
        ArrayList<String> arrLst = new ArrayList<String>();
        for (int i = 0; i < links.size(); i++) {
            if (links.get(i).text().trim().isEmpty()) {
                arrLst.add(links.get(i).attr("abs:href"));
            } else {
                arrLst.add("\n" + links.get(i).text() + "\n" + links.get(i).attr("abs:href") + "\n");
            }
        }
        String[] stringArray = arrLst.toArray(new String[arrLst.size()]);
        return stringArray;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    private void showTitleBody(final FontText title_txt, final FontText body_txt) {
        FadeAnimation fadeAnimation = new FadeAnimation(activity);
        //fadeAnimation.fadeInLeft(title_txt);
        //fadeAnimation.fadeInLeft(body_txt);
        fadeAnimation.fadeInAlpha(title_txt);
        fadeAnimation.fadeInAlpha(body_txt);
    }

    private void setDataIntoSharedPrefs(String url, String title, String body){
        editor.putString("url", url);
        editor.putString("title", title);
        editor.putString("body", body);
        editor.commit();
    }

    private void clearSharedPrefs(){
        editor.clear().commit();
    }

}
