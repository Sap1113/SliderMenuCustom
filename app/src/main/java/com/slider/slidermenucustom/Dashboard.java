package com.slider.slidermenucustom;

import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
