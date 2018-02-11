package com.myhexaville.login.login;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.myhexaville.login.OnButtonSwitchedListener;
import com.myhexaville.login.R;

import static android.graphics.Paint.Align.CENTER;
import static android.graphics.Paint.Style.FILL;
import static android.view.MotionEvent.ACTION_UP;
import static java.lang.Math.PI;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.tan;

public class LoginButton extends View {
    public static final String TAG = "LoginButton";
    private int width, height;

    private int buttonTop, buttonBottom;

    private Paint loginButtonPaint;
    private Paint signUpButtonPaint;

    private Path loginButtonPath = new Path();
    private Path signUpButtonPath = new Path();

    private Rect r = new Rect();

    private int startRight;
    private float currentY;
    private int buttonCenter;
    private float currentX, currentRight;
    private float currentBottomY;
    private float currentBottomX;
    private int currentArcY;
    private float currentArcX;

    private Paint paint2;
    private Paint loginPaint;
    private Paint orPaint;
    private Paint signUpPaint;

    private float currentLoginX;
    private float currentSignUpTextX;
    private float largeTextSize;
    private float smallTextSize;
    private float currentLoginY;
    private float currentLeft;
    private float signUpOrX;
    private boolean isLogin = true;
    private float currentSignUpTextY;
    private float currentSignUpX;
    private float currentBottomSignUpX;
    private int startLeft;

    private OnButtonSwitchedListener callback;

    private float startSignUpTextX;
    private float startSignUpTextY;
    private float startLoginX;
    private float startLoginY;
    private float loginOrX;
    private Rect loginButtonOutline;
    private Rect signUpButtonOutline;

    private OnSignUpListener onSignUpListener;
    private OnLoginListener onLoginListener;
    private Rect loginTextOutline;
    private Rect signUpTextOutline;


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

    private void init() {
        loginButtonPaint = new Paint();
        loginButtonPaint.setColor(ContextCompat.getColor(getContext(), R.color.secondPage));
        loginButtonPaint.setStyle(FILL);

        signUpButtonPaint = new Paint();
        signUpButtonPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        signUpButtonPaint.setStyle(FILL);

        paint2 = new Paint();
        paint2.setColor(Color.parseColor("#ffffff"));
        paint2.setStyle(FILL);

        loginPaint = new Paint();
        loginPaint.setColor(ContextCompat.getColor(getContext(), R.color.text));
        loginPaint.setTextAlign(CENTER);
        loginPaint.setTextSize(dpToPixels(16));

        orPaint = new Paint();
        orPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_two));
        orPaint.setTextSize(dpToPixels(16));

        signUpPaint = new Paint();
        signUpPaint.setColor(ContextCompat.getColor(getContext(), R.color.text));
        signUpPaint.setTextSize(dpToPixels(64));
        signUpPaint.setTextAlign(CENTER);
//        signUpPaint.setAlpha(255);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        buttonTop = height - getBottomMargin() - getButtonHeight();
        buttonBottom = height - getBottomMargin();
        startRight = (int) getStartButtonRight();

        buttonCenter = (buttonBottom - buttonTop) / 2 + buttonTop;

        currentSignUpX = width;
        currentBottomSignUpX = width;

        loginOrX = dpToPixels(32);

        currentY = buttonCenter;
        currentBottomY = buttonBottom;
        currentRight = startRight;
        currentLeft = width - startRight;
        startLeft = width - startRight;

        loginPaint.getTextBounds(getString(R.string.sign_up), 0, 7, r);

        currentLoginX = dpToPixels(92);
        int signUpWidth = r.right;
        currentSignUpTextX = width - signUpWidth / 2 - dpToPixels(32);

        loginPaint.getTextBounds(getString(R.string.login), 0, 5, r);

        loginTextOutline = new Rect();
        signUpTextOutline = new Rect();
        signUpPaint.getTextBounds(getString(R.string.login), 0, 5, loginTextOutline);
        signUpPaint.getTextBounds(getString(R.string.sign_up), 0, 7, signUpTextOutline);

        loginTextOutline.offset(width / 2 - (loginTextOutline.right + loginTextOutline.left) / 2, (int) dpToPixels(457));
        signUpTextOutline.offset(width / 2 - (signUpTextOutline.right + signUpTextOutline.left) / 2, (int) dpToPixels(457));

        int loginWidth = r.right;
        orPaint.getTextBounds(getContext().getString(R.string.or).toUpperCase(), 0, 2, r);
        float margin = (currentLoginX - loginWidth / 2) - dpToPixels(32) - r.right;
        signUpOrX = width - signUpWidth - dpToPixels(32) - r.right - margin;

        currentLoginY = buttonCenter + dpToPixels(8);
        currentSignUpTextY = buttonCenter + dpToPixels(8);
        largeTextSize = dpToPixels(64);
        smallTextSize = dpToPixels(16);

        startLoginX = currentLoginX;
        startLoginY = currentLoginY;
        startSignUpTextX = currentSignUpTextX;
        startSignUpTextY = currentSignUpTextY;

        loginButtonPath.moveTo(0, buttonBottom);
        loginButtonPath.lineTo(currentRight, buttonBottom);
        loginButtonPath.lineTo(currentRight, buttonTop);
        loginButtonPath.lineTo(0, buttonTop);
        loginButtonPath.close();

        signUpButtonPath.moveTo(width, buttonBottom);
        signUpButtonPath.lineTo(currentLeft, buttonBottom);
        signUpButtonPath.lineTo(currentLeft, buttonTop);
        signUpButtonPath.lineTo(width, buttonTop);
        signUpButtonPath.close();

        loginButtonOutline = new Rect(
                0,
                buttonTop,
                (int) currentRight + getButtonHeight() / 2,
                buttonBottom);

        signUpButtonOutline = new Rect(
                (int) (width - currentRight - getButtonHeight() / 2),
                buttonTop,
                width,
                buttonBottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isLogin) {
            canvas.drawText(getString(R.string.sign_up), width / 2, dpToPixels(457), signUpPaint);
        } else {
            canvas.drawText(getString(R.string.login), width / 2, dpToPixels(457), loginPaint);
        }

        if (isLogin) {
            canvas.drawPath(loginButtonPath, loginButtonPaint);
            canvas.drawArc(
                    currentRight - getButtonHeight() / 2 + currentArcX,
                    buttonTop,
                    currentRight + getButtonHeight() / 2 - currentArcX,
                    buttonBottom,
                    0,
                    360,
                    false,
                    loginButtonPaint);

            canvas.drawText(getString(R.string.or), loginOrX, buttonCenter + dpToPixels(8), orPaint);
            canvas.drawText(getString(R.string.login), currentLoginX, currentLoginY, loginPaint);
        } else {
            canvas.drawPath(signUpButtonPath, signUpButtonPaint);
            canvas.drawArc(
                    currentLeft - getButtonHeight() / 2 + currentArcX,
                    buttonTop,
                    currentLeft + getButtonHeight() / 2 - currentArcX,
                    buttonBottom,
                    0,
                    360,
                    false,
                    signUpButtonPaint);

            canvas.drawText(getString(R.string.or), signUpOrX, buttonCenter + dpToPixels(8), orPaint);

            canvas.drawText(getString(R.string.sign_up), currentSignUpTextX, currentSignUpTextY, signUpPaint);
        }
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
//        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(animation -> {
            float fraction = (float) animation.getAnimatedValue();
            float currentAngle = fraction * ((float) PI / 2); // in radians

            float gone = (width - start) * fraction;
            currentRight = start + gone;
            currentLeft = startLeft - gone;

            // fade out sign up text to 0
            if (isLogin) {
                signUpPaint.setAlpha((int) (255 - 255 * fraction)); // fade out sign up large text
            } else {
                loginPaint.setAlpha((int) (255 - 255 * fraction)); // fade out login large text
            }

            if (orPaint.getAlpha() != 0) {
                orPaint.setAlpha(0);
            }

            // move login text to center and scale
            if (isLogin) {
                currentLoginX = startLoginX + ((width / 2 - startLoginX) * fraction);
                currentLoginY = startLoginY - ((startLoginY - dpToPixels(457)) * fraction);
                loginPaint.setTextSize(smallTextSize + ((largeTextSize - smallTextSize) * (fraction)));
            } else {
                currentSignUpTextX = startSignUpTextX - ((startSignUpTextX - width / 2) * fraction);
                currentSignUpTextY = startSignUpTextY - ((startSignUpTextY - dpToPixels(457)) * fraction);
                signUpPaint.setTextSize(smallTextSize + ((largeTextSize - smallTextSize) * (fraction)));
            }

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
                currentSignUpX = width - currentX;
            }

            if (currentBottomY == height) {
                double cot = 1.0f / tan(currentAngle);
                currentBottomX = (float) ((y - getBottomMargin()) * cot);
                currentBottomSignUpX = width - currentBottomX;
            }

            if (currentAngle == (float) PI / 2) {
                currentX = currentRight;
                currentBottomX = currentRight;
                currentY = 0;
                currentBottomY = height;
            }

            if (isLogin) {
                loginButtonPath.reset();
                loginButtonPath.moveTo(0, buttonBottom);
                loginButtonPath.lineTo(currentRight, buttonBottom);
                loginButtonPath.lineTo(currentRight, buttonTop);

                loginButtonPath.lineTo(currentX, currentY);
                loginButtonPath.lineTo(0, currentY);

                // bottom reveal
                loginButtonPath.lineTo(0, currentBottomY);
                loginButtonPath.lineTo(currentBottomX, currentBottomY);
                loginButtonPath.lineTo(currentRight, buttonBottom);
            } else {
                signUpButtonPath.reset();
                signUpButtonPath.moveTo(width, buttonBottom);
                signUpButtonPath.lineTo(currentLeft, buttonBottom);
                signUpButtonPath.lineTo(currentLeft, buttonTop);

                signUpButtonPath.lineTo(currentSignUpX, currentY);
                signUpButtonPath.lineTo(width, currentY);

                // bopttom reveal
                signUpButtonPath.lineTo(width, currentBottomY);
                signUpButtonPath.lineTo(currentBottomSignUpX, currentBottomY);
                signUpButtonPath.lineTo(currentLeft, buttonBottom);
            }

            currentX = 0;
            currentSignUpX = width;
            currentBottomX = 0;
            currentBottomSignUpX = width;
            invalidate();
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                orPaint.setAlpha(125);
                signUpPaint.setAlpha(255);
                signUpPaint.setTextSize(dpToPixels(16));
                currentArcX = 0;
                currentArcY = 0;

                currentRight = (int) getStartButtonRight();
                currentLeft = width - (int) getStartButtonRight();

                isLogin = !isLogin;

                if (isLogin) {
                    currentLoginX = startLoginX;
                    currentLoginY = startLoginY;
                    loginPaint.setAlpha(255);
                    loginPaint.setTextSize(dpToPixels(16));
                    signUpPaint.setAlpha(255);
                    signUpPaint.setTextSize(dpToPixels(64));
                }

                currentSignUpTextX = startSignUpTextX;
                currentSignUpTextY = startSignUpTextY;

                int hideButton = startRight + getButtonHeight() / 2;
                if (!isLogin) {
                    currentLeft += hideButton;
                } else {
                    currentRight -= hideButton;
                }


                //move texts
                if (!isLogin) {
                    signUpOrX += hideButton;
                    currentSignUpTextX += hideButton;
                } else {
                    loginOrX -= hideButton;
                    currentLoginX -= hideButton;
                }
                float hiddenButtonLeft = currentLeft;
                float hiddenButtonRight = currentRight;
                float endSignUpOrX = signUpOrX;
                float endSignUpTextX = currentSignUpTextX;
                float endLoginOrX = loginOrX;
                float endLoginTextX = currentLoginX;

                // reset paths
                signUpButtonPath.reset();
                signUpButtonPath.moveTo(width, buttonBottom);
                signUpButtonPath.lineTo(currentLeft, buttonBottom);
                signUpButtonPath.lineTo(currentLeft, buttonTop);
                signUpButtonPath.lineTo(width, buttonTop);
                signUpButtonPath.close();

                loginButtonPath.reset();
                loginButtonPath.moveTo(0, buttonBottom);
                loginButtonPath.lineTo(currentRight, buttonBottom);
                loginButtonPath.lineTo(currentRight, buttonTop);
                loginButtonPath.lineTo(0, buttonTop);
                loginButtonPath.close();

                callback.onButtonSwitched(isLogin);

                ValueAnimator buttonBounce = ObjectAnimator.ofInt(0, hideButton).setDuration(500);
                buttonBounce.setStartDelay(300);
                buttonBounce.setInterpolator(new MyBounceInterpolator(.2, 7));
                buttonBounce.addUpdateListener(a -> {
                    int v = (int) a.getAnimatedValue();

                    if (!isLogin) {
                        currentLeft = hiddenButtonLeft - v;

                        signUpOrX = endSignUpOrX - v;
                        currentSignUpTextX = endSignUpTextX - v;

                        signUpButtonPath.reset();
                        signUpButtonPath.moveTo(width, buttonBottom);
                        signUpButtonPath.lineTo(currentLeft, buttonBottom);
                        signUpButtonPath.lineTo(currentLeft, buttonTop);
                        signUpButtonPath.lineTo(width, buttonTop);
                        signUpButtonPath.close();
                    } else {
                        currentRight = hiddenButtonRight + v;

                        loginOrX = endLoginOrX + v;
                        currentLoginX = endLoginTextX + v;

                        loginButtonPath.reset();
                        loginButtonPath.moveTo(0, buttonBottom);
                        loginButtonPath.lineTo(currentRight, buttonBottom);
                        loginButtonPath.lineTo(currentRight, buttonTop);
                        loginButtonPath.lineTo(0, buttonTop);
                        loginButtonPath.close();
                    }
                    invalidate();
                });
                buttonBounce.start();

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        animator.start();
    }

    private float getStartButtonRight() {
        return getResources().getDimensionPixelOffset(R.dimen.bottom_width);
    }

    private float dpToPixels(int dp) {
        return getResources().getDisplayMetrics().density * dp;
    }

    public void setOnButtonSwitched(OnButtonSwitchedListener callback) {
        this.callback = callback;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        setOnTouchListener((v, event) -> {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (isLogin && loginButtonOutline.contains(x, y)) {
                if (event.getAction() == ACTION_UP) {
                    l.onClick(v);
                }
                return true;
            } else if (!isLogin && loginTextOutline.contains(x, y)) {
                if (event.getAction() == ACTION_UP) {
                    onLoginListener.login();
                }
                return true;
            } else if (isLogin && signUpTextOutline.contains(x, y)) {
                if (event.getAction() == ACTION_UP) {
                    onSignUpListener.signUp();
                }
                return true;
            } else {
                if (!isLogin && signUpButtonOutline.contains(x, y)) {
                    if (event.getAction() == ACTION_UP) {
                        l.onClick(v);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public void setOnSignUpListener(OnSignUpListener listener) {
        onSignUpListener = listener;
    }

    public void setOnLoginListener(OnLoginListener listener) {
        onLoginListener = listener;
    }

    private String getString(@StringRes int stringId) {
        return getContext().getString(stringId).toUpperCase();
    }

}
