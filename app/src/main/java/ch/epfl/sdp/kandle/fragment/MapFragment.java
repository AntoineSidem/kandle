package ch.epfl.sdp.kandle.fragment;

import android.Manifest;
import android.content.IntentSender;
import android.os.Bundle;

import androidx.annotation.RequiresPermission;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.sdp.kandle.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);


        FusedLocationProviderClient loc = LocationServices.getFusedLocationProviderClient(getContext());

        TextView text = view.findViewById(R.id.placeholder);
        Button btn = view.findViewById(R.id.updateLocation);

        btn.setOnClickListener(v -> {
            loc.getLastLocation().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    text.setText(task.getResult().toString());
                } else {
                    Exception e = task.getException();
                    text.setText(e.getMessage());
                    if(task.getException() instanceof ResolvableApiException) {
                        ResolvableApiException ex = (ResolvableApiException) e;
                        try {
                            ex.startResolutionForResult(getActivity(), 1);
                        } catch (IntentSender.SendIntentException exc) {
                            Log.d("error: ", ex.getMessage());
                        }
                    }
                }
            });
        });

        return view;
    }




    private boolean isGooglePlayServicesAvailable() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this.getContext());
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d("Location Updates", "Google Play services is available.");
            return true;
        } else {
            return false;

        }
    }



    @Override
    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void onResume() {
        super.onResume();

        // Display the connection status

//        if (abstractLocation.getCurrentLocation() != null) {
//            Toast.makeText(this.getContext(), "GPS location was found!", Toast.LENGTH_SHORT).show();
//            LatLng latLng = new LatLng(abstractLocation.getCurrentLocation().getLatitude(), abstractLocation.getCurrentLocation().getLongitude());
//            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
//            //mMap.animateCamera(cameraUpdate);
//        } else {
//            Toast.makeText(this.getContext(), "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
//        }
//        abstractLocation.startLocationUpdates(this.getContext());
    }




}
