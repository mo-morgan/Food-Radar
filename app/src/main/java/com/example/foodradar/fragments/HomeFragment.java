package com.example.foodradar.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodradar.R;

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

        return view;
    }

}
