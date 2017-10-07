package com.example.h3410.a13_googlemapsapi;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int MY_LOCATION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private JSONArray locations;
    private Polyline polyline;



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            } else {
                // Permission was denied. Display an error message.
            }
        }
    }

    // onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        FetchDataTask task = new FetchDataTask();
        task.execute("http://student.labranet.jamk.fi/~H3410/android-application-development/res/otot/otot.json");
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

        // check if user has allowed to use geolocation, if not ask for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOCATION_REQUEST_CODE);
        }

        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(this);

        // custom layout for marker info window, for showing multiline snippet
        // https://stackoverflow.com/questions/13904651/android-google-maps-v2-how-to-add-marker-with-multiline-snippet
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

                LinearLayout info = new LinearLayout(context);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(context);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(context);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        FetchRouteTask task = new FetchRouteTask();
        LatLng position = marker.getPosition();
        String destinationCoordinates = Double.toString(position.latitude) + "," + Double.valueOf(position.longitude);
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=62.2415844,25.7606907" + "&destination=" + destinationCoordinates + "&key=" + getString(R.string.google_locations_key);
        task.execute(url);
        return false;
    }

    public void initializeMarkers(boolean shouldMoveCamera) {
        mMap.clear();
        try {
            for (int i = 0; i < locations.length(); i++) {
                JSONObject location = locations.getJSONObject(i);

                StringBuilder stringbuilder = new StringBuilder();
                stringbuilder.append(location.getString("location_type")).append("\n")
                        .append(location.getString("address")).append("\n")
                        .append(location.getString("post_number")).append("\n")
                        .append(location.getString("city")).append("\n")
                        // additional info not found in most of the markers
                        .append(location.getString("open_additional_info")).append("\n");

                LatLng otto = new LatLng(Double.parseDouble(location.getString("lat")), Double.parseDouble(location.getString("lon")));
                mMap.addMarker(new MarkerOptions().position(otto)
                        .title(location.getString("location"))
                        .snippet(stringbuilder.toString()));

                if (shouldMoveCamera) mMap.moveCamera(CameraUpdateFactory.newLatLng(otto));


            }
        } catch (JSONException e) {

        }
    }


    class FetchDataTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            JSONObject json = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                json = new JSONObject(stringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
            }
            return json;
        }

        protected void onPostExecute(JSONObject json) {
            StringBuffer text = new StringBuffer("");
            try {
                // store highscores
                locations = json.getJSONArray("locations");
                initializeMarkers(true);

            } catch (JSONException e) {
                Log.e("JSON", "Error getting data.");
            }

        }
    }

    class FetchRouteTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            JSONObject json = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                json = new JSONObject(stringBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) urlConnection.disconnect();
            }
            return json;
        }

        protected void onPostExecute(JSONObject json) {
            StringBuffer text = new StringBuffer("");
            List<LatLng> positions = new ArrayList<>();
            if (polyline != null) polyline.remove();
            int fullDistance = 0;

            try {
                JSONArray routes = json.getJSONArray("routes");

                for (int i = 0; i < routes.length(); i++) {
                    JSONObject route = routes.getJSONObject(i);
                    JSONArray legs = route.getJSONArray("legs");

                    for (int j = 0; j < legs.length(); j++) {
                        JSONObject leg = legs.getJSONObject(i);
                        JSONArray steps = leg.getJSONArray("steps");

                        for (int k = 0; k < steps.length(); k++) {
                            JSONObject step = steps.getJSONObject(k);
                            Log.e("step", step.toString());
                            JSONObject startLocation = step.getJSONObject("start_location");
                            JSONObject endLocation = step.getJSONObject("end_location");
                            LatLng start = new LatLng(Double.valueOf(startLocation.getString("lat")), Double.valueOf(startLocation.getString("lng")));
                            LatLng end = new LatLng(Double.valueOf(endLocation.getString("lat")), Double.valueOf(endLocation.getString("lng")));

                            positions.add(start);
                            positions.add(end);

                            fullDistance += step.getJSONObject("distance").getInt("value");
                        }
                    }
                }

                polyline = mMap.addPolyline(new PolylineOptions().addAll(positions).width(10).color(Color.RED).visible(true).clickable(true));
                Toast.makeText(getApplicationContext(), "Distance " + String.valueOf(fullDistance) + " meters", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                Log.e("FetchRoutes", "Error in JSON parsing");

            }


        }
    }
}
