package study.com.androidfunctions.suspend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import study.com.androidfunctions.R;

public class MainSuspendActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_suspend);

        TextView tv1 = (TextView) findViewById(R.id.tv_1);
        tv1.setOnClickListener(this);
        TextView tv2 = (TextView) findViewById(R.id.tv_2);
        tv2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_1:  //ListView顶部悬停
                jumpActivity(LvSuspendActivity.class);
                break;
            case R.id.tv_2://RecyclerView顶部悬停
                jumpActivity(RvSuspendActivity.class);
                break;
        }
    }

    private void jumpActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

}
