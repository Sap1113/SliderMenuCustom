package com.slider.slidermenucustom;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.slider.slidermenucustom.fragments.Home1Fragment;
import com.slider.slidermenucustom.fragments.Home2Fragment;
import com.slider.slidermenucustom.fragments.HomeFragment;
import com.slider.slidermenucustom.uc.CircularImageView;
import com.slider.slidermenucustom.uc.MainLayout;
import com.slider.slidermenucustom.uc.PermissionUtils;
import java.io.File;
import java.io.IOException;


public class Dashboard extends FragmentActivity implements View.OnClickListener {

    private String TAG = "Dashboard";
    private MainLayout mainLayout = null;
    public static ImageView img_menu;
    public static ImageView img_back;
    public static TextView txt_header_title;
    private static ImageView img_menu_Photo;

    private LinearLayout ll_menu_home, ll_menu_home1, ll_menu_home2;

    private Fragment current;
    public static FragmentManager fm;
    private boolean doubleBackToExitPressedOnce = false;

    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;

    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    public static final String FILE_NAME = "temp.jpg";
    public static final String FILE_NAME1 = "temp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setGenerateUI();
    }

    private void setGenerateUI() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mainLayout = (MainLayout) Dashboard.this.getLayoutInflater().inflate(R.layout.activity_dashboard, null);
        setContentView(mainLayout);

        img_menu = (ImageView) findViewById(R.id.img_header_menu);
        img_menu.setOnClickListener(this);

        img_back = (ImageView) findViewById(R.id.img_header_back);
        img_back.setOnClickListener(this);

        txt_header_title = (TextView) findViewById(R.id.txt_header_title);
        txt_header_title.setText("Home");
        SliderMenuContent();


        HomeFragment frgmtHomeMap = new HomeFragment(Dashboard.this);
        replaceMenuFragment(frgmtHomeMap, "Home");

    }

    public void toggleMenu(View v) {
        mainLayout.toggleMenu();
    }

    private void SliderMenuContent() {

        img_menu_Photo = (CircularImageView) findViewById(R.id.img_menu_Photo);
        img_menu_Photo.setOnClickListener(this);

        ll_menu_home = (LinearLayout) findViewById(R.id.ll_sliderMenu_home);
        ll_menu_home.setOnClickListener(this);

        ll_menu_home1 = (LinearLayout) findViewById(R.id.ll_sliderMenu_home1);
        ll_menu_home1.setOnClickListener(this);

        ll_menu_home2 = (LinearLayout) findViewById(R.id.ll_sliderMenu_home2);
        ll_menu_home2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.img_menu_Photo:
                AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                builder
                        .setMessage("Choose a picture")
                        .setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startGalleryChooser();
                            }
                        })
                        .setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startCamera();
                            }
                        });
//                builder.create().show();
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                break;
            case R.id.img_header_menu:
                mainLayout.toggleMenu();
                break;
            case R.id.img_header_back:
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 1) {
                    fm.popBackStack();
                }
                break;


            case R.id.ll_sliderMenu_home:
                HomeFragment frgmtHome = new HomeFragment(Dashboard.this);
                current = getSupportFragmentManager().findFragmentById(R.id.activity_main_content_fragment);
                if (!current.getClass().equals(frgmtHome.getClass())) {
                    replaceMenuFragment(frgmtHome, "Home");
                } else {
                    mainLayout.toggleMenu();
                }
                break;

            case R.id.ll_sliderMenu_home1:
                Home1Fragment frgmtHome1 = new Home1Fragment(Dashboard.this);
                current = getSupportFragmentManager().findFragmentById(R.id.activity_main_content_fragment);
                if (!current.getClass().equals(frgmtHome1.getClass())) {
                    replaceMenuFragment(frgmtHome1, "Home 1");
                } else {
                    mainLayout.toggleMenu();
                }
                break;
            case R.id.ll_sliderMenu_home2:
                Home2Fragment frgmtHome2 = new Home2Fragment(Dashboard.this);
                current = getSupportFragmentManager().findFragmentById(R.id.activity_main_content_fragment);
                if (!current.getClass().equals(frgmtHome2.getClass())) {
                    replaceMenuFragment(frgmtHome2, "Home 2");
                } else {
                    mainLayout.toggleMenu();
                }
                break;

        }
    }

    private void replaceMenuFragment(Fragment targetFragment, String _title) {
        if (mainLayout.isMenuShown()) {
            mainLayout.toggleMenu();
        }
        txt_header_title.setText(_title);
        fm = Dashboard.this.getSupportFragmentManager();
        boolean fragmentPopped = fm.popBackStackImmediate(_title, 0);
        if (!fragmentPopped) {
            fm.beginTransaction()
                    .replace(R.id.activity_main_content_fragment, targetFragment, _title)
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(_title)
                    .commit();
        }
    }

    //----------------- Start Gallery & Camera Function ------------
    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {
            try {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", getCameraFile());
                Log.i(TAG, "startCamera: photoUri :: " + photoUri);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public File getCameraFile() {
//        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//        Log.i(TAG, "----- Dir :: " + new File(dir, FILE_NAME).toString() + "");
//        return new File(dir, FILE_NAME);

        return new File(getCacheDir(), FILE_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "onActivityResult: requestCode :: " + requestCode);
        Log.i(TAG, "onActivityResult: GALLERY_IMAGE_REQUEST :: " + GALLERY_IMAGE_REQUEST);
        Log.i(TAG, "onActivityResult: CAMERA_IMAGE_REQUEST :: " + CAMERA_IMAGE_REQUEST);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Bitmap bitmap = BitmapFactory.decodeFile(Uri.fromFile(getCameraFile()).getPath().toString());
            img_menu_Photo.setImageBitmap(bitmap);
//            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
//            uploadImage(photoUri);
        }
    }

    public void uploadImage(Uri uri) {
        Log.i(TAG, "uploadImage: :: " + uri.getPath().toString());
        if (uri != null) {
            // scale the image to save on bandwidth
            try {
                Bitmap bitmap = scaleBitmapDown(
                        MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                        1200);
                img_menu_Photo.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, "Something is wrong with that image. Pick a different one please.", Toast.LENGTH_LONG).show();
        }
    }

    public Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }


    //----------------- Finished Camera & Gallery Function ------------

    //------------ Permission Result ----------------
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
    }

    //------------ Finish Permission Result ----------------
    @Override
    public void onBackPressed() {
        if (mainLayout.isMenuShown()) {
            mainLayout.toggleMenu();
        } else if (getSupportFragmentManager().getBackStackEntryCount() != 1) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            //  super.onBackPressed();
            getSupportFragmentManager().popBackStack();
            finish();

        }
    }
}
