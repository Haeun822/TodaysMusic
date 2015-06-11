package kookmin.todaysmusic;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.lang.reflect.Field;

import kookmin.todaysmusic.CustomView.MusicDialog;
import kookmin.todaysmusic.CustomView.SearchDialog;
import kookmin.todaysmusic.Data.Music;
import kookmin.todaysmusic.Data.User;
import kookmin.todaysmusic.Utils.Font;
import kookmin.todaysmusic.Utils.MusicListAdapter;
import kookmin.todaysmusic.Utils.Server;


public class MainActivity extends Activity implements OnGestureListener {

    MenuItem addButton;
    MenuItem listButton;
    MenuItem followButton;

    GestureDetector mGesture;
    ViewFlipper mViewFlipper;
    int viewChild = 1;

    View mainView, recommendView, timeLineView;

    ListView recommendList;
    MusicListAdapter recommendAdapter;
    ListView timeLineList;
    MusicListAdapter timeLineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Font.changeFont(findViewById(R.id.viewFlipper));

        mViewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        mViewFlipper.setDisplayedChild(viewChild);
        mainView = mViewFlipper.getChildAt(1);
        recommendView = mViewFlipper.getChildAt(0);
        timeLineView = mViewFlipper.getChildAt(2);

        mGesture = new GestureDetector(this, this);

        recommendList = (ListView)recommendView.findViewById(R.id.list_recommend);
        recommendList.setSelector(R.drawable.list_selector);
        recommendAdapter = new MusicListAdapter(this, 0, User.recommendList, Music.SERVER_REC);
        recommendList.setAdapter(recommendAdapter);
        recommendList.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return mGesture.onTouchEvent(event);
            }
        });

        timeLineList = (ListView)timeLineView.findViewById(R.id.list_timeline);
        timeLineList.setSelector(R.drawable.list_selector);
        timeLineAdapter = new MusicListAdapter(this, 0, User.timeLine, Music.USER_REC);
        timeLineList.setAdapter(timeLineAdapter);
        timeLineList.setOnTouchListener(new View.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event){
                return mGesture.onTouchEvent(event);
            }
        });

        setMainPreview();

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class
                    .getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMainPreview(){
        int recommendSize = User.recommendList.size();
        int timeLineSize = User.timeLine.size();
        View v;
        Music m;
        TextView t;
        ImageView i;

        if(recommendSize > 0){
            v = mainView.findViewById(R.id.main_recommend_big);
            v.setVisibility(View.VISIBLE);
            m = User.recommendList.get(0);

            i = (ImageView)v.findViewById(R.id.user_image);
            i.setImageBitmap(m.thumb);

            t = (TextView)v.findViewById(R.id.titleText);
            t.setTypeface(Font.font);
            t.setText(m.Title);

            t = (TextView)v.findViewById(R.id.artistText);
            t.setTypeface(Font.font);
            t.setText(m.Artist);

            if(recommendSize > 1){
                v = mainView.findViewById(R.id.main_recommend_small);
                v.setVisibility(View.VISIBLE);
                m = User.recommendList.get(1);

                i = (ImageView)v.findViewById(R.id.user_image);
                i.setImageBitmap(m.thumb);

                t = (TextView)v.findViewById(R.id.titleText);
                t.setTypeface(Font.font);
                t.setText(m.Title + " - " + m.Artist);
            }
        }

        if(timeLineSize > 0){
            v = mainView.findViewById(R.id.main_timeline1);
            m = User.timeLine.get(0);

            setTimeLinePreview(v, m);

            if(timeLineSize > 1){
                v = mainView.findViewById(R.id.main_timeline2);
                m = User.timeLine.get(1);

                setTimeLinePreview(v, m);
            }
            if(timeLineSize > 2){
                v = mainView.findViewById(R.id.main_timeline3);
                m = User.timeLine.get(2);

                setTimeLinePreview(v, m);
            }
        }
    }

    public void setTimeLinePreview(View v, Music m){
        ImageView i;
        TextView t;

        v.setVisibility(View.VISIBLE);

        i = (ImageView)v.findViewById(R.id.user_image);
        i.setImageBitmap(m.thumb);

        if(m.Star != -1) {
            i = (ImageView) v.findViewById(R.id.starImage);
            i.setImageBitmap(MusicListAdapter.bitmap_star);
            t = (TextView) v.findViewById(R.id.starCount);
            t.setTypeface(Font.font);
            t.setText(m.Star + "");
            t.setVisibility(View.VISIBLE);
        }

        t = (TextView)v.findViewById(R.id.titleText);
        t.setTypeface(Font.font);
        t.setText(m.Title + " - " + m.Artist);
        t = (TextView)v.findViewById(R.id.timeText);
        t.setTypeface(Font.font);
        t.setText(m.SharedTime);
        t = (TextView)v.findViewById(R.id.userText);
        t.setTypeface(Font.font);
        t.setText(m.UserID+"님이 같이 듣고 싶어합니다.");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        addButton = menu.findItem(R.id.action_add);
        addButton.setVisible(false);
        listButton = menu.findItem(R.id.action_list);
        followButton = menu.findItem(R.id.action_follow);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Server.getRecommend();
                    Server.getTimeLine();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recommendAdapter.notifyDataSetChanged();
                            timeLineAdapter.notifyDataSetChanged();
                            setMainPreview();
                        }
                    });
                }
            }).start();
        }
        else if (id == R.id.action_list){
            Intent intent = new Intent(MainActivity.this, StaredListActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.action_follow){
            Intent intent = new Intent(MainActivity.this, FollowActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.action_add){
            SearchDialog dialog = new SearchDialog(this);
            dialog.show();
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    refresh();
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v){
        int id = v.getId();

        if(id == R.id.button_recommend)
            MovePreviousView();
        else if(id == R.id.button_timeline)
            MoveNextView();
        else if(id == R.id.myProfile){
            Intent intent = new Intent(MainActivity.this, StaredListActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.main_recommend_big){
            MusicDialog dialog = new MusicDialog(this, User.recommendList.get(0));
            dialog.show();
        }
        else if(id == R.id.main_recommend_small){
            MusicDialog dialog = new MusicDialog(this, User.recommendList.get(1));
            dialog.show();
        }
        else if(id == R.id.main_timeline1){
            MusicDialog dialog = new MusicDialog(this, User.timeLine.get(0));
            dialog.show();
        }
        else if(id == R.id.main_timeline2){
            MusicDialog dialog = new MusicDialog(this, User.timeLine.get(1));
            dialog.show();
        }
        else if(id == R.id.main_timeline3){
            MusicDialog dialog = new MusicDialog(this, User.timeLine.get(2));
            dialog.show();
        }
    }

    private void MoveNextView() {
        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.appear_from_right));
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.disappear_to_left));
        mViewFlipper.showNext();
        viewChild = mViewFlipper.getDisplayedChild();
        if(viewChild == 2) {
            addButton.setVisible(true);
            listButton.setVisible(false);
        }
        followButton.setVisible(true);
    }

    private void MovePreviousView() {
        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.appear_from_left));
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.disappear_to_right));
        mViewFlipper.showPrevious();
        viewChild = mViewFlipper.getDisplayedChild();
        if(viewChild == 0)
            followButton.setVisible(false);
        addButton.setVisible(false);
        listButton.setVisible(true);
    }

    public void refresh(){
        timeLineAdapter.notifyDataSetChanged();
        setMainPreview();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGesture.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if(mViewFlipper.getDisplayedChild() == 0){
            int position = recommendList.pointToPosition((int)e.getX(), (int)e.getY());
            if(User.recommendList.size() > position && position != -1) {
                MusicDialog dialog = new MusicDialog(this, User.recommendList.get(position));
                dialog.show();
            }
        }
        else if(mViewFlipper.getDisplayedChild() == 2){
            int position = timeLineList.pointToPosition((int)e.getX(), (int)e.getY());
            if(User.timeLine.size() > position && position != -1) {
                MusicDialog dialog = new MusicDialog(this, User.timeLine.get(position));
                dialog.show();
            }
        }
        return false;
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(mViewFlipper.getDisplayedChild() == 0){
            recommendList.smoothScrollByOffset((int)distanceY);
        }
        else if(mViewFlipper.getDisplayedChild() == 2){
            timeLineList.smoothScrollByOffset((int)distanceY);
        }
        return true;
    }
    @Override
    public void onLongPress(MotionEvent e) {

    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(velocityX > 2000 && mViewFlipper.getDisplayedChild() != 0)
            MovePreviousView();
        else if(velocityX < -2000 && mViewFlipper.getDisplayedChild() != 2)
            MoveNextView();
        return true;
    }
}
