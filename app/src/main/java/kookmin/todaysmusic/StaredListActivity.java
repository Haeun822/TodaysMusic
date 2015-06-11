package kookmin.todaysmusic;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import kookmin.todaysmusic.CustomView.MusicDialog;
import kookmin.todaysmusic.CustomView.SearchDialog;
import kookmin.todaysmusic.Data.Music;
import kookmin.todaysmusic.Data.User;
import kookmin.todaysmusic.Utils.ListAdapter;

public class StaredListActivity extends Activity implements AdapterView.OnItemClickListener, Dialog.OnDismissListener{

    ListView staredListView;
    ListAdapter staredListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_timeline);

        staredListView = (ListView)findViewById(R.id.list_timeline);
        staredListView.setSelector(R.drawable.list_selector);
        staredListAdapter = new ListAdapter(this, 0, User.staredList, Music.MY_MUSIC);
        staredListView.setAdapter(staredListAdapter);
        staredListView.setOnItemClickListener(this);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mylist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_add:
                SearchDialog dialog = new SearchDialog(this);
                dialog.show();
                dialog.setOnDismissListener(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MusicDialog dialog = new MusicDialog(this, User.staredList.get(position));
        dialog.show();
        dialog.setOnDismissListener(this);
    }

    public void onDismiss(DialogInterface dialog){
        staredListAdapter.notifyDataSetChanged();
    }

    public void refresh(){
        staredListAdapter.notifyDataSetChanged();

    }
}
