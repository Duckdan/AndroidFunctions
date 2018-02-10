package study.com.androidfunctions.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import study.com.androidfunctions.R;
import study.com.androidfunctions.utils.DisplayNativeUtils;

/**
 * 指示器
 */
public class IndicatorView extends View {
    private final int DEFAULT_IV_TEXT_SIZE = 12;
    private final int DEFAULT_IV_NORMAL_TEXT_COLOR = Color.parseColor("#444444");
    private final int DEFAULT_IV_CHOOSE_TEXT_COLOR = Color.parseColor("#3399ff");

    private final int DEFAULT_POPUP_WINDOW_TEXT_SIZE = 40;
    private final int DEFAULT_POPUP_WINDOW_COLOR = Color.GRAY;
    private final int DEFAULT_POPUP_WINDOW_TEXT_COLOR = Color.parseColor("#990488f0");

    private OnItemClickListener mOnItemClickListener;
    String[] b = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z"};
    int choose = -1;
    Paint paint = new Paint();
    boolean showBkg = false;
    private PopupWindow mPopupWindow;
    private TextView mPopupText;

    private Handler handler = new Handler();
    //指示器的文本大小
    private int ivTextSize;
    //指示器的普通文本颜色
    private int ivNormalTextColor;
    //指示器的选择文本颜色
    private int ivChooseTextColor;
    //弹窗文本的大小
    private int ivPopupWindowTextSize;
    //弹窗的背景色
    private int ivPopupWindowColor;
    //弹窗文本颜色
    private int ivPopupWindowTextColor;

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        ivTextSize = typedArray.getInteger(R.styleable.IndicatorView_iv_text_size, DEFAULT_IV_TEXT_SIZE);
        ivNormalTextColor = typedArray.getColor(R.styleable.IndicatorView_iv_normal_text_color, DEFAULT_IV_NORMAL_TEXT_COLOR);
        ivChooseTextColor = typedArray.getColor(R.styleable.IndicatorView_iv_normal_text_color, DEFAULT_IV_CHOOSE_TEXT_COLOR);

        ivPopupWindowTextSize = typedArray.getInteger(R.styleable.IndicatorView_iv_popup_window_text_size, DEFAULT_POPUP_WINDOW_TEXT_SIZE);
        ivPopupWindowColor = typedArray.getInteger(R.styleable.IndicatorView_iv_popup_window_color, DEFAULT_POPUP_WINDOW_COLOR);
        ivPopupWindowTextColor = typedArray.getInteger(R.styleable.IndicatorView_iv_popup_window_text_color, DEFAULT_POPUP_WINDOW_TEXT_COLOR);

        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = DisplayNativeUtils.dp2px(getContext(), ivTextSize + 10);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBkg) {
            canvas.drawColor(Color.parseColor("#00000000"));
        }
        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / b.length;
        for (int i = 0; i < b.length; i++) {
            paint.setColor(ivNormalTextColor);
            int size = DisplayNativeUtils.dp2px(getContext(), ivTextSize);
            paint.setTextSize(size);
            paint.setFakeBoldText(true);
            paint.setAntiAlias(true);
            if (i == choose) {
                paint.setColor(ivChooseTextColor);
            }
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final int c = (int) (y / getHeight() * b.length);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBkg = true;
                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        performItemClicked(c);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        performItemClicked(c);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBkg = false;
                choose = -1;
                dismissPopup();
                invalidate();
                break;
        }
        return true;
    }

    private void showPopup(int item) {
        if (mPopupWindow == null) {
            handler.removeCallbacks(dismissRunnable);
            mPopupText = new TextView(getContext());
            mPopupText.setBackgroundColor(ivPopupWindowTextColor);
            mPopupText.setTextColor(ivPopupWindowColor);
            mPopupText.setTextSize(ivPopupWindowTextSize);
            mPopupText.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            int width = DisplayNativeUtils.dp2px(getContext(), ivPopupWindowTextSize * 2);
            mPopupWindow = new PopupWindow(mPopupText, width, width);
        }
        String text = "";
        if (item == 0) {
            text = "#";
        } else {
            text = Character.toString((char) ('A' + item - 1));
        }
        mPopupText.setText(text);
        if (!mPopupWindow.isShowing()) {
            mPopupWindow.showAtLocation(getRootView(),
                    Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        }
    }

    private void dismissPopup() {
        handler.postDelayed(dismissRunnable, 400);
    }

    Runnable dismissRunnable = new Runnable() {
        @Override
        public void run() {
            if (mPopupWindow != null) {
                mPopupWindow.dismiss();
            }
        }
    };




    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private void performItemClicked(int item) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(b[item]);
            showPopup(item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String s);
    }

}