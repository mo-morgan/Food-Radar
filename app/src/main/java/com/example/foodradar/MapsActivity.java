package com.example.foodradar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * FIXME Everything you need to know about this activity/layout (Listed by priorities):
 *  -___SEARCH BAR___
 *  - Decorate auto-complete search bar to have a unique color
 *  - On search bar click, view should expand into suggestions, etc. (NEED DESIGNING)
 *  - On place selected, view should switch the the place's location, as well as a small popup describing the restaurant
 *  -   -> If user clicks the location again (or an expand button), it will go into restaurant activity, describing even more
 *  -___MAP START___
 *  - Immediately after activity starts (onCreate, not onResume or anything), set center to be user's location
 *  - Auomatically search area for restaurants (and everytime user stops sliding for more than 1 sec or so)
 *  - Begin loading the markers (of only restaurants ofc), clustering if necessary
 *  - Markers should have a price tag $$, or $, or $$$$$$$ depending how expensive the restaurant is
 *  -   -> as well as I guess the type of food? we have to design the markers
 *  -   -> Favorite restaurants should display something different like a heart on top or smth
 *  -___MAP LAYOUT___
 *  - Top: the search bar, with an option to switch forth and between list-view (in case user hates maps)
 *  - Map: the map ofc, with "Locate me" button on the side, and "Favorite", and "Filter" buttons as well
 *  - Bottom: where the small popup describing the restaurant will be, covering approx. 1/3 the screen
 *  -___SMALL POPUP DETAILS___
 *  - Snippet describing the address
 *  - Raiting, types of food, some pictures, busy scale, hours of operations, closing soon(?)
 *  - Buttons: Get direction, More detail
 *  -___FILTER___
 *  - more on the design next time, but will give similar popup feeling to auto-complete
 *  - after user selects a filter and presses done (or multiple filters), map should display only the filtered restaurants as markers
 *  -___LIST VIEW___
 *  - In case user hates maps and wants a list view, can be prioritized by location, etc.
 *  - Will basically be what the map will be showing except in a list-view (users can filter this as well)
 */

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    // for debugging purposes
    private static final String TAG = "My_message";

    /**
     * Default LatLng when the user location is null
     */
    private static final LatLng mDefaultLatLng = new LatLng(49, -123);

    /**
     * Default zoom value
     */
    private static final float DEFAULT_ZOOM = 15.0f;

    private GoogleMap mMap;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Location mLastKnownLocation;

    /**
     * Map location requesting variables
     */
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public static boolean mLocationPermissionGranted = false;

    /**
     * HashMap of currently displayed markers stored as:
     *  - Key: the marker's LatLng
     *  - Value: the marker
     */
    private HashMap<LatLng, Marker> mMarkers = new HashMap<>();

    /**
     * Currently clicked marker
     */
    private Marker clickedMarker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        // If the permission was granted previously, then set mLocationPermissionGranted to be true
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "start debugging last known location");
            mLocationPermissionGranted = true;
        }
    }

    private boolean isLocationFree(LatLng latlng) {
        return !mMarkers.containsKey(latlng);
    }

    private void getLocationPermission() {
        /**
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }

        // Once the user grants location permission, immediately update the device location on the UI
        if (mLocationPermissionGranted) {
            updateLocationUI();
            getDeviceLocation();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }


    /**
     * Setup the auto complete search bar
     * - If loc is null, setup without location bias
     * - else, setup with location bias near user's location
     */
    private void setupAutoCompleteFragment(Location loc) {
        // Initialize Places
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.ApiKey);
        }

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Set which information to retrieve from the Place API
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));

        // Set the location to be biased near the user location (first LatLng is SW, second is NE)
        if (loc != null) {
            Log.d(TAG, "set location bias");
            autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
                    new LatLng(loc.getLatitude() - 0.05, loc.getLongitude() - 0.05),
                    new LatLng(loc.getLatitude() + 0.05, loc.getLongitude() + 0.05)
            ));
        }

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    // If the marker is already displayed, don't add a new marker to the map

                    if (isLocationFree(latLng)) {
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(place.getName())
                                .snippet(place.getAddress()));
                        mMarkers.put(latLng, marker);
                        clickedMarker = marker;
                    } else {
                        Marker marker = mMarkers.get(latLng);
                        clickedMarker = marker;
                    }
                } else {
                    Log.d(TAG, "Missing LatLng for place");
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /**
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                            // Need to setup auto complete fragment here because task is ran on another thread
                            setupAutoCompleteFragment(mLastKnownLocation);
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLatLng, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);

                            // if no location is retrieved, setup autocomplete with no location bias
                            setupAutoCompleteFragment(null);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    // TODO: Increase clicked marker size, add small snippet maybe, add info window on the bottom
    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
