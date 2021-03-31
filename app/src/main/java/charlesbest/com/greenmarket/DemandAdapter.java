package charlesbest.com.greenmarket;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by BEN on 14/06/2018.
 */

public class DemandAdapter extends ArrayAdapter {

    Context context;
    ArrayList<DemandModel> data;
    int resource;
    ViewHolder holder;
    Direction direct;

    public DemandAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DemandModel> data) {
        super(context, resource, data);
        this.context = context;
        this.resource = resource;
        this.data = data;
        direct = (Direction)context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView==null){
            convertView = inflater.inflate(resource,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder= (ViewHolder)convertView.getTag();
        }

        final DemandModel productModel = data.get(position);
        holder.location.setText(productModel.getLocation());

        holder.title.setText(productModel.getDescription());
        holder.number.setText(productModel.getNumber());
        holder.num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                direct.direct(productModel.getNumber());

            }

        });
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                direct.Do(productModel.getSeller());
            }
        });
        return convertView;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    static class ViewHolder  {

        LinearLayout num,chat;
        TextView title,location,number;
        public ViewHolder(View itemView) {
            title = itemView.findViewById(R.id.dd);
            location = itemView.findViewById(R.id.location);
            num = itemView.findViewById(R.id.num);
            number = itemView.findViewById(R.id.pNum);
            chat = itemView.findViewById(R.id.chat);
        }
    }
    public interface Direction{

        void direct(String text);
        void Do (String id);
    }

}
