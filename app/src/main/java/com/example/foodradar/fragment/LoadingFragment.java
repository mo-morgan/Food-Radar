package com.example.foodradar.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.example.foodradar.R;

public class LoadingFragment extends Fragment {
    private static final String TAG = "My_message";
    private Fragment fragmentToLoad;
    private FragmentActivity context; // Used to get getSupportFragmentManager()


    @Override
    public void onAttach(Activity activity) {
        context =(FragmentActivity) activity;
        super.onAttach(activity);
    }

    public LoadingFragment() {
        // Required empty public constructor
    }

    public boolean setFragment(Fragment fragment) {
        if (fragment != null) {
            fragmentToLoad = fragment;
            return true;
        }
        return false;
    }

    private boolean loadFragment() {
        //switching fragment
        if (fragmentToLoad != null) {
            Log.d(TAG, "Loading!");
            context.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragmentToLoad)
                    .commit();
            return true;
        }
        return false;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loading, container, false);
        new Thread(() -> loadFragment()).start();
        return view;
    }

}
