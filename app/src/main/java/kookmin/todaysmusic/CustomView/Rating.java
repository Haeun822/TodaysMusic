package kookmin.todaysmusic.CustomView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;

import kookmin.todaysmusic.R;

public class Rating extends TableLayout implements View.OnClickListener{

    View ratingView;
    ImageView[] star = new ImageView[6];
    static Bitmap star_blank, star_star;
    int starCounter = 0;

    public Rating(Context context, AttributeSet attrs) {
        super(context, attrs);

        if(star_blank == null)
            star_blank = BitmapFactory.decodeResource(getResources(), R.drawable.star_blank);
        if(star_star == null)
            star_star = BitmapFactory.decodeResource(getResources(), R.drawable.star_star);

        inflate(getContext(), R.layout.layout_ratingbar, this);
        star[0] = (ImageView)findViewById(R.id.star1);
        star[1] = (ImageView)findViewById(R.id.star2);
        star[2] = (ImageView)findViewById(R.id.star3);
        star[3] = (ImageView)findViewById(R.id.star4);
        star[4] = (ImageView)findViewById(R.id.star5);
        star[5] = (ImageView)findViewById(R.id.star6);

        for(int i=0; i<6; i++)
            star[i].setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == star[0])
            starCounter = 1;
        else if(v == star[1])
            starCounter = 2;
        else if(v == star[2])
            starCounter = 3;
        else if(v == star[3])
            starCounter = 4;
        else if(v == star[4])
            starCounter = 5;
        else if(v == star[5])
            starCounter = 6;

        setStarImage();
    }

    public void setStarImage(){
        for(int i=0; i<starCounter; i++)
            ((ImageView)star[i]).setImageBitmap(star_star);
        for(int i=starCounter; i<6; i++)
            ((ImageView)star[i]).setImageBitmap(star_blank);
    }

    public int getCount(){ return starCounter; }
    public void setCount(int count){
        if(count < 0)
            starCounter = 0;
        else
            starCounter = count;
        setStarImage();
    }
}
