package kookmin.todaysmusic.Utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Font {
    public static Typeface font;

    public static void createFont(Context context){
        font = Typeface.createFromAsset(context.getAssets(), "fonts/Gasi_M.ttf");
    }

    public static void changeFont(View view){
        if(view instanceof ViewGroup){
            ViewGroup vg = (ViewGroup) view;
            for (int i=0; i<vg.getChildCount(); i++){
                View child = vg.getChildAt(i);
                changeFont(child);
            }
        }
        else if(view instanceof TextView){
            ((TextView)view).setTypeface(font);
        }
    }
}
