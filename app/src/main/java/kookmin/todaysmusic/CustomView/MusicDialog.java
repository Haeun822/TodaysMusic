package kookmin.todaysmusic.CustomView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import kookmin.todaysmusic.Data.Music;
import kookmin.todaysmusic.Data.User;
import kookmin.todaysmusic.R;
import kookmin.todaysmusic.Utils.Font;
import kookmin.todaysmusic.Utils.Server;
import kookmin.todaysmusic.Utils.Time;

public class MusicDialog extends Dialog implements View.OnClickListener {

    public Music music;
    ImageView musicImage;
    TextView title, artist, user;
    ToggleButton toggle;
    Rating ratingView;
    TextView remove, listen, confirm;

    public MusicDialog(Context context, Music music){
        super(context);
        this.music = music;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_star);

        Font.changeFont(findViewById(R.id.dialog_layout));

        musicImage = (ImageView)findViewById(R.id.item_music_image);
        title = (TextView)findViewById(R.id.titleText);
        artist = (TextView)findViewById(R.id.artistText);
        user = (TextView)findViewById(R.id.userText);
        toggle = (ToggleButton)findViewById(R.id.toggleButton);
        ratingView = (Rating)findViewById(R.id.ratingBar);
        remove = (TextView)findViewById(R.id.button_remove);
        confirm = (TextView)findViewById(R.id.button_confirm);
        listen = (TextView)findViewById(R.id.button_listen);

        musicImage.setImageBitmap(music.thumb);
        title.setText(music.Title);
        artist.setText(music.Artist);
        if(music.UserID != null && !music.UserID.isEmpty() && !music.UserID.equals(User.ID)) {
            user.setText(music.UserID + "님이 같이 듣고 싶어합니다.");
            user.setVisibility(View.VISIBLE);
        }
        else if(music.UserID == null || music.UserID.isEmpty()){

        }
        else{
            toggle.setChecked(music.isShared);
            toggle.setVisibility(View.VISIBLE);
            remove.setVisibility(View.VISIBLE);
        }
        ratingView.setCount(music.Star);

        toggle.setOnClickListener(this);
        remove.setOnClickListener(this);
        confirm.setOnClickListener(this);
        listen.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        if(v == toggle){
            if(music.isShared == true){
                music.isShared = false;
                toggle.setChecked(false);
                for(int i=0; i<User.timeLine.size(); i++){
                    if(User.timeLine.get(i).MusicID.equals(music.MusicID))
                        User.timeLine.remove(i);
                }
            }
            else{
                music.isShared = true;
                toggle.setChecked(true);

                music.SharedTime = Time.getCurrentTime();
                User.timeLine.add(0, music);
            }

            Server.registerMusicList(music);
        }
        else if(v == remove){
            Server.deleteMusicList(music);
            for(int i=0; i<User.staredList.size(); i++){
                if(User.staredList.get(i).MusicID.equals(music.MusicID))
                    User.staredList.remove(i);
            }
            for(int i=0; i<User.timeLine.size(); i++){
                Music timeline = User.timeLine.get(i);
                if(timeline.UserID == User.ID && timeline.MusicID.equals(music.MusicID))
                    User.timeLine.remove(i);
            }
            dismiss();
        }
        else if(v == confirm){
            int counter = ratingView.getCount();
            if(counter != music.Star){
                music.Star = counter;
                Server.registerMusicList(music);
                if(!User.staredList.contains(music)) {
                    music.SharedTime = Time.getCurrentTime();
                    music.UserID = User.ID;
                    User.staredList.add(0, music);
                }
            }

            dismiss();
        }
        else if(v == listen){

        }
    }
}
