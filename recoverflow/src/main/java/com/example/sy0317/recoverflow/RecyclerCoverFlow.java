package com.example.sy0317.recoverflow;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/10/27.
 */

public class RecyclerCoverFlow extends RecyclerView {
    /**
     * 按下的X轴坐标
     *
     * @param context
     */
    private float mDownX;

    /**
     * 布局器构建者
     *
     * @param context
     */
    private CoverFlowLayoutManger.Builder mManagerBuilder;

    public RecyclerCoverFlow(Context context) {
        super(context);
        init();
    }
    public RecyclerCoverFlow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerCoverFlow(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init() {
        createManagerBuilder();
        setLayoutManager(mManagerBuilder.build());
        setChildrenDrawingOrderEnabled(true);//开启重新排序
        setOverScrollMode(OVER_SCROLL_NEVER);

    }

    /**
     * 创建布局构建器
     */
    private void createManagerBuilder() {
        if (mManagerBuilder == null) {
            mManagerBuilder = new CoverFlowLayoutManger.Builder();
        }
    }

    /**
     * 设置是否为普通平面滚动
     * true:平面滚动 false: 叠加缩放滚动
     */
    public void setFlatFlow(boolean isFlat){
        createManagerBuilder();
        mManagerBuilder.setFlat(isFlat);
        setLayoutManager(mManagerBuilder.build());
    }

    /**
     * 设置Item灰度渐变
     * true:Item灰度渐变 false: Items灰度不变
     */

    public void setGreyItem(boolean greyItem){
        createManagerBuilder();
        mManagerBuilder.setGreyItem(greyItem);
        setLayoutManager(mManagerBuilder.build());
    }

    /**
     * 设置Item透明渐变
     * true:设置Item半透明渐变 false: Item透明度不变
     */

    public void setAlphaItem(boolean alphaItem){
        createManagerBuilder();
        mManagerBuilder.setAlphaItem(alphaItem);
        setLayoutManager(mManagerBuilder.build());
    }


    /**
     * 设置Item的间隔比例：item的宽x intervalRatio
     */
    public void setIntervalRatio(float intervalRatio){
        createManagerBuilder();
        mManagerBuilder.setIntervalRatio(intervalRatio);
        setLayoutManager(mManagerBuilder.build());
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        if (!(layout instanceof CoverFlowLayoutManger)) {
            throw new IllegalArgumentException("The layout manager must be CoverFlowLayoutManger");
        }
        super.setLayoutManager(layout);
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        int center = getCoverFlowLayout().getCenterPosition()
                - getCoverFlowLayout().getFirstVisiblePosition(); //计算正在显示的所有Item的中间位置
        if (center < 0) center = 0;
        else if (center > childCount) center = childCount;
        int order;
        if (i == center) {
            order = childCount - 1;
        } else if (i > center) {
            order = center + childCount - 1 - i;
        } else {
            order = i;
        }
        return order;
    }
    /**
     * 获取LayoutManger，并强制转换为CoverFlowLayoutManger
     */
    public CoverFlowLayoutManger getCoverFlowLayout() {
        return ((CoverFlowLayoutManger)getLayoutManager());
    }
    /**
     * 设置选中的Item位置
     */

    public int getSelectedPos() {
        return getCoverFlowLayout().getSelectedPos();
    }

    /**
     * 设置选中监听
     * @param l 监听接口
     */
    public void setOnItemSelectedListener(CoverFlowLayoutManger.OnSelected l) {
        getCoverFlowLayout().setOnSelectedListener(l);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                getParent().requestDisallowInterceptTouchEvent(true); //设置父类不拦截滑动事件
                break;
            case MotionEvent.ACTION_MOVE:
                if ((ev.getX() > mDownX && getCoverFlowLayout().getCenterPosition() == 0) ||
                        (ev.getX() < mDownX && getCoverFlowLayout().getCenterPosition() ==
                                getCoverFlowLayout().getItemCount() -1)) {
                    //如果是滑动到了最前和最后，开放父类滑动事件拦截
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    //滑动到中间，设置父类不拦截滑动事件
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}

