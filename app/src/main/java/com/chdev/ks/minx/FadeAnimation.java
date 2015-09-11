package com.chdev.ks.minx;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by Kartik_ch on 8/17/2015.
 */
public class FadeAnimation {

    Context context;

    public FadeAnimation(Context context) {
        this.context = context;
    }

    public void fadeInLeft(View view) {
        view.setVisibility(View.INVISIBLE);
        Animation fadeInAnim = AnimationUtils.makeInAnimation(context, true);
        view.startAnimation(fadeInAnim);
        view.setVisibility(View.VISIBLE);
        Log.d("fadeIn", "true");
    }

    public void fadeOutRight(View view) {
        Animation fadeOutAnim = AnimationUtils.makeOutAnimation(context, true);
        view.startAnimation(fadeOutAnim);
        view.setVisibility(View.INVISIBLE);
        Log.d("fadeOut", "true");
    }

    public void fadeInAlpha(View view){
        view.setVisibility(View.INVISIBLE);
        AlphaAnimation alphaAnimation=new AlphaAnimation(0f, 1.0f);
        alphaAnimation.setDuration(500);
        view.startAnimation(alphaAnimation);
        view.setVisibility(View.VISIBLE);
    }

    public void fadeOutAlpha(View view){
        view.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation=new AlphaAnimation(1.0f, 0f);
        alphaAnimation.setDuration(500);
        view.startAnimation(alphaAnimation);
        view.setVisibility(View.GONE);
    }
}
