package mobitechs.cityriders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import mobitechs.cityriders.firebase.GoogleMapUtil;

public class Map extends FragmentActivity implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback,DirectionCallback {

    double meetUpLat,meetUpLong;
    double ridingLat,ridingLong;


    String meetUpPlace,ridingPlace;
    Marker locationMarker;
    LatLng destination;
    LatLng origin;
    String serverKey = "AIzaSyBnIAqzNEjw_ZJjqsRenVctNQMrIwHtAJw";
    protected GoogleMap mGoogleMap;
    private List<Marker> markers = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.map);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        if (null != intent) {
            meetUpLat = intent.getDoubleExtra("meetUpLat",0);
            meetUpLong = intent.getDoubleExtra("meetUpLong",0);
            ridingLat = intent.getDoubleExtra("ridingLat",0);
            ridingLong = intent.getDoubleExtra("ridingLong",0);
            meetUpPlace = intent.getStringExtra("meetUpPlace");
            ridingPlace = intent.getStringExtra("ridingPlace");
        }

        destination = new LatLng(ridingLat, ridingLong);
        origin = new LatLng(meetUpLat, meetUpLong);
        //drawSourceToDestinarion();
    }

    private void drawSourceToDestinarion() {
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.WALKING)
                .execute(this);
    }

    // Include the OnCreate() method here too, as described above.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        drawSourceToDestinarion();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        // Retrieve the data from the marker.
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        if (direction.isOK()) {
            markers.add(mGoogleMap.addMarker(new MarkerOptions().position(origin)));
            markers.add(mGoogleMap.addMarker(new MarkerOptions().position(destination)));

            GoogleMapUtil.fixZoomForMarkers(mGoogleMap, markers);

            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            mGoogleMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED));
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {

    }
}