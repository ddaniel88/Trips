package com.example.crsautomotive;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
 
public class TripListAdapter extends ArrayAdapter<String> {
	private final Context context;
	//private final String[] slike;
	private final String[] trips;
	private final String[] dates;
	private final String[] end_dates;
	private final String[] distances;
	private final Drawable[] more;
	private TextView name;
	private TextView date;
	private TextView distance;
	private ImageView image;
	
	List<TextView> trip = new ArrayList<TextView>();
	List<TextView> date_list = new ArrayList<TextView>();
	List<TextView> distance_lsit = new ArrayList<TextView>();
	
	
	 /* String[] slike */ 
	public TripListAdapter(Context context, String [] trips,  String dates[], String[] end_dates,String [] distances, Drawable[] more) {
		super(context, R.layout.listitem, trips);
		this.context = context;
		//this.slike = slike;
		this.trips = trips;
		this.dates = dates;
		this.end_dates = end_dates;
		this.distances = distances;
		this.more = more;
	}
	
 
	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.listitem,null);
		
		name = (TextView) rowView.findViewById(R.id.texttrip);
		date = (TextView) rowView.findViewById(R.id.date);
		distance = (TextView) rowView.findViewById(R.id.distance);
		
		name.setId(position);
		trip.add(name);
		
		date.setId(position);
		date_list.add(date);
		
		distance.setId(position);
		distance_lsit.add(distance);
		
		name.setTextColor(Color.BLACK);
		name.setText(trips[position]);
		date.setTextColor(Color.BLACK);
		date.setText("Date: " + dates[position] + "-" + end_dates[position]);
		distance.setTextColor(Color.BLACK);
		distance.setText("Traveled distance: " + distances[position] + " km");
		
		//Image
		image = (ImageView)rowView.findViewById(R.id.next);
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		 
	      
		  int width = display.getWidth()/10;
		  int height = (int)(width);
		  
		  LinearLayout.LayoutParams vp = 
			        new LinearLayout.LayoutParams(width,height);
		  
		  LinearLayout lln = (LinearLayout)rowView.findViewById(R.id.layout_next);
		  lln.setGravity(Gravity.CENTER);
		   image.setLayoutParams(vp); 
		   
	      image.setBackgroundDrawable(more[position]);
	       
		 

		return rowView;
	}
	
}
