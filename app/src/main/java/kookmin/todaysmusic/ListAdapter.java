package kookmin.todaysmusic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends ArrayAdapter<Music>{
    Context mContext;

    private ArrayList<Music> items;
    int type;

    public ListAdapter(Context context, int textViewResourceId, ArrayList<Music> items, int type){
        super(context, textViewResourceId, items);
        mContext = context;
        this.items = items;
        this.type = type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(type == Music.SERVER_REC)
                view = inflater.inflate(R.layout.item_recommend, null);
            else
                view = inflater.inflate(R.layout.item_timeline, null);
        }

        Music m = items.get(position);
        if(m != null){
            TextView titleArtist = (TextView) view.findViewById(R.id.titleText);
            titleArtist.setText(m.Title + " - " + m.Artist);
        }

        return view;
    }
}
