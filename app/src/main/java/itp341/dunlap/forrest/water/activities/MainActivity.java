package itp341.dunlap.forrest.water.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.android.gms.ads.MobileAds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import itp341.dunlap.forrest.water.R;
import itp341.dunlap.forrest.water.adapters.NavListAdapter;
import itp341.dunlap.forrest.water.fragments.DonateFragment;
import itp341.dunlap.forrest.water.fragments.MapViewFragment;
import itp341.dunlap.forrest.water.fragments.ProfileFragment;
import itp341.dunlap.forrest.water.fragments.QuestionFragment;
import itp341.dunlap.forrest.water.fragments.StartGameFragment;
import itp341.dunlap.forrest.water.singletons.CategoryManager;
import itp341.dunlap.forrest.water.singletons.UserManager;
import itp341.dunlap.forrest.water.views.NavDrawerItem;


//TODO: CRITICAL: Deal with lifecycle and rotation, etc.

public class MainActivity extends AppCompatActivity implements QuestionFragment.OnFragmentInteractionListener, DonateFragment.OnFragmentInteractionListener {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private FrameLayout mMainFrame;
    private FragmentManager mFragmentManager;

    //Fragments
    private StartGameFragment mStartFragment;
    private DonateFragment mDonateFragment;
    private MapViewFragment mMapFragment;
    private ProfileFragment mProfileFragment;

    private double latitude;
    private double longitude;

    static Context mContext;

    String[] mPagesList;

    public static Context getContext() {
        return mContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPagesList = getResources().getStringArray(R.array.pages);

        mFragmentManager = getSupportFragmentManager();

        mMainFrame = (FrameLayout) findViewById(R.id.frame_main_activity);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.nav_drawer);

        //Adapter for drawer list
        ArrayList<NavDrawerItem> ndItems = new ArrayList<>();
        ndItems.add(new NavDrawerItem(mPagesList[0], NavDrawerItem.type.Home, this));
        ndItems.add(new NavDrawerItem(mPagesList[1], NavDrawerItem.type.Map, this));
        ndItems.add(new NavDrawerItem(mPagesList[2], NavDrawerItem.type.Profile, this));
        ndItems.add(new NavDrawerItem(mPagesList[3], NavDrawerItem.type.Donate, this));
        ndItems.add(new NavDrawerItem(mPagesList[4], NavDrawerItem.type.Settings, this));


        mDrawerList.setAdapter(new NavListAdapter(this, ndItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout.addDrawerListener(new DrawerListener());

        //initFragment
        if(savedInstanceState == null) {
            mStartFragment = new StartGameFragment();
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.add(R.id.frame_main_activity, mStartFragment);
            ft.commit();
        }

        //Other Fragments
        mDonateFragment = new DonateFragment();
        mMapFragment = new MapViewFragment();
        mProfileFragment = new ProfileFragment();

        //init ads
        MobileAds.initialize(getApplicationContext());

        //If the category manager doesn't know the categories, add them.
        if (CategoryManager.getInstance().getCategories().isEmpty())
            CategoryManager.getInstance().giveStrings(getResources().getStringArray(R.array.categories));

        //check permissions
        checkPermissions();

        //check to see if user is in database or not. If not, add them. If so, populate.


        mDrawerLayout.setDrawerElevation(16.0f);
        mDrawerLayout.setBackground(getDrawable(R.drawable.drawer_shadow));
        mDrawerList.setItemChecked(0, true);
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        getResources().getInteger(R.integer.finelocrequest));
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        getResources().getInteger(R.integer.courselocrequest));

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_NETWORK_STATE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_NETWORK_STATE},
                        getResources().getInteger(R.integer.netrequest));

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

            }
        } else {
            LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            boolean network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            Location location;

            if (network_enabled) {

                location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (location != null) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                }

                try {
                    if(location != null) {
                        Geocoder gc = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<Address> addrList = gc.getFromLocation(latitude, longitude, 1);
                        Address address = addrList.get(0);
                        UserManager.getInstance().setCurrentLocation(address.getAdminArea());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == getResources().getInteger(R.integer.finelocrequest)) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
            return;
        }
        if (requestCode == getResources().getInteger(R.integer.courselocrequest)) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
            return;
        }
        // other 'case' lines to check for other
        // permissions this app might request
        if (requestCode == getResources().getInteger(R.integer.netrequest)) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) ;

            LocationManager locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            boolean network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            Location location;

            if (network_enabled) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if(location!=null){
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                }
            }

            try {
                UserManager.getInstance().setCurrentLocation(new Geocoder(getContext(), Locale.getDefault()).getFromLocation(longitude,latitude,1).get(0).getAdminArea());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            UserManager.getInstance().setCurrentLocation(null);
        }
    }

    @Override
    public void onQuestionInteraction(int action) {
        if(action == getResources().getInteger(R.integer.menu_drawer_action)){
            toggleDrawer();
        }
    }

    public void toggleDrawer() {
        if(mDrawerLayout.isDrawerOpen(mDrawerList)) mDrawerLayout.closeDrawer(mDrawerList);
        else mDrawerLayout.openDrawer(mDrawerList);
    }

    @Override
    public void onDonateInteraction(int action, double val) {

        if(action == getResources().getInteger(R.integer.menu_drawer_action)) toggleDrawer();

    }

    //launch the game activity
    public void startPressed() {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            //TODO: UPDATE TO HANDLE FRAGMENT SWITCHES

            Fragment f = mFragmentManager.findFragmentById(R.id.frame_main_activity);

            switch(position){
                case 0:
                    if(!(f instanceof QuestionFragment)){
                        int backs = mFragmentManager.getBackStackEntryCount();
                        for(int i = 0; i < backs; ++i){
                            mFragmentManager.popBackStack();
                        }
                        FragmentTransaction ft =  mFragmentManager.beginTransaction();
                        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                        ft.replace(R.id.frame_main_activity, mStartFragment);
                        ft.commit();
                    }
                    break;
                case 1:
                    if(!(f instanceof MapViewFragment)){
                        FragmentTransaction ft =  mFragmentManager.beginTransaction();
                        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                        ft.replace(R.id.frame_main_activity, mMapFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                    break;
                case 2:
                    if(!(f instanceof ProfileFragment)){
                        FragmentTransaction ft =  mFragmentManager.beginTransaction();
                        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                        ft.replace(R.id.frame_main_activity, mProfileFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                    break;
                case 3:
                    if(!(f instanceof DonateFragment)){
                        FragmentTransaction ft =  mFragmentManager.beginTransaction();
                        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                        ft.replace(R.id.frame_main_activity, mDonateFragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                    break;
                case 4:
                    break;
            }


            //TODO: set item checked, change title on selection, close drawer.
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    private class DrawerListener implements DrawerLayout.DrawerListener{

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(View drawerView) {

        }

        @Override
        public void onDrawerClosed(View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    }
}
