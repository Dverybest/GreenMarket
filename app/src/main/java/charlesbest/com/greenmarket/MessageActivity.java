package charlesbest.com.greenmarket;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    DatabaseReference mDatabase ;
    ArrayList<String> chat;
    ListView listView;
    View mProgressView;
     ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        auth = FirebaseAuth.getInstance();
        listView = findViewById(R.id.recycler);
        mProgressView = findViewById(R.id.login_progress);
        chat = readChat();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MessageActivity.this,ChatActivity.class);
                intent.putExtra("receiver",chat.get(i));
                startActivity(intent);
            }
        });
         adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,chat);
        listView.setAdapter(adapter);

    }

    ArrayList<String> readChat(){
        final ArrayList<String> me = new ArrayList<>();

        if (auth.getCurrentUser() != null) {
            FirebaseUser users = auth.getCurrentUser();
            mDatabase = FirebaseDatabase.getInstance()
                    .getReference( users.getEmail().replace(".",""));
                  mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                        me.add(dsp.getKey());
                        Toast.makeText(MessageActivity.this,dsp.getKey(),Toast.LENGTH_SHORT).show();
                    }
                    adapter.notifyDataSetChanged();
                    mProgressView.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        return me;
    }

}
