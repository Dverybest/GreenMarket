package charlesbest.com.greenmarket;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

public class PostDemandActivity extends AppCompatActivity {

    EditText title,description,location;
    StorageReference storageReference;
    DatabaseReference mDatabase ;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_demand);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = findViewById(R.id.pName);
        description = findViewById(R.id.pDescription);
        location = findViewById(R.id.pLocation);
        bar = findViewById(R.id.login_progress);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bar.setVisibility(View.VISIBLE);
                upLoadToDatabase();
            }
        });
    }

    void upLoadToDatabase(){

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String  seller = prefs.getString("email",null);
        String  number = prefs.getString("phone",null);

        mDatabase = FirebaseDatabase.getInstance().getReference("Demand");

        String key = mDatabase.push().getKey();
        if(key==null){
            key = seller+number;
        }
        DemandModel product = new DemandModel(title.getText().toString(),location.getText().toString(),number,key,seller,description.getText().toString());

        mDatabase.child(key).setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(this.getClass().getName(),e.getMessage());
                bar.setVisibility(View.GONE);
            }
        });
    }
}
