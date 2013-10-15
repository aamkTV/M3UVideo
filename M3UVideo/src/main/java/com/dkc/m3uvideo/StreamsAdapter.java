package com.dkc.m3uvideo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by barbarian on 15.10.13.
 */
public class StreamsAdapter extends ArrayAdapter<VideoStream> {
    int itemResourceId;


    public StreamsAdapter(Context context, int resource, List<VideoStream> objects) {
        super(context, resource, objects);
        itemResourceId=resource;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        StreamView holder=null;
        View v=convertView;
        if(v== null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(itemResourceId, null);
            holder=getViewHolder(v);

            if(holder!=null){
                v.setTag(holder);
            }
        }

        if(holder==null){
            holder = (StreamView)v.getTag();
        }
        VideoStream item= getItem(position);
        if(item!=null&&holder!=null){
            holder.titleView.setText(item.getTitle());

            Picasso.with(getContext())
                    .load(item.getPath())
                    .placeholder(R.drawable.movie_placeholder_symbol)
                    .fit()
                    .into(holder.imageView);
        }
        return v;
    }


    public StreamView getViewHolder(View parent) {
        StreamView viewHolder= new StreamView();

        viewHolder.imageView = (ImageView) parent.findViewById(R.id.logo);
        viewHolder.titleView = (TextView) parent.findViewById(R.id.title);
        return viewHolder;
    }

    private static class StreamView{
        public ImageView imageView;
        public TextView titleView;
    }


}
