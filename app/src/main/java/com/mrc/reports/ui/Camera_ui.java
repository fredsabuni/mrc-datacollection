package com.mrc.reports.ui;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.camerakit.CameraKit;
import com.camerakit.CameraKitView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.mrc.reports.BaseActivity;
import com.mrc.reports.R;
import com.mrc.reports.utils.PermissionUtils;
import com.mrc.reports.utils.Utils;

import java.io.File;
import java.util.ArrayList;

public class Camera_ui extends BaseActivity {

    public static final String IMAGE_KEY = "IMAGE";
    public final int STORAGE_PERMISSION = 109;
    public final int SETTINGS_CODE = 101;
    private CameraKitView camera;
    private ImageView ivCapture;
    private ImageView ivFlashOn;
    private ImageView ivFlashOff;
    private ImageView ivSwitchCamera;
    private ImageView ivDone;
    private ImageView ivDisplayImg;
    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private boolean invertCamera;

    private String selectedImg = "";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_ui);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        camera = findViewById(R.id.camera_custom_gallery);
        ivCapture = findViewById(R.id.iv_capture_custom_gallery);
        ivFlashOn = findViewById(R.id.iv_flashOn_custom_gallery);
        ivFlashOff = findViewById(R.id.iv_flashOff_custom_gallery);
        ivSwitchCamera = findViewById(R.id.iv_switchCamera_custom_gallery);
        ivDone = findViewById(R.id.send_btn);
        ivDisplayImg = findViewById(R.id.captured_img);

        checkForPermission();
        initClicks();

    }

    public void checkForPermission() {
        if (PermissionUtils.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            init();
        } else {
            if (PermissionUtils.shouldShowRational(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Display UI and wait for user interaction
                PermissionUtils.requestPermissions(
                        this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION);
                PermissionUtils.markedPermissionAsAsked(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                if (PermissionUtils.hasAskedForPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "you need to provide permissions to proceed further", Toast.LENGTH_SHORT).show();
                    PermissionUtils.goToAppSettings(this, SETTINGS_CODE);
                } else {
                    PermissionUtils.requestPermissions(
                            this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION);
                }

            }

        }
    }

    public void init() {
        loadAnimations();
    }

    @SuppressLint("ResourceType")
    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.anim.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.anim.in_animation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        camera.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        camera.onStart();
    }

    @Override
    protected void onPause() {
        camera.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        camera.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init();
            } else {
                checkForPermission();
                // Permission was denied or request was cancelled
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_CODE) {
            checkForPermission();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.onStop();
    }

    void initClicks() {

        ivCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });
        ivFlashOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashOn();
            }
        });
        ivFlashOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flashOff();
            }
        });
        ivSwitchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchCamera();
            }
        });
        ivDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchSelectedFiles();
            }
        });

    }


    void fetchSelectedFiles() {
        if(TextUtils.isEmpty(selectedImg)){
            Toast.makeText(this,R.string.take_picture,Toast.LENGTH_SHORT).show();
        }else {
            sendResultBackToActivity(selectedImg);
        }

    }

    void captureImage() {

        camera.captureImage(new CameraKitView.ImageCallback() {
            @Override
            public void onImage(CameraKitView cameraKitImage, byte[] captureImage) {
                File photo = Utils.writeImage(captureImage);
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(photo.getAbsolutePath());
                Log.d("Path", arrayList.get(0));
                ivDisplayImg.setImageURI(Uri.parse(arrayList.get(0)));
                ivDone.setVisibility(View.VISIBLE);
                selectedImg = arrayList.get(0);
            }
        });
    }


    void flashOn() {
        camera.setFlash(CameraKit.FLASH_ON);
        ivFlashOn.setVisibility(View.GONE);
        ivFlashOff.setVisibility(View.VISIBLE);
    }

    void flashOff() {
        camera.setFlash(CameraKit.FLASH_OFF);
        ivFlashOn.setVisibility(View.VISIBLE);
        ivFlashOff.setVisibility(View.GONE);
    }

    void switchCamera() {
        if (!invertCamera) {
            mSetRightOut.setTarget(ivSwitchCamera);
            mSetLeftIn.setTarget(ivSwitchCamera);
            mSetRightOut.start();
            mSetLeftIn.start();
            camera.toggleFacing();
            invertCamera = true;
        } else {
            mSetRightOut.setTarget(ivSwitchCamera);
            mSetLeftIn.setTarget(ivSwitchCamera);
            mSetRightOut.start();
            mSetLeftIn.start();
            camera.toggleFacing();
            invertCamera = false;
        }
    }

    public void sendResultBackToActivity(String files) {
        Intent intent = new Intent();
        intent.putExtra(IMAGE_KEY, files);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

}
