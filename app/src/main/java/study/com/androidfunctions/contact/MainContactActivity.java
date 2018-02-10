package study.com.androidfunctions.contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import study.com.androidfunctions.R;

public class MainContactActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_contact);
        TextView tv1 = (TextView) findViewById(R.id.tv_1);
        tv1.setOnClickListener(this);
        TextView tv2 = (TextView) findViewById(R.id.tv_2);
        tv2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_1:  //SmartRefreshA刷新合集
                jumpActivity(FirstContactActivity.class);
                break;
            case R.id.tv_2:
                jumpActivity(SecondContactActivity.class);
                break;
        }
    }

    private void jumpActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
