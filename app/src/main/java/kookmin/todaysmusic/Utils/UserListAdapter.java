package kookmin.todaysmusic.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kookmin.todaysmusic.R;

public class UserListAdapter extends ArrayAdapter<String>{
    Context mContext;

    private ArrayList<String> items;

    public UserListAdapter(Context context, int textViewResourceId, ArrayList<String> items){
        super(context, textViewResourceId, items);
        mContext = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View view = convertView;
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_follow, null);
        }
        String s = items.get(position);
        if(s != null){
            TextView t = (TextView)view.findViewById(R.id.userID);
            t.setTypeface(Font.font);
            t.setText(s);
        }

        return view;
    }
}
