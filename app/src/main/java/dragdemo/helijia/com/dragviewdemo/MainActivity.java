package dragdemo.helijia.com.dragviewdemo;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {


    private RelativeLayout rl_root;
    private int xDelta;
    private int yDelta;
    private int initX, initY;
    private static final int MAXINTANCE_CLICK = 6;//点击事件最大的触发距离
    private int screenWidth, screenHeight;
    private int theDragViewHeight, theDragViewWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl_root = findViewById(R.id.rl_root);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        final ImageView imgView = findViewById(R.id.imgView);
        imgView.setOnTouchListener(this);
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "onClick", Toast.LENGTH_SHORT).show();
                Log.e(MainActivity.class.getSimpleName(), "onclick");
            }
        });

        ViewTreeObserver viewTreeObserver = imgView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imgView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                theDragViewHeight = imgView.getHeight();
                theDragViewWidth = imgView.getWidth();
                Log.e(MainActivity.class.getSimpleName(), "dragView width:" + theDragViewWidth + ",dragView height:" + theDragViewHeight);
            }
        });
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {

        final int x = (int) event.getRawX();
        final int y = (int) event.getRawY();

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams)
                        view.getLayoutParams();
                xDelta = x - lParams.leftMargin;
                yDelta = y - lParams.topMargin;
                initX = x;
                initY = y;
                break;

            case MotionEvent.ACTION_UP:
                Log.e(MainActivity.class.getSimpleName(),
                        "action up");
                double distance = Math.sqrt(Math.pow((x - initX), 2) + Math.pow((y - initY), 2));
                if (distance > MAXINTANCE_CLICK) {
                    return true;
                } else {
                    return false;
                }
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                layoutParams.leftMargin = x - xDelta;
                layoutParams.topMargin = y - yDelta;

                if (layoutParams.leftMargin < 0) {
                    layoutParams.leftMargin = 0;
                }

                if (layoutParams.topMargin < 0) {
                    layoutParams.topMargin = 0;
                }

                if (layoutParams.leftMargin > screenWidth - theDragViewWidth) {
                    layoutParams.leftMargin = screenWidth - theDragViewWidth;
                }

                if (layoutParams.topMargin > screenHeight - theDragViewHeight) {
//                    layoutParams.topMargin = screenHeight - theDragViewHeight;
                }
                view.setLayoutParams(layoutParams);
                rl_root.invalidate();
                break;
        }
        return false;
    }

}
