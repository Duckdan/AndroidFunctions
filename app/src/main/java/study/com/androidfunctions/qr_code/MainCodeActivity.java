package study.com.androidfunctions.qr_code;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import study.com.androidfunctions.R;

public class MainCodeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_code);
        TextView tv1 = (TextView) findViewById(R.id.tv_1);
        tv1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_1:  //SmartRefreshA刷新合集
                jumpActivity(ZxingCodeActivity.class);
                break;
        }
    }

    private void jumpActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
