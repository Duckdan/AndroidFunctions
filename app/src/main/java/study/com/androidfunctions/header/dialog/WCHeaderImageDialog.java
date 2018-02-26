package study.com.androidfunctions.header.dialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import study.com.androidfunctions.R;


/**
 * 与微信匹配的底部弹出对话框
 */

public class WCHeaderImageDialog {

    private final Context context;
    private int themeResId = R.style.dialog;
    private final Activity activity;
    private OnResultListener listener;
    private Dialog dialog;

    /**
     * @param context    上下文实例
     * @param themeResId 对话框的弹出风格，传0是默认从底部弹出对话框
     * @param activity   对话框弹出的宿主Acitivity实例
     */
    public WCHeaderImageDialog(Context context, int themeResId, Activity activity) {
        this.context = context;
        if (themeResId != 0) {
            this.themeResId = themeResId;
        }
        this.activity = activity;

        initView();
    }

    private void initView() {

        View inflate = View.inflate(context, R.layout.dialog_header_wc_layout, null);
        dialog = new Dialog(activity, themeResId);

        dialog.setContentView(inflate);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = window.getWindowManager().getDefaultDisplay().getWidth();
        window.setAttributes(layoutParams);
        dialog.findViewById(R.id.tv_photo_album).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.sexFlag(0);
                }
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.sexFlag(1);
                }
                dialog.dismiss();

            }
        });


    }


    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }


    public void setOnResultUriListener(OnResultListener listener) {
        this.listener = listener;
    }

    public interface OnResultListener {
        /**
         * type为0时代表从相册中选择
         * type为1时代表保存到手机
         *
         * @param type
         */
        void sexFlag(int type);
    }
}
