package study.com.androidfunctions.qr_code;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.uuzuche.lib_zxing.activity.CodeUtils;

import study.com.androidfunctions.R;
import study.com.androidfunctions.utils.PermissionCheckUtils;

public class ZxingCodeActivity extends AppCompatActivity implements View.OnClickListener {
    private final int SCAN_CODE = 100;
    private TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxing_code);
        TextView tv1 = (TextView) findViewById(R.id.tv_1);
        tv1.setOnClickListener(this);
        tv2 = (TextView) findViewById(R.id.tv_2);

        PermissionCheckUtils.setOnOnWantToOpenPermissionListener(new PermissionCheckUtils.OnWantToOpenPermissionListener() {
            @Override
            public void onWantToOpenPermission() {
                Toast.makeText(ZxingCodeActivity.this, "请赋予应用使用相机的权限！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_1:  //SmartRefreshA刷新合集
                int size = PermissionCheckUtils.checkActivityPermissions(ZxingCodeActivity.this, new String[]{Manifest.permission.CAMERA}, SCAN_CODE, null);
                if (size == 0) {
                    jumpActivity(ScanCodeActivity.class);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SCAN_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                jumpActivity(ScanCodeActivity.class);
            } else {
                Toast.makeText(ZxingCodeActivity.this, "您拒绝了应用使用相机的请求！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                String result = data.getStringExtra(CodeUtils.RESULT_STRING);
                tv2.setText(result);
            }
        }
    }

    private void jumpActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, SCAN_CODE);
    }
}
