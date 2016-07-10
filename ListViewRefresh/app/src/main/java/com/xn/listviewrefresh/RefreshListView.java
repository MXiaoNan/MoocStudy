package com.xn.listviewrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by XiaoNan on 2016/7/3.
 */
public class RefreshListView extends ListView implements AbsListView.OnScrollListener {

    private View mHeadview;
    private int mHeadHeight;
    private int firstVisibleItem;
    private boolean isRemark;
    private int startY;

    int state;
    final int NONE = 0;
    final int PULL = 1;
    final int RELESE = 2;
    final int REFLASHING = 3;
    private int scrollState;

    IReflashListener reflashListener;

    public RefreshListView(Context context) {
        super(context);
        initView(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 初始化界面，添加顶部布局文件到ListView里面
     *
     * @param context
     */
    private void initView(Context context) {
        mHeadview = LayoutInflater.from(context).inflate(R.layout.layout_head, null);
        measureView(mHeadview);
        mHeadHeight = mHeadview.getMeasuredHeight();
        Log.d("print", "mHeadHeight = " + mHeadHeight);
        setTopPadding(-mHeadHeight);
        this.addHeaderView(mHeadview);
        this.setOnScrollListener(this);
    }

    /**
     * 通知父布局所占用的宽高
     *
     * @param view
     */
    private void measureView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        //参数：当前左右的边距，内边距，子布局的宽度
        int width = ViewGroup.getChildMeasureSpec(0, 0, params.width);
        int height;
        int tempHeight = params.height;
        if (tempHeight > 0) {
            //参数：填充高度的值，模式
            height = MeasureSpec.makeMeasureSpec(tempHeight, MeasureSpec.EXACTLY);
        } else {
            height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        mHeadview.measure(width, height);
    }

    /**
     * 设置顶部布局的上边距
     */

    private void setTopPadding(int topPadding) {
        mHeadview.setPadding(mHeadview.getPaddingLeft(),
                topPadding,
                mHeadview.getPaddingRight(),
                mHeadview.getPaddingBottom());
        mHeadview.invalidate();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        this.scrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (firstVisibleItem == 0) {
                    isRemark = true;
                    startY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onMove(ev);
                break;
            case MotionEvent.ACTION_UP:
                if (state == RELESE) {
                    state = REFLASHING;
                    reflashViewByState();
                    reflashListener.onReflash();
                } else if (state == PULL) {
                    state = NONE;
                    isRemark = false;
                    reflashViewByState();
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void onMove(MotionEvent ev) {
        if (!isRemark) {
            return;
        }
        int tempY = (int) ev.getY();
        int spance = tempY - startY;
        int topPadding = spance - mHeadHeight;
        switch (state) {
            case NONE:
                if (spance > 0) {
                    state = PULL;
                    reflashViewByState();
                }
                break;
            case PULL:
                setTopPadding(topPadding);
                if (spance > mHeadHeight + 30 && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    state = RELESE;
                    reflashViewByState();
                }
                break;
            case RELESE:
                setTopPadding(topPadding);
                if (spance < mHeadHeight + 30) {
                    state = PULL;
                    reflashViewByState();
                } else if (spance <= 0) {
                    state = NONE;
                    isRemark = false;
                    reflashViewByState();
                }
                break;
            case REFLASHING:

                break;
        }
    }

    private void reflashViewByState() {
        TextView text = (TextView) mHeadview.findViewById(R.id.tv_head_text);
        ImageView pic = (ImageView) mHeadview.findViewById(R.id.iv_head_pic);
        ProgressBar pro = (ProgressBar) mHeadview.findViewById(R.id.pb_head_pro);
        RotateAnimation animation1 = new RotateAnimation(0,
                180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation1.setDuration(500);
        animation1.setFillAfter(true);

        RotateAnimation animation2 = new RotateAnimation(180,
                0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation2.setDuration(500);
        animation2.setFillAfter(true);

        switch (state) {
            case NONE:
                setTopPadding(-mHeadHeight);
                break;
            case PULL:
                pic.clearAnimation();
                pic.setVisibility(View.VISIBLE);
                pro.setVisibility(GONE);
                text.setText("下拉可以刷新");
                pic.clearAnimation();
                pic.setAnimation(animation2);
                break;
            case RELESE:
                pic.setVisibility(View.VISIBLE);
                pro.setVisibility(GONE);
                text.setText("松开可以刷新");
                pic.clearAnimation();
                pic.setAnimation(animation1);
                break;
            case REFLASHING:
                setTopPadding(50);
                pic.setVisibility(View.GONE);
                pro.setVisibility(VISIBLE);
                text.setText("正在刷新");
                pic.clearAnimation();
                break;
        }
    }

    public void reflashComplete() {
        state = NONE;
        isRemark = false;
        reflashViewByState();
        TextView last_time = (TextView) mHeadview.findViewById(R.id.tv_head_time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = dateFormat.format(date);
        last_time.setText(time);
    }

    public void setInterface(IReflashListener reflashListener) {
        this.reflashListener = reflashListener;
    }

    public interface IReflashListener {
        public void onReflash();
    }
}
