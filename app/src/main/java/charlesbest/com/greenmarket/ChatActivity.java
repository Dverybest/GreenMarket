package charlesbest.com.greenmarket;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    ListView listView;
    EditText  editText;
    Button button;
    private FirebaseAuth auth;
    DatabaseReference mDatabase ;
    ArrayList<String> chats;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        button = findViewById(R.id.send);
        editText =findViewById(R.id.pChat);
        listView = findViewById(R.id.recycler);
        auth = FirebaseAuth.getInstance();
        chats = readChat();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,chats);
        listView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editText.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter message!", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ChatActivity.this);

                ChatModel chat = new ChatModel(editText.getText().toString());

                if (auth.getCurrentUser() != null) {

                    FirebaseUser users = auth.getCurrentUser();

                    String hold = getIntent().getStringExtra("receiver");
                    if(hold.contains(".")){
                        hold =  hold.replace(".","");
                    }
                    mDatabase = FirebaseDatabase.getInstance().getReference(hold);

                    mDatabase.child(users.getEmail().replace(".","")).push().setValue(chat);


                    mDatabase = FirebaseDatabase.getInstance().getReference(users.getEmail().replace(".",""));

                    mDatabase.child(hold).push().setValue(chat);
                    mDatabase.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            chats = readChat();
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                editText.setText("");

            }
        });

    }

    ArrayList readChat(){
        final ArrayList<String> chats = new ArrayList<>();
        if (auth.getCurrentUser() != null) {
            FirebaseUser users = auth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance().getReference(users.getEmail().replace(".",""));

        String hold = getIntent().getStringExtra("receiver");
        if(hold.contains(".")){
          hold =  hold.replace(".","");
        }
        mDatabase.child(hold).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.getChildren()) {

                    ChatModel chatModel = dsp.getValue(ChatModel.class);

                    chats.add(chatModel.getText());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        }
        return chats;
    }

}
