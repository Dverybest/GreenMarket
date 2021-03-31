package charlesbest.com.greenmarket;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by BEN on 14/06/2018.
 */

public class HomeViewPagerAdapter extends RecyclerView.Adapter<HomeViewPagerAdapter.ViewHolder> {

    ArrayList<ProductModel> list ;
    Context context;
    Activity home;

    public HomeViewPagerAdapter(ArrayList<ProductModel> list, Context context, Activity home) {
        this.list = list;
        this.context = context;
        this.home = home;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_view,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ProductModel productModel = list.get(position);
        holder.location.setText(productModel.getLocation());
        holder.price.setText(productModel.getPrice());
        holder.title.setText(productModel.getName());
        Glide.with(context).load(productModel.getImage())
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);
        holder.price.setText("#"+productModel.getPrice());
        holder.title.setText(productModel.getName());
        holder.num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.number.getText().toString().contains("Show phone")){
                    holder.number.setText(productModel.getNumber());
                }else {
                    Intent intent = new Intent(Intent.ACTION_CALL);

                    intent.setData(Uri.parse("tel:" + productModel.getNumber() ));
                    if (ActivityCompat.checkSelfPermission(context,
                            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                        context.startActivity(intent);

                    }else{
                        String [] permission = { android.Manifest.permission.CALL_PHONE};
                        ActivityCompat.requestPermissions(home,permission,1);
                        context.startActivity(intent);
                    }
                }
            }

        });
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(home,ChatActivity.class);
                intent.putExtra("receiver",productModel.getSeller());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        LinearLayout num,chat;
        TextView title,price,location,number;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imga);
            title = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.price);
            location = itemView.findViewById(R.id.location);
            num = itemView.findViewById(R.id.num);
            chat = itemView.findViewById(R.id.chat);
            number = itemView.findViewById(R.id.pNum);

        }

    }

    public interface OnFragmentInteractionListener {

        void onInteraction(String value);
    }

}
