package com.suntowns.labeltest.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2016/8/17.
 */
public class SwipeLayout extends FrameLayout {

    private ViewDragHelper viewDragHelper;
    private ViewGroup menuView;
    private ViewGroup mainView;
    private int mWidth;
    private int mHeight;
    private int maxDragRange;
    private GestureDetector gestureDetector;

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    //定义状态
    public enum DragState{
        OPEN,DRAGGING,CLOSE
    }
    private DragState currentState= DragState.CLOSE;//初始化状态
    private DragState preState= DragState.CLOSE;//保存上一次状态
    //定义接口
    public interface OnSwipeLayoutChangedListener{
        public void onOpen(SwipeLayout swipeLayout);
        public void onClose(SwipeLayout swipeLayout);
    }
    private OnSwipeLayoutChangedListener onSwipeLayoutChangedListener;
    public void setOnSwipeLayoutChangedListener(OnSwipeLayoutChangedListener onSwipeLayoutChangedListener) {
        this.onSwipeLayoutChangedListener = onSwipeLayoutChangedListener;
    }
    private void init() {
        viewDragHelper = ViewDragHelper.create(this, callback);
        gestureDetector = new GestureDetector(getContext(),simpleOnGestureListener);
    }
    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener=new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //当水平滑动比垂直滑动的距离大，就请求父亲不要拦截事件
            if (Math.abs(distanceX)> Math.abs(distanceY)){
                requestDisallowInterceptTouchEvent(true);
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    };
    private ViewDragHelper.Callback callback=new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            //都能被捕获
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child==mainView){
                if (left<-maxDragRange){
                    left=-maxDragRange;
                }else if (left>0){
                    left=0;
                }
            }else{
                //拖拽的是菜单
                if (left<mWidth-maxDragRange){
                    left=mWidth-maxDragRange;
                }else if (left>mWidth){
                    left=mWidth;
                }
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if (changedView==mainView){
                menuView.offsetLeftAndRight(dx);
            }else{
                //拖动的是菜单
                mainView.offsetLeftAndRight(dx);
            }
            //执行接口回调
            executeListener(mainView.getLeft());
            invalidate();
        }
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (xvel==0&&mainView.getLeft()<-maxDragRange*0.5f){
                open();
            }else if (xvel<0){
                open();
            }else{
                close(true);
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return maxDragRange;
        }
    };
    //执行接口回调的方法
    private void executeListener(int left) {
        //更新之前保存上一次状态
        preState=currentState;
        //更新当前状态
        currentState=updateState(left);
        //接口回调
        if (onSwipeLayoutChangedListener!=null && preState!=currentState){
            if (currentState== DragState.OPEN){
                onSwipeLayoutChangedListener.onOpen(this);
            }else if (currentState== DragState.CLOSE){
                onSwipeLayoutChangedListener.onClose(this);
            }
        }
    }
    ///更新当前状态的方法
    private DragState updateState(int left) {
        if (left==-maxDragRange){
            return DragState.OPEN;
        }else if (left==0){
            return DragState.CLOSE;
        }
        return DragState.DRAGGING;
    }

    //关闭菜单
    public void close(boolean isSmooth) {
        if (isSmooth){
            if (viewDragHelper.smoothSlideViewTo(mainView,0,0)){
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }else{
            //强行摆放到关闭状态  ,这种方式并没有正在的关闭条目，正在的关闭条目要调用onSwipeLayoutChangedListener.onClose(this)方法
            mainView.layout(0,0,mWidth,mHeight);
            menuView.layout(mWidth,0,mWidth+maxDragRange,mHeight);
            //手动的更改当前状态，调用接口的方法关闭菜单
            currentState= DragState.CLOSE;
            if (onSwipeLayoutChangedListener!=null){
                onSwipeLayoutChangedListener.onClose(this);
            }
        }
    }
    //打开菜单
    private void open() {
        if (viewDragHelper.smoothSlideViewTo(mainView,-maxDragRange,0)){
            //发起动画
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    //初始化控件的位置
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        //初始化控件的位置
        mainView.layout(0,0,mWidth,mHeight);
        menuView.layout(mWidth,0,mWidth+maxDragRange,mHeight);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        //菜单的宽度
        maxDragRange = menuView.getMeasuredWidth();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //健壮性判断
        if (getChildCount()!=2){
            throw new IllegalStateException("you must have only two children");
        }
        if (!(getChildAt(0) instanceof ViewGroup)||!(getChildAt(1) instanceof ViewGroup)){
            throw new IllegalArgumentException("your child must be instance of ViewGroup");
        }
        //找到主界面和菜单
        menuView = (ViewGroup) getChildAt(0);
        mainView = (ViewGroup) getChildAt(1);
    }
}
