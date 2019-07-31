package com.example.foodradar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.foodradar.adapter.SectionsPageAdapter;
import com.example.foodradar.fragment.HomeFragment;
import com.example.foodradar.fragment.LoadingFragment;
import com.example.foodradar.fragment.RecommendedFragment;
import com.example.foodradar.fragment.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
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

        //loading the default fragment
        loadFragment(new HomeFragment());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        FragmentManager fragmentManager = getSupportFragmentManager();
        mSectionsPageAdapter = new SectionsPageAdapter(fragmentManager);
    }


    /**
     * On return from MapActivity, reset MainActivity to default
     */
    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setVisibility(View.VISIBLE);

        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setEnabled(true);
    }

    private void setupViewPager(ViewPager viewPager) {
        // TODO: see if we can replace the words with icons in the future
        mSectionsPageAdapter.addFragment(new HomeFragment(), "Home");
        mSectionsPageAdapter.addFragment(new SettingsFragment(), "Settings");
        viewPager.setAdapter(mSectionsPageAdapter);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.action_home:
                fragment = new HomeFragment();
                break;

            case R.id.action_recommended:
                fragment = new RecommendedFragment();
                break;

            case R.id.action_settings:
                fragment = new SettingsFragment();
                break;
        }

        return loadFragment(fragment);
    }
}
