package itp341.dunlap.forrest.water.fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import itp341.dunlap.forrest.water.R;
import itp341.dunlap.forrest.water.activities.MainActivity;

/**
 * Created by FDUNLAP on 4/27/2017.
 */

public class MapViewFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    MapView mMapView;
    private GoogleMap googleMap;

    FloatingActionButton drawerFAB;

    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;

    Location mLastLocation;
    Marker mCurrLocationMarker;

    Marker mostRecentTap;

    Geocoder geocoder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        geocoder = new Geocoder(getContext(), Locale.getDefault());

        mostRecentTap = null;

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);

        mMapView.setClickable(true);

        drawerFAB = (FloatingActionButton) rootView.findViewById(R.id.menu_fab);
        drawerFAB.setOnClickListener(this);

        AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        checkFirstTime();

        return rootView;
    }

    private void checkFirstTime() {
        SharedPreferences sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        if(!sp.getBoolean(getString(R.string.map_first_bool), false)){
            AboutDialogFragment aboutFrag = AboutDialogFragment.newInstance(getString(R.string.map_first));
            aboutFrag.show(getActivity().getSupportFragmentManager(), "about");
            sp.edit().putBoolean(getString(R.string.map_first_bool), true).apply();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onClick(View v) {
        ((MainActivity) getActivity()).onQuestionInteraction(getResources().getInteger(R.integer.menu_drawer_action));
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }



    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng USCenter = new LatLng(37.090240, -95.712891);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_COARSE_LOCATION}, getResources().getInteger(R.integer.courselocrequest));
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, getResources().getInteger(R.integer.finelocrequest));
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(USCenter, 3));


        //KML LAYERS TODO: Reach to get these click-through. For now, just click on the state.
//        try {
//            KmlLayer layer = new KmlLayer(map, R.raw.st_us, getContext());
//            Log.e("Map", "Loaded KML");
//            layer.setOnFeatureClickListener(new Layer.OnFeatureClickListener() {
//                @Override
//                public void onFeatureClick(Feature feature) {
//
//                }
//            });
//            layer.addLayerToMap();
//            if(layer.isLayerOnMap()) Log.e("Map", "On map");
//        } catch (XmlPullParserException e) {
//            Log.e("xml", e.getMessage().toString());
//        } catch (IOException e) {
//            Log.e("xml", e.getMessage().toString());
//        }

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Address address = null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if(!addressList.isEmpty())
                        address = addressList.get(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(address!= null) {
                    if (address.getAdminArea() != null && address.getCountryName() != null) {
                        String state = address.getAdminArea();
                        String country = address.getCountryName();
                        if (isSupported(country)) {

                            showInfoWindow(state, latLng);
                        }
                    }
                }
            }
        });
    }

    private void showInfoWindow(String state, LatLng latLng) {
        if(mostRecentTap == null)
            mostRecentTap = googleMap.addMarker(new MarkerOptions()
            .position(latLng)
            .title(state));
        else{
            mostRecentTap.hideInfoWindow();
            mostRecentTap.setPosition(latLng);
            mostRecentTap.setTitle(state);
        }
        getDropsForState(state);
    }


    //TODO Actually implement the get drops thing? ;D
    private void getDropsForState(String state) {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("region").child(state).child("drops")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null)
                mostRecentTap.setSnippet(getString(R.string.map_drops_added)
                + dataSnapshot.getValue().toString());
                else mostRecentTap.setSnippet(getString(R.string.no_drops_yet));

                mostRecentTap.showInfoWindow();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean isSupported(String country) {

        return(country.compareTo("United States") == 0);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = googleMap.addMarker(markerOptions);

        //move map camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }
}
