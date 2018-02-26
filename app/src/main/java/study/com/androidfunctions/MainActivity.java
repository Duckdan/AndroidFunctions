package study.com.androidfunctions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import study.com.androidfunctions.contact.MainContactActivity;
import study.com.androidfunctions.header.MainHeaderActivity;
import study.com.androidfunctions.qr_code.MainCodeActivity;
import study.com.androidfunctions.refresh.MainRefreshActivity;
import study.com.androidfunctions.suspend.MainSuspendActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv1 = (TextView) findViewById(R.id.tv_1);
        tv1.setOnClickListener(this);
        TextView tv2 = (TextView) findViewById(R.id.tv_2);
        tv2.setOnClickListener(this);
        TextView tv3 = (TextView) findViewById(R.id.tv_3);
        tv3.setOnClickListener(this);
        TextView tv4 = (TextView) findViewById(R.id.tv_4);
        tv4.setOnClickListener(this);
        TextView tv5 = (TextView) findViewById(R.id.tv_5);
        tv5.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_1://Android刷新合集
                jumpActivity(MainRefreshActivity.class);
                break;
            case R.id.tv_2://Android悬停列表合集
                jumpActivity(MainSuspendActivity.class);
                break;
            case R.id.tv_3://Android二维码扫描合集
                jumpActivity(MainCodeActivity.class);
                break;
            case R.id.tv_4://Android联系人列表
                jumpActivity(MainContactActivity.class);
                break;
            case R.id.tv_5://Android适配7.0相机拍照
                jumpActivity(MainHeaderActivity.class);
                break;

        }
    }

    private void jumpActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
