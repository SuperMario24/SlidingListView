package com.example.saber.slidingitem;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

import static android.content.ContentValues.TAG;

/**
 * Created by saber on 2017/7/25.
 */

public class SlidingListView extends ListView{

    private int mScreenWidth;   // 屏幕宽度
    private int mDownX;         // 按下点的x值
    private int mDownY;         // 按下点的y值
    private int mDeleteBtnWidth;// 删除按钮的宽度

    private boolean isDeleteShown;  // 删除按钮是否正在显示
    private int currentItem = -1;//触发ACTION_DOWN时的item
    private int openItem = -1;//正在打开的item

    private ViewGroup mPointChild;  // 当前处理的item
    private LinearLayout.LayoutParams mLayoutParams;    // 当前处理的item的LayoutParams

    public SlidingListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        // 获取屏幕宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) e.getX();
                mDownY = (int) e.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int mMoveX = (int) e.getX();
                int mMoveY = (int) e.getY();

                //当y方向滑动距离大于x方向时，拦截事件，交给自己处理
                if(Math.abs(mMoveX - mDownX) < Math.abs(mMoveY - mDownY)){
                    Log.d(TAG, "onInterceptTouchEvent");
                    return true;
                }
                mDownX = mMoveX;
                mDownY = mMoveY;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                performActionDown(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                return performActionMove(ev);
            case MotionEvent.ACTION_UP:
                performActionUp();
                break;
        }

        return super.onTouchEvent(ev);
    }

    // 处理action_down事件
    private void performActionDown(MotionEvent ev) {
        //获取当前item的index
        currentItem = pointToPosition(mDownX, mDownY)- getFirstVisiblePosition();
        if(isDeleteShown && openItem != currentItem) {
            turnToNormal();
        }

        mDownX = (int) ev.getX();
        mDownY = (int) ev.getY();
        // 获取当前点的item
        mPointChild = (ViewGroup) getChildAt(currentItem);
        // 获取删除按钮的宽度
        mDeleteBtnWidth = mPointChild.getChildAt(1).getLayoutParams().width;
        mLayoutParams = (LinearLayout.LayoutParams) mPointChild.getChildAt(0)
                .getLayoutParams();
        mLayoutParams.width = mScreenWidth;
        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
    }

    // 处理action_move事件
    private boolean performActionMove(MotionEvent ev) {
        int nowX = (int) ev.getX();
        int nowY = (int) ev.getY();
        if(Math.abs(nowX - mDownX) > Math.abs(nowY - mDownY)) {
            // 如果向左滑动
            if(nowX < mDownX && mLayoutParams.leftMargin != -mDeleteBtnWidth*2) {
                // 计算要偏移的距离
                int scroll = (nowX - mDownX) / 2;
                // 如果大于了删除按钮的宽度， 则最大为删除按钮的宽度
                if(-scroll >= mDeleteBtnWidth) {
                    scroll = -mDeleteBtnWidth;
                }
                // 重新设置leftMargin
                //TODO 平缓滑动
                mLayoutParams.leftMargin = scroll*2;
                Log.d(TAG," mLayoutParams.leftMargin---left:"+ mLayoutParams.leftMargin);
                mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);

                //记录已经打开的item
                openItem = currentItem;
            }else if(nowX >= mDownX && mLayoutParams.leftMargin >= -mDeleteBtnWidth*2 && mLayoutParams.leftMargin <0 ){//向右滑动
                // 计算要偏移的距离
                int scroll = (nowX - mDownX) / 2;
                // 如果大于了删除按钮的宽度， 则最大为删除按钮的宽度
                if(scroll >= mDeleteBtnWidth) {
                    scroll = mDeleteBtnWidth;
                }
                // 重新设置leftMargin
                //TODO 平缓滑动
                mLayoutParams.leftMargin = -mDeleteBtnWidth*2 + scroll*2;
                Log.d(TAG," mLayoutParams.leftMargin---right:"+ mLayoutParams.leftMargin);
                mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    // 处理action_up事件
    private void performActionUp() {
        // 偏移量大于button的一半，则显示button
        // 否则恢复默认
        if(-mLayoutParams.leftMargin >= mDeleteBtnWidth) {
            mLayoutParams.leftMargin = -mDeleteBtnWidth*2;
            isDeleteShown = true;
            openItem = currentItem;
        }else {
            turnToNormal();
            openItem = -1;
        }

        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
    }
    /**
     * 变为正常状态
     */
    public void turnToNormal() {

        mLayoutParams.leftMargin = 0;
        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
        isDeleteShown = false;
    }
    /**
     * 当前是否可点击
     * @return 是否可点击
     */
    public boolean canClick() {
        return !isDeleteShown;
    }

}
