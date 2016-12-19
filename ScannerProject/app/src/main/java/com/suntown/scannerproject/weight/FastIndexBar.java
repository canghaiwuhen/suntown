package com.suntown.scannerproject.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * 快速索引
 */
public class FastIndexBar extends View {

    private Paint paint;
    private int cellWidth;
    private int cellHeight;

    public FastIndexBar(Context context) {
        super(context);
        init();
    }

    public FastIndexBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FastIndexBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private static final String[] LETTERS = new String[]{
            "A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X",
            "Y", "Z"};

    private void init() {
        //创建画笔，并去掉锯齿
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setTextSize(18f);
        paint.setFakeBoldText(true);
    }
    public interface OnLetterChangeListener{
        void letterChanged(String letter);
    }
    private OnLetterChangeListener onLetterChangeListener;

    public void setOnLetterChangeListener(OnLetterChangeListener onLetterChangeListener) {
        this.onLetterChangeListener = onLetterChangeListener;
    }

    //绘制字母
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawText("A", 5f, 20f, paint);
        for (int i = 0; i < LETTERS.length; i++) {
            //计算文字的宽高
            Rect rect=new Rect();
            paint.getTextBounds(LETTERS[i], 0, 1, rect);
            //得到文字的宽高
            int textWidth=rect.width();
            int textHeight=rect.height();
            //计算绘制的开始位置
            float x=cellWidth*0.5f-textWidth*0.5f;
            float y=cellHeight*0.5f+textHeight*0.5f+cellHeight*i;
            paint.setColor(chooseIndex==i?Color.GRAY:Color.WHITE);
            canvas.drawText(LETTERS[i],x,y,paint);
        }

    }
    //通过测量获取单元格的宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //单元格的宽高
        cellWidth = getMeasuredWidth();
        cellHeight = getMeasuredHeight()/26;
    }

    private int chooseIndex=-1;
    //处理触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float y=0f;
        int currentIndex=0;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                y = event.getY();
                currentIndex = (int) (y/cellHeight);
                if (currentIndex>LETTERS.length-1){
                    currentIndex=LETTERS.length-1;
                }
                if (onLetterChangeListener!=null){
                    onLetterChangeListener.letterChanged(LETTERS[currentIndex]);
                }
//                Utils.showTaost(getContext(),LETTERS[currentIndex]);
                chooseIndex=currentIndex;
                break;
            case MotionEvent.ACTION_MOVE:
                y = event.getY();
                currentIndex= (int) (y/cellHeight);
                if (currentIndex>LETTERS.length-1){
                    currentIndex=LETTERS.length-1;
                }
                if (onLetterChangeListener!=null){
                    onLetterChangeListener.letterChanged(LETTERS[currentIndex]);
                }
//                Utils.showTaost(getContext(), LETTERS[currentIndex]);
                chooseIndex=currentIndex;
                break;
            case MotionEvent.ACTION_UP:
                chooseIndex=-1;
                break;
        }
        //重绘
        invalidate();
        return true;
    }
}
