package kookmin.todaysmusic;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import kookmin.todaysmusic.Utils.Font;


public class MainActivity extends Activity implements OnGestureListener {

    GestureDetector mGesture;
    ViewFlipper mViewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Font.changeFont(findViewById(R.id.viewFlipper));

        mGesture = new GestureDetector(this, this);

        mViewFlipper = (ViewFlipper)findViewById(R.id.viewFlipper);
        mViewFlipper.setDisplayedChild(1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View v){
        int id = v.getId();

        if(id == R.id.button_recommend)
            MovePreviousView();
        else if(id == R.id.button_timeline)
            MoveNextView();
    }

    private void MoveNextView() {
        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.appear_from_right));
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.disappear_to_left));
        mViewFlipper.showNext();
    }

    private void MovePreviousView() {
        mViewFlipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.appear_from_left));
        mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.disappear_to_right));
        mViewFlipper.showPrevious();
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
        return true;
    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }
    @Override
    public void onLongPress(MotionEvent e) {

    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e1.getX() < e2.getX() && mViewFlipper.getDisplayedChild() != 0)
            MovePreviousView();
        else if(e1.getX() > e2.getX() && mViewFlipper.getDisplayedChild() != 2)
            MoveNextView();
        return true;
    }
}
