package com.example.foodradar;

import com.example.foodradar.adapters.SectionsPageAdapter;
import com.example.foodradar.fragments.HomeFragment;
import com.example.foodradar.fragments.SettingsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    // for debugging purposes
    private static final String TAG = "My_message";

    /**
     * Keeping track of fragment positions
     */
    private static final int HOME_POSITION = 0;
    private static final int SETTINGS_POSITION = 1;

    /**
     * The {@link androidx.viewpager.widget.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * androidx.fragment.app.FragmentStatePagerAdapter.
     */
    private SectionsPageAdapter mSectionsPageAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
        }

        Log.d(TAG, "setContentView");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        FragmentManager fragmentManager = getSupportFragmentManager();
        mSectionsPageAdapter = new SectionsPageAdapter(fragmentManager);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        /**
         * The tab layout function calls
         */

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        // TODO: see if we can replace the words with icons in the future
        mSectionsPageAdapter.addFragment(new HomeFragment(), "Home");
        mSectionsPageAdapter.addFragment(new SettingsFragment(), "Settings");
        viewPager.setAdapter(mSectionsPageAdapter);
    }
}
