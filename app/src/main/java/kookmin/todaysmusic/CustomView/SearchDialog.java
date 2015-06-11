package kookmin.todaysmusic.CustomView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import kookmin.todaysmusic.Data.Music;
import kookmin.todaysmusic.MainActivity;
import kookmin.todaysmusic.R;
import kookmin.todaysmusic.StaredListActivity;
import kookmin.todaysmusic.Utils.Font;
import kookmin.todaysmusic.Utils.MusicListAdapter;
import kookmin.todaysmusic.Utils.Server;

public class SearchDialog extends Dialog implements View.OnClickListener{

    Context context;
    Spinner spinner;
    TextView searchText;
    ListView searchListView;
    MusicListAdapter searchListAdapter;
    ArrayList<Music> searchList;

    public SearchDialog(Context context){
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_search);

        Font.changeFont(findViewById(R.id.dialog_layout));

        spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setPrompt("검색옵션");
        String[] item = {"제목", "가수"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, item);
        spinner.setAdapter(adapter);

        ImageView searchImage = (ImageView)findViewById(R.id.button_search);
        searchImage.setOnClickListener(this);

        searchText = (TextView)findViewById(R.id.searchText);

        searchListView = (ListView)findViewById(R.id.searchView);
        searchList = new ArrayList<Music>();
        searchListAdapter = new MusicListAdapter(context, 0, searchList, Music.SEARCH);
        searchListView.setAdapter(searchListAdapter);
        searchListView.setSelector(R.drawable.list_selector);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MusicDialog dialog = new MusicDialog(context, searchList.get(position));
                dialog.show();
                dialog.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if(context instanceof StaredListActivity)
                            ((StaredListActivity)context).refresh();
                        else if(context instanceof MainActivity)
                            ((MainActivity)context).refresh();
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v){
        if(v.getId() == R.id.button_search && searchText.getText() != null){
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String string = searchText.getText().toString();
                    if (spinner.getSelectedItemPosition() == 0)
                        Server.searchMusic(searchList, string, null);
                    else
                        Server.searchMusic(searchList, null, string);

                    ((Activity)context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            searchListAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();
        }
    }
}
