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

import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

    /**
     * The pulse pattern for the radar button
     */
    private PulsatorLayout pulsator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "setContentView");

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        FragmentManager fragmentManager = getSupportFragmentManager();
        mSectionsPageAdapter = new SectionsPageAdapter(fragmentManager);

        pulsator = (PulsatorLayout) findViewById(R.id.pulsator);

        fragmentManager.registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentResumed(@NonNull FragmentManager fm, @NonNull Fragment f) {
                super.onFragmentResumed(fm, f);
                if (f.equals(mSectionsPageAdapter.getItem(HOME_POSITION))) {
                    pulsator.start();
                } else if (f.equals(mSectionsPageAdapter.getItem(SETTINGS_POSITION))) {
                    pulsator.stop();
                }
            }
        }, true);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        pulsator.start();
    }

    private void setupViewPager(ViewPager viewPager) {
        // TODO: see if we can replace the words with icons in the future
        mSectionsPageAdapter.addFragment(new HomeFragment(), "Home");
        mSectionsPageAdapter.addFragment(new SettingsFragment(), "Settings");
        viewPager.setAdapter(mSectionsPageAdapter);
    }
}
