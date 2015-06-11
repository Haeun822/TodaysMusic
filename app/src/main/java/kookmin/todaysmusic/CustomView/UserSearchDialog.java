package kookmin.todaysmusic.CustomView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import kookmin.todaysmusic.Data.User;
import kookmin.todaysmusic.R;
import kookmin.todaysmusic.Utils.Font;
import kookmin.todaysmusic.Utils.Server;
import kookmin.todaysmusic.Utils.UserListAdapter;

public class UserSearchDialog extends Dialog implements View.OnClickListener{

    Context context;
    TextView searchText;
    ListView searchListView;
    UserListAdapter searchListAdapter;
    ArrayList<String> searchList;

    public UserSearchDialog(Context context){
        super(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_search_user);

        Font.changeFont(findViewById(R.id.dialog_layout));

        ImageView searchImage = (ImageView)findViewById(R.id.button_search);
        searchImage.setOnClickListener(this);

        searchText = (TextView)findViewById(R.id.searchText);

        searchListView = (ListView)findViewById(R.id.searchView);
        searchList = new ArrayList<String>();
        searchListAdapter = new UserListAdapter(context, 0, searchList);
        searchListView.setAdapter(searchListAdapter);
        searchListView.setSelector(R.drawable.list_selector);
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String userID = searchList.get(position);

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
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
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
                        ((Activity)context).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                searchListAdapter.notifyDataSetChanged();
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
        });
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button_search && searchText.getText() != null){
            InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String string = searchText.getText().toString();
                    Server.searchUser(searchList, string);

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
