package com.application.cool.history.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.application.cool.history.R;

/**
 * Create by Zhenyuan Shen on 5/13/18.
 */

public class SideIndexBar extends View {

    private OnTouchListener onTouchListener;

    // 26个字母
    public static String[] index = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

//    public static String[] index = { "☆", "#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
//            "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    private int choose = -1;// 选中
    private Paint paint = new Paint();

    private TextView mTextDialog;

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public SideIndexBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SideIndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideIndexBar(Context context) {
        super(context);
    }


    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        int singleHeight = (height - 32) / index.length;


        for (int i = 0; i < index.length; i++) {
            if (!isInEditMode()) {
                paint.setColor(ContextCompat.getColor(getContext(), R.color.historyGreen));
            }
            paint.setAntiAlias(true);
            paint.setTextSize(20);

            if (i == choose) {
                paint.setColor(ContextCompat.getColor(getContext(), R.color.historyDarkBrown));
                paint.setFakeBoldText(true);
            }

            float xPos = width / 2 - paint.measureText(index[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(index[i], xPos, yPos, paint);
            paint.reset();
        }

    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final OnTouchListener listener = onTouchListener;
        final int c = (int) (y / getHeight() * index.length);

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackground(new ColorDrawable(getResources().getColor(R.color.white)));
                choose = -1;
                mTextDialog.setVisibility(View.INVISIBLE);
                invalidate();
                break;

            default:
                if (oldChoose != c) {
                    if (c >= 0 && c < index.length) {
                        if (onTouchListener != null) {
                            onTouchListener.onTouchChanged(index[c]);
                        }

                        if (mTextDialog != null) {
                            mTextDialog.setText(index[c]);
                            mTextDialog.setVisibility(View.INVISIBLE); // VISIBLE
                        }

                        choose = c;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }


    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }


    public interface OnTouchListener {

        void onTouchChanged(String s);
    }

}