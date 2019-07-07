package com.example.foodradar.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.foodradar.MainActivity;
import com.example.foodradar.MapsActivity;
import com.example.foodradar.R;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private static final String TAG = "My_message";

    /**
     * The pulse pattern for the radar button
     */
    private PulsatorLayout pulsator;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        pulsator = (PulsatorLayout) view.findViewById(R.id.pulsator);
        pulsator.start();

        Button searchButton = (Button) view.findViewById(R.id.search_button);
        searchButton.setVisibility(View.VISIBLE);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clicked button!");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    beginAnimation(v);
                } else {
                    startMapActivity();
                }
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void beginAnimation(View view) {
        RelativeLayout layoutContent = (RelativeLayout) getActivity().findViewById(R.id.home_layout);
        PulsatorLayout pulsator = (PulsatorLayout) getActivity().findViewById(R.id.pulsator);

        int centerX = (int) (view.getX() + view.getWidth() / 2);
        int centerY = (int) (view.getY() + view.getHeight() / 2);

        int startRadius = 0;
        int endRadius = (int) Math.hypot(layoutContent.getWidth(), layoutContent.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(layoutContent, centerX, centerY, startRadius, endRadius);
        anim.setDuration(1000);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
//                view.setVisibility(View.INVISIBLE);
//                pulsator.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(TAG, "animation ended");
                startMapActivity();
            }
        });

        anim.start();
    }

    private void startMapActivity() {
        Intent intent = new Intent(getActivity(), MapsActivity.class);
        startActivity(intent);
    }
}

