package com.example.camerademo1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.camerademo1.AutoFitTextureView;
import com.example.camerademo1.CameraController;
import com.example.camerademo1.R;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextureView mTextureview;

    private Button mTakePictureBtn;//拍照
    private Button mVideoRecodeBtn;//开始录像
    private CameraController mCameraController;
    private boolean mIsRecordingVideo; //开始停止录像
    public static String BASE_PATH = Environment.getExternalStorageDirectory() + "/AAA";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取相机管理类的实例
        mCameraController = CameraController.getmInstance(this);
        mCameraController.setFolderPath(BASE_PATH);
        initView();

    }

    private void initView() {
        mTextureview =findViewById(R.id.textureview);
        mTakePictureBtn = (Button) findViewById(R.id.take_picture_btn);
        mTakePictureBtn.setOnClickListener(this);
        mVideoRecodeBtn = (Button) findViewById(R.id.video_recode_btn);
        mVideoRecodeBtn.setOnClickListener(this);
        XXPermissions.with(this).permission(Permission.READ_EXTERNAL_STORAGE,Permission.WRITE_EXTERNAL_STORAGE,Permission.CAMERA,Permission.RECORD_AUDIO)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        mCameraController.InitCamera(mTextureview);
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean never) {
                        Toast.makeText(MainActivity.this, "请先授予权限", Toast.LENGTH_SHORT).show();
                        XXPermissions.startApplicationDetails(MainActivity.this);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.take_picture_btn:
                mCameraController.takePicture();
                break;
            case R.id.video_recode_btn:
                if (mIsRecordingVideo) {
                    mIsRecordingVideo = !mIsRecordingVideo;
                    mCameraController.stopRecordingVideo();
                    mVideoRecodeBtn.setText("开始录像");
                    Toast.makeText(this, "录像结束", Toast.LENGTH_SHORT).show();

                } else {
                    mVideoRecodeBtn.setText("停止录像");
                    mIsRecordingVideo = !mIsRecordingVideo;
                    mCameraController.startRecordingVideo();
                    Toast.makeText(this, "录像开始", Toast.LENGTH_SHORT).show();

                }
                break;

        }
    }
}