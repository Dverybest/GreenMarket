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

public class Fruits extends Fragment {

    private FirebaseAuth auth;
    DatabaseReference mDatabase;
    View mProgressView;

    private OnFragmentInteractionListener mListener;

    public Fruits() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        mProgressView = view.findViewById(R.id.login_progress);

       final RecyclerView recyclerView = view.findViewById(R.id.recycler);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {

            final ArrayList<ProductModel> list = new ArrayList<>();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
          final HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(list,getContext(),getActivity());
            recyclerView.setAdapter(adapter);
            mDatabase = FirebaseDatabase.getInstance().getReference("Products").child("Fruit");
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

//                    list.add(new ProductModel("Apple","https://static.pexels.com/photos/7160/bird-trees-medium.jpg","£100","ifite awka","07061011343"));
                    /*list.add(new ProductModel("Yam", "https://static.pexels.com/photos/6036/nature-forest-tree-bark-medium.jpg","£100","ifite awka","07061011343"));
                    list.add(new ProductModel("Rice","https://static.pexels.com/photos/5618/fish-fried-mint-pepper-medium.jpg","£100","ifite awka","07061011343"));

*/

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
