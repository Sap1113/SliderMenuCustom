package com.slider.slidermenucustom.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.slider.slidermenucustom.Dashboard;
import com.slider.slidermenucustom.R;

public class HomeFragment extends Fragment {

    private Activity context;
    private static View rootView;

    public HomeFragment() {
        // Required empty public constructor
    }

    public HomeFragment(Activity context) {
        this.context = context;
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null)
                parent.removeView(rootView);
        }
        try {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
        } catch (InflateException e) {
            /* map is already there, just return view as it is */
        }
        return rootView;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Dashboard.img_back.setVisibility(View.GONE);
        Dashboard.img_menu.setVisibility(View.VISIBLE);
        Dashboard.txt_header_title.setText("Home");
    }

    @Override
    public void onResume() {
        super.onResume();
        Dashboard.img_back.setVisibility(View.GONE);
        Dashboard.img_menu.setVisibility(View.VISIBLE);
        Dashboard.txt_header_title.setText("Home");
    }
}
