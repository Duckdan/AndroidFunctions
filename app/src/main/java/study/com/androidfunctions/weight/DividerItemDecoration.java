package study.com.androidfunctions.weight;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import study.com.androidfunctions.R;

/**
 * Rv线型布局的分割线
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private static final int defaultDivider;
    public static final int HORIZONTAL_LIST = 0;
    public static final int VERTICAL_LIST = 1;
    public Drawable mDivider;
    public int mOrientation;
    public boolean bottomDivider = false;

    public DividerItemDecoration(Context context, int orientation) {
        this.setDefaultDivider(context);
        this.setOrientation(orientation);
    }

    public DividerItemDecoration(Activity activity, int orientation, int drawableId) {
        this.setDivider(activity, drawableId);
        this.setOrientation(orientation);
    }

    private void setDivider(Activity activity, int drawableId) {
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                this.mDivider = activity.getTheme().getDrawable(drawableId);
            } else {
                this.mDivider = activity.getResources().getDrawable(drawableId);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        if (this.mDivider == null) {
            this.setDefaultDivider(activity);
        }

    }

    private void setDefaultDivider(Context context) {
        this.mDivider = context.getResources().getDrawable(defaultDivider);
    }

    public void setOrientation(int orientation) {
        if (orientation != 0 && orientation != 1) {
            throw new IllegalArgumentException("invalid orientation");
        } else {
            this.mOrientation = orientation;
        }
    }

    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (this.mOrientation == 1) {
            this.drawVertical(c, parent);
        } else {
            this.drawHorizontal(c, parent);
        }

    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        int drawCount = childCount;
        if (!this.bottomDivider) {
            drawCount = childCount - 1;
        }

        for (int i = 0; i <= drawCount; ++i) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
            int bottom = top + this.mDivider.getIntrinsicHeight();
            this.mDivider.setBounds(left, top, right, bottom);
            this.mDivider.draw(c);
        }

    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        int childCount = parent.getChildCount();
        int drawCount = childCount;
        if (!this.bottomDivider) {
            drawCount = childCount - 1;
        }

        for (int i = 0; i <= drawCount; ++i) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int left = child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));
            int right = left + this.mDivider.getIntrinsicHeight();
            this.mDivider.setBounds(left, top, right, bottom);
            this.mDivider.draw(c);
        }

    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (this.mOrientation == 1) {
            outRect.set(0, 0, 0, this.mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, this.mDivider.getIntrinsicWidth(), 0);
        }

    }

    static {
        defaultDivider = R.drawable.divider_item_shape;
    }
}

