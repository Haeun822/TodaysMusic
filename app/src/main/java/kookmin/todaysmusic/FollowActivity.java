package kookmin.todaysmusic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import kookmin.todaysmusic.CustomView.UserSearchDialog;
import kookmin.todaysmusic.Data.User;
import kookmin.todaysmusic.Utils.Server;
import kookmin.todaysmusic.Utils.UserListAdapter;
import kookmin.todaysmusic.Utils.Font;

public class FollowActivity extends Activity implements AdapterView.OnItemClickListener{

    ListView followedListView;
    ListView followerListView;
    UserListAdapter followedListAdapter;
    UserListAdapter followerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_follow);

        Font.changeFont(findViewById(R.id.layout_follow));

        followedListView = (ListView)findViewById(R.id.FollowedList);
        followedListAdapter = new UserListAdapter(this, 0, User.followed);
        followedListView.setAdapter(followedListAdapter);
        followedListView.setOnItemClickListener(this);

        followerListView = (ListView)findViewById(R.id.FollowerList);
        followerListAdapter = new UserListAdapter(this, 0, User.follower);
        followerListView.setAdapter(followerListAdapter);
        followerListView.setOnItemClickListener(this);

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
                UserSearchDialog dialog = new UserSearchDialog(this);
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final String userID = (String)parent.getItemAtPosition(position);

        boolean check = false;
        int index = -1;
        for(int i=0; i< User.followed.size(); i++)
            if(User.followed.get(i).equals(userID)) {
                index = i;
                check = true;
            }

        String msg = "";
        if(check)
            msg = "팔로잉중인 유저입니다. 팔로잉을 해제하시겠습니까?";
        else
            msg = "이 유저를 팔로잉하시겠습니까?";

        final boolean finalCheck = check;
        final int finalIndex = index;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage(msg).setCancelable(false);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(finalCheck){
                    User.followed.remove(finalIndex);
                    Server.follow(User.ID, userID, "Unregister");
                }
                else{
                    User.followed.add(userID);
                    Server.follow(User.ID, userID, "Register");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        followedListAdapter.notifyDataSetChanged();
                        followerListAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }
}
