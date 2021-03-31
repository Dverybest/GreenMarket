package charlesbest.com.greenmarket;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewDemandActivity extends AppCompatActivity  implements  DemandAdapter.Direction{

    private FirebaseAuth auth;
    DatabaseReference mDatabase;
    ListView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_demand);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recycler);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {

            final ArrayList<DemandModel> list = new ArrayList<>();
            final DemandAdapter adapter = new DemandAdapter(this,R.layout.view_demand,list);
            recyclerView.setAdapter(adapter);

            mDatabase = FirebaseDatabase.getInstance().getReference("Demand");
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        DemandModel value = dsp.getValue(DemandModel.class);
                        Toast.makeText(ViewDemandActivity.this,value.getName(),Toast.LENGTH_LONG).show();
                        list.add(value);

                    }
                    adapter.notifyDataSetChanged();

                   
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


    }


    @Override
    public void direct(String text) {
        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + text ));
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            startActivity(intent);

        }else{
            String [] permission = { android.Manifest.permission.CALL_PHONE};
            ActivityCompat.requestPermissions(this,permission,1);
            startActivity(intent);
        }

    }

    @Override
    public void Do(String id) {
        Intent intent = new Intent(this,ChatActivity.class);
        intent.putExtra("receiver",id);
        startActivity(intent);
    }
}
