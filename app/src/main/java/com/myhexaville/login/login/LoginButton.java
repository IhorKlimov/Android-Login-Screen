package com.myhexaville.login.login;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.myhexaville.login.R;

import static android.graphics.Paint.Style.FILL;
import static java.lang.Math.PI;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.tan;

public class LoginButton extends View {
    public static final String LOG_TAG = "LoginButton";
    private int width, height;

    private int buttonTop, buttonBottom;

    private Paint paint;
    private Path path;

    private int currentRectangleRight;
    private float currentY;
    private int buttonCenter;
    private float currentX, currentRight;
    private float currentBottomY;
    private float currentBottomX;
    private int currentArcY;
    private float currentArcX;
    private Paint paint2;
    private Rect r = new Rect();
    private Paint loginPaint;
    private Paint orPaint;
    private Paint signUpPaint;
    private float currentLoginX;
    private float startLoginX;
    private float largeTextSize;
    private float smallTextSize;
    private float currentLoginY;
    private float startLoginY;

    public LoginButton(Context context) {
        super(context);
        init();
    }

    public LoginButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoginButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LoginButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        buttonTop = height - getBottomMargin() - getButtonHeight();
        buttonBottom = height - getBottomMargin();
        currentRectangleRight = (int) getStartButtonRight();

        buttonCenter = (buttonBottom - buttonTop) / 2 + buttonTop;

        currentY = buttonCenter;
        currentBottomY = buttonBottom;
        currentRight = currentRectangleRight;

        currentLoginX = dpToPixels(92);
        currentLoginY = buttonCenter + dpToPixels(8);
        largeTextSize = dpToPixels(64);
        smallTextSize = dpToPixels(16);

        startLoginX = currentLoginX;
        startLoginY = currentLoginY;

        path.moveTo(0, buttonBottom);
        path.lineTo(currentRight, buttonBottom);
        path.lineTo(currentRight, buttonTop);
        path.lineTo(0, buttonTop);
        path.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText("SIGN UP", width / 2, 1600, signUpPaint);

        canvas.drawPath(path, paint);

        canvas.drawArc(
                currentRight - getButtonHeight() / 2 + currentArcX,
                buttonTop,
                currentRight + getButtonHeight() / 2 - currentArcX,
                buttonBottom,
                0,
                360,
                false,
                paint);


        canvas.drawText("OR", dpToPixels(32), buttonCenter + dpToPixels(8), orPaint);

        canvas.drawText("LOGIN", currentLoginX, currentLoginY, loginPaint);
    }

    private void init() {
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        paint.setStyle(FILL);

        paint2 = new Paint();
        paint2.setColor(Color.parseColor("#ffffff"));
        paint2.setStyle(FILL);
        path = new Path();

        loginPaint = new Paint();
        loginPaint.setColor(ContextCompat.getColor(getContext(), R.color.text));
        loginPaint.setTextAlign(Paint.Align.CENTER);
        loginPaint.setTextSize(dpToPixels(16));

        orPaint = new Paint();
        orPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_two));
        orPaint.setTextSize(dpToPixels(16));

        signUpPaint = new Paint();
        signUpPaint.setColor(ContextCompat.getColor(getContext(), R.color.text));
        signUpPaint.setTextSize(dpToPixels(64));
//        signUpPaint.setTypeface(Typeface.create(DEFAULT, BOLD));
        signUpPaint.setTextAlign(Paint.Align.CENTER);
        signUpPaint.setAlpha(125);
    }

    private int getButtonHeight() {
        return getResources().getDimensionPixelOffset(R.dimen.bottom_height);
    }

    private int getBottomMargin() {
        return getResources().getDimensionPixelOffset(R.dimen.bottom_margin);
    }

    public void startAnimation() {
        float start = getStartButtonRight();
        ValueAnimator animator = ObjectAnimator.ofFloat(0f, 1f);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(animation -> {
            float fraction = (float) animation.getAnimatedValue();
            float currentAngle = fraction * ((float) PI / 2); // in radians

            float gone = (width - start) * fraction;
            currentRight = start + gone;


            // fade out sign up text to 0
            signUpPaint.setAlpha((int) (125 - 125 * fraction));

            if (orPaint.getAlpha() != 0) {
                orPaint.setAlpha(0);
            }

            // move login text to center and scale
            currentLoginX = startLoginX + ((width / 2 - startLoginX) * fraction);
            currentLoginY = startLoginY - ((startLoginY - 1600) * fraction);
            loginPaint.setTextSize(smallTextSize + ((largeTextSize - smallTextSize) * (fraction)));

            currentArcY = (int) (fraction * dpToPixels(28)); // just hardcoded value
            currentArcX = (int) (fraction * dpToPixels(37)); // just hardcoded value

            double y = tan(currentAngle) * currentRight; // goes from ~ 0 to 4451
            float realY = (float) (buttonTop - y); // goes ~ from 1234 to -1243
            currentY = max(0, realY); // goes ~ from 1234 to 0


            float realBottomY = (float) (buttonBottom + y);
            currentBottomY = min(height, realBottomY);


            if (currentY == 0) { // if reached top, start moving to the right
                double cot = 1.0f / tan(currentAngle);
                currentX = (float) ((y - buttonTop) * cot);
            }

            if (currentBottomY == height) {
                double cot = 1.0f / tan(currentAngle);
                currentBottomX = (float) ((y - getBottomMargin()) * cot);
            }

            if (currentAngle == (float) PI / 2) {
                currentX = currentRight;
                currentBottomX = currentRight;
                currentY = 0;
                currentBottomY = height;
            }

            path.reset();
            path.moveTo(0, buttonBottom);
            path.lineTo(currentRight, buttonBottom);
            path.lineTo(currentRight, buttonTop);

            path.lineTo(currentX, currentY);
            path.lineTo(0, currentY);

            // bottom reveal
            path.lineTo(0, currentBottomY);
            path.lineTo(currentBottomX, currentBottomY);
            path.lineTo(currentRight, buttonBottom);


            currentX = 0;
            currentBottomX = 0;
            invalidate();
        });

        animator.start();
    }

    private float getStartButtonRight() {
        return getResources().getDimensionPixelOffset(R.dimen.bottom_width);
    }

    private float dpToPixels(int dp) {
        return getResources().getDisplayMetrics().density * dp;
    }

}
