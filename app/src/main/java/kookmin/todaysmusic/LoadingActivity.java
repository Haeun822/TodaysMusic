package kookmin.todaysmusic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import kookmin.todaysmusic.Utils.Font;

public class LoadingActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Font.createFont(this);
        Font.changeFont(findViewById(R.id.layout_loading));

    }

    public void onClick(View view){
        int id = view.getId();
        String ID = ((TextView)findViewById(R.id.editID)).getText().toString();
        String PW = ((TextView)findViewById(R.id.editPW)).getText().toString();

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
                //TODO Login progress
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
                alert.setMessage("[" + ID + "]로 등록합니다").setCancelable(false);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO Register progress
                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }
        }
    }
}
