package charlesbest.com.greenmarket;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Vegetables extends Fragment {

    private FirebaseAuth auth;
    DatabaseReference mDatabase;
    View mProgressView;

    public Vegetables() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

       final RecyclerView recyclerView = view.findViewById(R.id.recycler);
        mProgressView = view.findViewById(R.id.login_progress);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {

            final ArrayList<ProductModel> list = new ArrayList<>();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
          final HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(list,getContext(),getActivity());
            recyclerView.setAdapter(adapter);
            mDatabase = FirebaseDatabase.getInstance().getReference("Products").child("Vegetables");
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        ProductModel value = dsp.getValue(ProductModel.class);
                        Toast.makeText(getContext(),value.getName(),Toast.LENGTH_LONG).show();
                        list.add(value);


                    }
                        adapter.notifyDataSetChanged();
                    mProgressView.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        return view;
    }


}
