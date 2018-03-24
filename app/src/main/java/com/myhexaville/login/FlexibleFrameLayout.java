package com.myhexaville.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

public class FlexibleFrameLayout extends FrameLayout {
    private static final String TAG = "OrderLayout";
    public static int ORDER_SIGN_UP_STATE = 0;
    public static int ORDER_LOGIN_STATE = 1;

    private static final int[][] DRAW_ORDERS = new int[][]{
            {0, 1, 2},
            {2, 1, 0}
    };

    private int currentOrder;

    public FlexibleFrameLayout(@NonNull Context context) {
        super(context);
        setChildrenDrawingOrderEnabled(true);
    }

    public FlexibleFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
    }

    public FlexibleFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setChildrenDrawingOrderEnabled(true);
    }

    public FlexibleFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setChildrenDrawingOrderEnabled(true);
    }

    public void setDrawOrder(int order) {
        currentOrder = order;
        invalidate();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return DRAW_ORDERS[currentOrder][i];
    }
}