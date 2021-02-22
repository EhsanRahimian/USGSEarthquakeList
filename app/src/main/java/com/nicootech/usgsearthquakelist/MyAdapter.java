package com.nicootech.usgsearthquakelist;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyAdapter extends ArrayAdapter<Earthquake> {


    public MyAdapter(@NonNull Context context, @NonNull List<Earthquake> objects) {
        super(context, 0,objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitem = convertView;
        if(convertView == null){
            listitem = LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
        }
        Earthquake pos = getItem(position);

        TextView mag = listitem.findViewById(R.id.mag);
        DecimalFormat formatter=new DecimalFormat("0.0");
        mag.setText(formatter.format(pos.getMag()));

        TextView location1 = listitem.findViewById(R.id.location);
        TextView location2 = listitem.findViewById(R.id.country);
        char ch=pos.getLocation().charAt(0);
        if(ch >= '0' && ch <= '9')
        {
            //',' is the character to separate the location 1 and 2 so if we see ',' we can fill to textView
            // one by geographical location and the other one by city or country
            location1.setText(pos.getLocation().substring(0,(pos.getLocation().indexOf(','))+1));
            location2.setText(pos.getLocation().substring(pos.getLocation().indexOf(',')+2));
        }
        else {
            // otherwise  we have only the city or country
            location1.setText("");
            location2.setText(pos.getLocation());
        }
        long timemilli = pos.getTime();
        Date object = new Date(timemilli);

        TextView date = listitem.findViewById(R.id.date);
        date.setText(date(object));

        TextView time = listitem.findViewById(R.id.time);
        time.setText(time(object));

        return listitem;
    }
    private String date(Date object)
    {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpledate=new SimpleDateFormat("MMM dd, yyyy");
        return simpledate.format(object);
    }
    private String time(Date object)
    {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpletime=new SimpleDateFormat("h:mm a");
        return simpletime.format(object);
    }
}
