package mobitechs.cityriders;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import mobitechs.cityriders.SessionManger.SessionManager;
import mobitechs.cityriders.adapter.Rider_List_Adapter;
import mobitechs.cityriders.model.Rider_List_Items;

public class Rider_List extends AppCompatActivity implements View.OnClickListener {

//    private ProgressDialog progressDialog = null;
    public List<Rider_List_Items> riderListItems = new ArrayList<Rider_List_Items>();
    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;

    String adminCode;
    String isAdmin;

    View v;
    SessionManager sessionManager;
    private FirebaseDatabase database;
    private DatabaseReference myDbRef;

    FloatingActionButton riderListFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_list);

        sessionManager = new SessionManager(this);
        HashMap<String, String> typeOfUser = sessionManager.getActivationCode();
        adminCode = typeOfUser.get(SessionManager.KEY_ADMINCODE);
        isAdmin = typeOfUser.get(SessionManager.KEY_IS_ADMIN);

        riderListFab = (FloatingActionButton) findViewById(R.id.riderListFab);

//        riderListFab.setOnClickListener(this);
//        riderListFab.setVisibility(View.VISIBLE);
//
        if(isAdmin.equals("true")){
            riderListFab.setOnClickListener(this);
            riderListFab.setVisibility(View.VISIBLE);
        }else{
            riderListFab.setVisibility(View.GONE);
        }

        getList();
    }

    private void getList() {

        database = FirebaseDatabase.getInstance();
        myDbRef = database.getReference("rider_list");
        myDbRef.keepSynced(true);

        recyclerView = (RecyclerView) findViewById(R.id.riderListRecyclerView);
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.smoothScrollToPosition(0);
        reviewAdapter = new Rider_List_Adapter(riderListItems);
        recyclerView.setAdapter(reviewAdapter);

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Please Wait");
//        progressDialog.show();

        myDbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Rider_List_Items riderList_items = dataSnapshot.getValue(Rider_List_Items.class);
//                progressDialog.dismiss();
                riderListItems.add(riderList_items);
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Rider_List_Items riderList_items = dataSnapshot.getValue(Rider_List_Items.class);
//                progressDialog.dismiss();
                riderListItems.add(riderList_items);
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Rider_List_Items riderList_items = dataSnapshot.getValue(Rider_List_Items.class);
//                progressDialog.dismiss();
                riderListItems.add(riderList_items);
                reviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.riderListFab) {
            Intent addRider = new Intent(this, Add_Rider.class);
            addRider.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(addRider);

        }
    }
}