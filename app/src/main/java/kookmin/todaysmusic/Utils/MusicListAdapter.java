package kookmin.todaysmusic.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kookmin.todaysmusic.Data.Music;
import kookmin.todaysmusic.R;

public class MusicListAdapter extends ArrayAdapter<Music>{
    Context mContext;

    private ArrayList<Music> items;
    int type;

    public static Bitmap bitmap_star;

    public MusicListAdapter(Context context, int textViewResourceId, ArrayList<Music> items, int type){
        super(context, textViewResourceId, items);
        mContext = context;
        this.items = items;
        this.type = type;

        if(bitmap_star == null)
            bitmap_star = BitmapFactory.decodeResource(context.getResources(), R.drawable.star_star);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(type == Music.SERVER_REC || type == Music.SEARCH)
                view = inflater.inflate(R.layout.item_recommend, null);
            else
                view = inflater.inflate(R.layout.item_timeline, null);
        }
        Music m = items.get(position);
        if(m != null){
            if(m.thumb != null) {
                ImageView image = (ImageView) view.findViewById(R.id.user_image);
                image.setImageBitmap(m.thumb);
            }

            TextView titleArtist = (TextView) view.findViewById(R.id.titleText);
            titleArtist.setText(m.Title + " - " + m.Artist);
            titleArtist.setTypeface(Font.font);

            if(type == Music.SEARCH){
                titleArtist.setTextColor(Color.parseColor("#404040"));
            }
            if(type == Music.USER_REC){
                TextView t = (TextView)view.findViewById(R.id.timeText);
                t.setTypeface(Font.font);
                t.setText(m.SharedTime);

                t = (TextView)view.findViewById(R.id.userText);
                t.setTypeface(Font.font);
                t.setText(m.UserID+"님이 같이 듣고 싶어합니다.");
            }
            if(type == Music.MY_MUSIC){
                ImageView i = (ImageView)view.findViewById(R.id.friend_profile);
                i.setVisibility(View.GONE);
                TextView t = (TextView)view.findViewById(R.id.timeText);
                t.setVisibility(View.GONE);
                t = (TextView)view.findViewById(R.id.userText);
                t.setVisibility(View.GONE);
            }

            if(type == Music.USER_REC || type == Music.MY_MUSIC){
                int star = m.Star;
                if(star != -1){
                    ImageView i = (ImageView)view.findViewById(R.id.starImage);
                    i.setImageBitmap(bitmap_star);
                    TextView t = (TextView)view.findViewById(R.id.starCount);
                    t.setTypeface(Font.font);
                    t.setText(star+"");
                    t.setVisibility(View.VISIBLE);
                }
            }
        }

        return view;
    }
}
