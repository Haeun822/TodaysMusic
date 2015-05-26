package kookmin.todaysmusic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import kookmin.todaysmusic.Utils.Font;
import kookmin.todaysmusic.Utils.Server;

public class LoadingActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Font.createFont(this);
        Font.changeFont(findViewById(R.id.layout_loading));

    }

    public void registerUser(String ID, String PW){
        if(Server.register(ID, PW)) {
            findViewById(R.id.row_id).setVisibility(View.INVISIBLE);
            findViewById(R.id.row_pw).setVisibility(View.INVISIBLE);
            findViewById(R.id.row_buttons).setVisibility(View.INVISIBLE);
            findViewById(R.id.Label_Loading).setVisibility(View.VISIBLE);

            User.ID = ID;
            User.registerMusicInDevice(getApplicationContext());

            goMainActivity();
        }
    }

    public void login(String ID, String PW){
        if(Server.login(ID, PW)){
            findViewById(R.id.row_id).setVisibility(View.INVISIBLE);
            findViewById(R.id.row_pw).setVisibility(View.INVISIBLE);
            findViewById(R.id.row_buttons).setVisibility(View.INVISIBLE);
            findViewById(R.id.Label_Loading).setVisibility(View.VISIBLE);

            User.ID = ID;
            Server.dataLoading(this);
        }
        else{
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.setMessage("로그인에 실패했습니다.");
            alert.show();
        }
    }

    public void goMainActivity(){
        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClick(View view){
        int id = view.getId();
        final String ID = ((TextView)findViewById(R.id.editID)).getText().toString();
        final String PW = ((TextView)findViewById(R.id.editPW)).getText().toString();

        if(id == R.id.Button_Login){
            if(ID.isEmpty() || PW.isEmpty()){
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.setMessage("아이디와 비밀번호를 입력해 주세요.");
                alert.show();
            }
            else{
                login(ID, PW);
            }
        }
        else if(id == R.id.Button_Register){
            if(ID.isEmpty() || PW.isEmpty()){
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.setMessage("등록할 아이디와 비밀번호를 입력해 주세요.");
                alert.show();
            }
            else{
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                if(Server.checkDuplicateID(ID) == true) {
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("이미 등록된 아이디입니다.");
                    alert.show();
                }
                else {
                    alert.setMessage("[" + ID + "]로 등록합니다").setCancelable(false);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            registerUser(ID, PW);
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
        }
    }
}
