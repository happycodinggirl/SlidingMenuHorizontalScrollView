package com.test.huangxingli.slidingmenuhorizontalscrollview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.test.huangxingli.slidingmenuhorizontalscrollview.R;

/**
 * Created by huangxingli on 2014/12/16.
 */
public class SlidingHorizontalScrollView extends HorizontalScrollView {

    int menuWidth;
    LinearLayout mWrapper;
    ViewGroup menuLayout, contentLayout;
    int rightMargin=50;
    int screenWidth;
  //  Scroller scroller;
    boolean isOpen;

    public SlidingHorizontalScrollView(Context context) {
        super(context);
        init(context);
    }

    public SlidingHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu);
        rightMargin= (int) typedArray.getDimension(R.styleable.SlidingMenu_rightPadding,50);
    }

    public SlidingHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public void init(Context context){
        rightMargin= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,rightMargin,getResources().getDisplayMetrics());
        WindowManager windowManager= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm=new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        screenWidth=dm.widthPixels;

       // scroller=new Scroller(context);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWrapper= (LinearLayout) getChildAt(0);
        menuLayout= (ViewGroup) mWrapper.getChildAt(0);
        contentLayout= (ViewGroup) mWrapper.getChildAt(1);
        //Log.v("")
        ViewGroup.LayoutParams menuLayoutLayoutParams=menuLayout.getLayoutParams();
        ViewGroup.LayoutParams contentLayoutParams=contentLayout.getLayoutParams();
        menuWidth=menuLayoutLayoutParams.width=screenWidth-rightMargin;
        contentLayoutParams.width=screenWidth;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed){
            scrollTo(menuWidth,0);
        }
    }
//注释掉的为另一种实现方法，体验稍差。
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        int action=ev.getAction();
        switch (action){
            case MotionEvent.ACTION_UP:
                int scrollX=getScrollX();
                Log.v("TAG","===SCROLLY is "+scrollX);
                if (scrollX>menuWidth/2){
                    //关闭slidingMenu
                    this.smoothScrollTo(menuWidth,0);
                    isOpen=false;
                   // startmoveAnimation(getScrollX(),menuWidth-getScrollY(),1000);
                    Log.v("TAG","====SCROLLY > menuWidth/2 关闭slidingMenu");
                    //postInvalidate();
                }else{
                    //打开SlidingMenu
                    this.smoothScrollTo(0,0);
                    isOpen=true;
                 //   startmoveAnimation(getScrollX(),getScrollY()-menuWidth,1000);
                  //  postInvalidate();
                    Log.v("TAG","===SCROLLY <MENUwIDTH/2 打开slidingMenu");

                }
                Log.v("TAG","------ACTION_UP");
                //注意这个地方要返回true,否则达不到效果
                return true;
        }
        return super.onTouchEvent(ev);
    }

    /*@Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            postInvalidate();
        }

    }

    public void startmoveAnimation(int x,int dx,int duration){
        scroller.startScroll(x,0,dx,0,1000);
        invalidate();
    }*/

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //这一句的作用是实现抽屉式布局，感觉像是一直在内容区域下面，把上面的拉走就显示出来下面的了。
        menuLayout.setTranslationX(l);
        //若还要有缩放 、透明度等的变化则在此处进行处理
        //缩放可使用setScaleX,setScaleY等方法，但要注意缩放的中心点，
        //设置缩放中心点的方法为setPivotX,setPivotY.
    }

    public void closeMenu(){

            this.smoothScrollTo(menuWidth,0);

    }
    public void openMenu(){
        this.smoothScrollTo(0,0);
    }
    public void toggle(){
        if (isOpen){
            closeMenu();
            isOpen=false;
        }else{
            openMenu();
            isOpen=true;
        }
    }
}
