package study.com.androidfunctions.header;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import study.com.androidfunctions.R;
import study.com.androidfunctions.header.dialog.QQHeaderImageDialog;
import study.com.androidfunctions.header.dialog.WCHeaderImageDialog;
import study.com.androidfunctions.utils.PermissionCheckUtils;

public class MainHeaderActivity extends AppCompatActivity implements View.OnClickListener {
    //权限的请求码
    private final int PERMISSION_CODE = 100;
    //拍照的请求码
    private final int TAKE_PHOTO_CODE = 200;
    //裁剪的请求码
    private static final int CROP_CODE = 300;
    private QQHeaderImageDialog qqDialog;
    private WCHeaderImageDialog wcDialog;
    //图片Uri
    Uri imgUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_header);
        qqDialog = new QQHeaderImageDialog(this, 0, this);
        wcDialog = new WCHeaderImageDialog(this, 0, this);

        TextView tv1 = (TextView) findViewById(R.id.tv_1);
        tv1.setOnClickListener(this);


        PermissionCheckUtils.setOnOnWantToOpenPermissionListener(new PermissionCheckUtils.OnWantToOpenPermissionListener() {
            @Override
            public void onWantToOpenPermission() {
                Toast.makeText(MainHeaderActivity.this, "请赋予使用相机和读取内存的权限！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_1:
                qq();
                break;
        }
    }


    private void qq() {
        qqDialog.show();
        qqDialog.setOnResultUriListener(new QQHeaderImageDialog.OnResultListener() {
            @Override
            public void sexFlag(int type) {
                switch (type) {
                    case 0:  //去相册,此处由开源框架TakePhoto完成
                        Toast.makeText(MainHeaderActivity.this, "用TakePhoto完成", Toast.LENGTH_SHORT).show();
                        break;
                    case 1: //去拍照
                        int size = PermissionCheckUtils.checkActivityPermissions(MainHeaderActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSION_CODE, null);
                        if (size == 0) { //已授权
                            dealCamera();
                        }
                        break;
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE:
                if (permissions.length > 0) {
                    boolean flag = true;
                    for (int i = 0; i < permissions.length; i++) {
                        flag &= (grantResults[i] == PackageManager.PERMISSION_GRANTED);
                    }
                    if (flag) { //处理相机
                        dealCamera();
                    } else {
                        Toast.makeText(MainHeaderActivity.this, "授权失败！", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    /**
     * 处理相机拍照
     */
    private void dealCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File baseFile = Environment.getExternalStorageDirectory();
        File file = new File(baseFile.getPath() + "/heads", "avatar_"
                + String.valueOf(System.currentTimeMillis())
                + ".png");

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //根据file的路径匹配res下xml中的配置文件下的标签，
            imgUri = FileProvider.getUriForFile(this, "study.com.androidfunctions.fileprovider", file);
        } else {
            imgUri = Uri.fromFile(file);
        }
        Log.e("path", imgUri.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, TAKE_PHOTO_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO_CODE: //拍照
                takePhotoResult(resultCode, data);
                break;
            case CROP_CODE: //裁剪完成
                cropResult(resultCode, data);
                break;
        }
    }


    /**
     * 处理拍照的结果
     *
     * @param resultCode
     * @param data
     */
    private void takePhotoResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) { //拍照成功
            doCrop();// 裁剪图片
        }
    }

    /**
     * 裁剪
     */
    private void doCrop() {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(
                intent, 0);
        int size = list.size();

        if (size == 0) {
            Toast.makeText(MainHeaderActivity.this, "裁剪失败，没有找到裁剪图片的应用！", Toast.LENGTH_SHORT).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            intent.setDataAndType(imgUri, "image/*");
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("scale", true);
            intent.putExtra("return-data", true);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());


            Intent i = new Intent(intent);
            ResolveInfo res = list.get(0);
            i.setComponent(new ComponentName(res.activityInfo.packageName,
                    res.activityInfo.name));
            startActivityForResult(i, CROP_CODE);
        }
    }

    /**
     * 处理拍照结果
     *
     * @param resultCode
     * @param data
     */
    private void cropResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                Bitmap bitmap = bundle.getParcelable("data");
                File file = saveBitmapToDisk(bitmap);
            }
        }
    }

    /**
     * 将Bitmap存储至磁盘
     */
    private File saveBitmapToDisk(Bitmap bitmap) {
        File baseFile = Environment.getExternalStorageDirectory();
        File file = new File(baseFile.getPath() + "/heads", "crop_"
                + String.valueOf(System.currentTimeMillis())
                + ".png");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            //将Bitmap存储至本地磁盘
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;

    }

    private void jumpActivity(Class clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
