package mobitechs.cityriders.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mobitechs.cityriders.Rider_List;
import mobitechs.cityriders.SessionManger.SessionManager;
import mobitechs.cityriders.Upcoming_Riding_List;
import mobitechs.cityriders.firebase.GoogleMapUtil;
import mobitechs.cityriders.Map;
import mobitechs.cityriders.R;
import mobitechs.cityriders.model.Riding_Event_Items;

public class UpcomingRide_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Riding_Event_Items> listItems;
    View v;
    RecyclerView.ViewHolder viewHolder;
    private FirebaseDatabase database;
    private DatabaseReference myDbRef;
    SessionManager sessionManager;

    public UpcomingRide_Adapter(List<Riding_Event_Items> items) {
        this.listItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
       
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.upcoming_riding_list_items, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            ViewHolder vHolder = (ViewHolder) viewHolder;
            Riding_Event_Items itemOflist = listItems.get(position);
            vHolder.bindListDetails(itemOflist);
        }
    }

    @Override
    public int getItemCount() {
       
        return listItems.size();
    }

//    public class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback, DirectionCallback,View.OnClickListener, RoutingListener, GoogleMap.OnMapClickListener {
    public class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback,View.OnClickListener, RoutingListener, GoogleMap.OnMapClickListener {


        public TextView txtRidingDate;
        public TextView txtNote,lblNote;
        public TextView txtMeetUpPlace;
        public TextView txtRidingPlace;
        public MapView mapView;
        public View cardView;
        public LatLng ridingLocation;

        private List<Marker> markers = new ArrayList<Marker>();
        Marker marker,marker2;
        LatLng destination;
        LatLng origin;
        String adminCode,isAdmin;
        RelativeLayout deleteListLayout,mainRelativeLayout;
        String serverKey = "AIzaSyBnIAqzNEjw_ZJjqsRenVctNQMrIwHtAJw";

        protected GoogleMap mGoogleMap;

        Riding_Event_Items listItem = new Riding_Event_Items();

        public ViewHolder(View itemView) {
            super(itemView);

            sessionManager = new SessionManager(v.getContext());
            HashMap<String, String> typeOfUser = sessionManager.getActivationCode();
            adminCode = typeOfUser.get(SessionManager.KEY_ADMINCODE);
            isAdmin = typeOfUser.get(SessionManager.KEY_IS_ADMIN);

            txtRidingDate = (TextView) itemView.findViewById(R.id.txtRidingDate);
            txtNote = (TextView) itemView.findViewById(R.id.txtNote);
            lblNote = (TextView) itemView.findViewById(R.id.lblNote);
            txtMeetUpPlace = (TextView) itemView.findViewById(R.id.txtMeetUpPlace);
            txtRidingPlace = (TextView) itemView.findViewById(R.id.txtRidingPlace);
            mapView = (MapView) itemView.findViewById(R.id.ridingMap);

            deleteListLayout = (RelativeLayout) itemView.findViewById(R.id.deleteListLayout);
            mainRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.mainRelativeLayout);

            mapView.onCreate(null);
            mapView.getMapAsync(this);
            cardView = itemView;
            cardView.setOnClickListener(this);
            mainRelativeLayout.setOnClickListener(this);

            txtNote.setVisibility(View.GONE);
            lblNote.setVisibility(View.GONE);

            if(isAdmin.equals("true")){
                deleteListLayout.setOnClickListener(this);
                deleteListLayout.setVisibility(View.VISIBLE);
            }else{
                deleteListLayout.setVisibility(View.GONE);
            }

            database = FirebaseDatabase.getInstance();
            myDbRef = database.getReference("rider_list");
        }

        public void bindListDetails(Riding_Event_Items listItem) {
            this.listItem = listItem;

            destination = new LatLng(listItem.ridingLat, listItem.ridingLongi);
            origin = new LatLng(listItem.meetUpLat, listItem.meetUpLongi);

//            Handler handler1 = new Handler();
//            handler1.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    drawSourceToDestination();
//                }
//            }, 1000);

            txtRidingDate.setText(listItem.RidingDate);
            txtRidingPlace.setText(listItem.RidingPlace);
            txtMeetUpPlace.setText(listItem.MeetUpPlace);

            if(listItem.Note != null) {
                txtNote.setText(listItem.Note);
                txtNote.setVisibility(View.VISIBLE);
                lblNote.setVisibility(View.VISIBLE);
            }
            ridingLocation = new LatLng(listItem.ridingLat, listItem.ridingLongi);
        }


        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;

            MapsInitializer.initialize(v.getContext());
            googleMap.getUiSettings().setMapToolbarEnabled(false);

            // If we have map data, update the map content.
            if (ridingLocation != null) {
                updateMapContents();
            }

            mGoogleMap.setOnMapClickListener(this);
        }

        protected void updateMapContents() {
            // Since the mapView is re-used, need to remove pre-existing mapView features.
            mGoogleMap.clear();

            // Update the mapView feature data and camera position.
            mGoogleMap.addMarker(new MarkerOptions().position(ridingLocation));

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ridingLocation, 10f);
            mGoogleMap.moveCamera(cameraUpdate);
        }

//        public void drawSourceToDestination() {
//
////            GoogleDirection.withServerKey(serverKey)
////                    .from(origin)
////                    .to(destination)
////                    .transportMode(TransportMode.DRIVING)
////                    .execute(this);
//
//            Routing routing = new Routing.Builder()
//                    .travelMode(Routing.TravelMode.DRIVING)
//                    .withListener(this)
//                    .waypoints(origin, destination)
//                    .build();
//            routing.execute();
//        }
//        @Override
//        public void onDirectionSuccess(Direction direction, String s) {
//            if (direction.isOK()) {
//                markers.add(mGoogleMap.addMarker(new MarkerOptions().position(origin)));
//                markers.add(mGoogleMap.addMarker(new MarkerOptions().position(destination)));
//
//                GoogleMapUtil.fixZoomForMarkers(mGoogleMap, markers);
//
//                ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
//                mGoogleMap.addPolyline(DirectionConverter.createPolyline(v.getContext(), directionPositionList, 5, Color.RED));
//            }
//        }

//        @Override
//        public void onDirectionFailure(Throwable throwable) {
//
//        }

        @Override
        public void onRoutingFailure(RouteException e) {
            if(e != null) {
                Toast.makeText(v.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(v.getContext(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onRoutingStart() {

        }

        @Override
        public void onRoutingSuccess(ArrayList<Route> routeList, int shortestRouteIndex) {
            markers.add(mGoogleMap.addMarker(new MarkerOptions().position(origin)));
            markers.add(mGoogleMap.addMarker(new MarkerOptions().position(destination)));

            GoogleMapUtil.fixZoomForMarkers(mGoogleMap, markers);

            for (int i = 0; i <routeList.size(); i++) {

                PolylineOptions polyOptions = new PolylineOptions();
                polyOptions.color(v.getResources().getColor(R.color.colorred));
                polyOptions.width(10 + i * 3);
                polyOptions.addAll(routeList.get(i).getPoints());
                mGoogleMap.addPolyline(polyOptions);
            }
        }

        @Override
        public void onRoutingCancelled() {

        }

        @Override
        public void onClick(final View v) {
            if (v.getId() == R.id.mainRelativeLayout) {
                openMap(v);
            }
            else if (v.getId() == R.id.deleteListLayout) {
                AlertDialog.Builder alertbox = new AlertDialog.Builder(v.getRootView().getContext());
                alertbox.setMessage("Do you want to delete this Ride?");
                alertbox.setTitle("Delete");

                alertbox.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listItem = listItems.get(getAdapterPosition());
                                String RidingDate = listItem.RidingDate;
                                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                Query query = ref.child("riding_event").orderByChild("RidingDate").equalTo(RidingDate);

                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                                            snapshot.getRef().removeValue();
                                        }
                                        listItems.remove(getAdapterPosition());
                                        notifyItemRemoved(getAdapterPosition());
                                        notifyItemRangeChanged(getAdapterPosition(), listItems.size());

                                        Intent refresh = new Intent(v.getContext(), Upcoming_Riding_List.class);
                                        refresh.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        v.getContext().startActivity(refresh);
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e("", "onCancelled", databaseError.toException());
                                    }
                                });
                            }
                        });
                alertbox.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertbox.show();
            }
        }

        public void openMap(View v) {
            Intent petFullInformation = new Intent(v.getContext(), Map.class);

            petFullInformation.putExtra("meetUpLat", listItem.meetUpLat);
            petFullInformation.putExtra("meetUpLong", listItem.meetUpLongi);
            petFullInformation.putExtra("ridingLat", listItem.ridingLat);
            petFullInformation.putExtra("ridingLong", listItem.ridingLongi);
            petFullInformation.putExtra("meetUpPlace", listItem.MeetUpPlace);
            petFullInformation.putExtra("ridingPlace", listItem.RidingPlace);

            v.getContext().startActivity(petFullInformation);
        }

        @Override
        public void onMapClick(LatLng latLng) {
            openMap(v);
        }
    }
}