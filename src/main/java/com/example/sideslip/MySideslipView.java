package com.example.sideslip;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * 自定义侧滑
 */
public class MySideslipView extends HorizontalScrollView{

    //父容器
    private LinearLayout mLayout;
    //menu
    private ViewGroup menu;
    //内容
    private ViewGroup content;
    //屏幕宽度
    private int mScreenWidth;
    //menu右边距
    private int mMenuRightPadding = 50;
    //menu宽度
    private int mMenuWidth;

    private boolean flag = true;
    private boolean isOpen = false;

    public MySideslipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        WindowManager wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;


        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MySideslipView, defStyleAttr, 0);
        for (int i = 0; i < a.getIndexCount(); i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.MySideslipView_rightPadding:
                    int defaule = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,context.getResources().getDisplayMetrics());
                    mMenuRightPadding = a.getDimensionPixelSize(attr,defaule);
                    break;
            }
        }
        a.recycle();

    }

    public MySideslipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MySideslipView(Context context) {
        this(context, null, 0);
    }

    //测量自己的宽高和子view宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(flag){
            flag = false;
            mLayout = (LinearLayout) getChildAt(0);

            menu = (ViewGroup) mLayout.getChildAt(0);
            content = (ViewGroup) mLayout.getChildAt(1);

            mMenuWidth = mScreenWidth - mMenuRightPadding;
            menu.getLayoutParams().width = mMenuWidth;
            content.getLayoutParams().width = mScreenWidth;

        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //指定怎么显示布局
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed){
            scrollTo(mMenuWidth,0);
        }
    }

    //通过设置偏移量隐藏menu

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                int scrollx = getScrollX();
                if(scrollx > mMenuWidth / 2){
                    smoothScrollTo(mMenuWidth,0);
                    isOpen = false;
                }else{
                    smoothScrollTo(0,0);
                    isOpen = true;
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    //打开菜单
    public void openMenu() {
        if (!isOpen){
            smoothScrollTo(0, 0);
            isOpen = true;
        }
    }

    //关闭菜单
    public void closeMenu() {
        if (isOpen) {
            this.smoothScrollTo(mMenuWidth, 0);
            isOpen = false;
        }
    }

    //切换菜单
    public void change() {
        if (isOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    //缩放和渐变效果
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l * 1.0f / mMenuWidth;
        float leftScale = 1 - 0.3f * scale;
        float rightScale = 0.8f + scale * 0.2f;

        ViewHelper.setScaleX(menu, leftScale);
        ViewHelper.setScaleY(menu, leftScale);
        ViewHelper.setAlpha(menu, 0.6f + 0.4f * (1 - scale));
        ViewHelper.setTranslationX(menu, mMenuWidth * scale * 0.6f);

        ViewHelper.setPivotX(content, 0);
        ViewHelper.setPivotY(content, content.getHeight() / 2);
        ViewHelper.setScaleX(content, rightScale);
        ViewHelper.setScaleY(content, rightScale);

    }
}
