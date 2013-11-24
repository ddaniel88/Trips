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
 
public class CheckinListAdapter extends ArrayAdapter<String> {
	private final Context context;
	//private final String[] slike;
	private final String[] checkins;
	private final String[] dates;
	private TextView checkin;
	private TextView date;
	
	List<TextView> checkin_list = new ArrayList<TextView>();
	List<TextView> date_list = new ArrayList<TextView>();
	
	
	 /* String[] slike */ 
	public CheckinListAdapter(Context context, String [] checkins,  String dates[]) {
		super(context, R.layout.listitem, checkins);
		this.context = context;
		//this.slike = slike;
		this.checkins = checkins;
		this.dates = dates;
	}
	
 
	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.checkin_listitem,null);
		
		checkin = (TextView) rowView.findViewById(R.id.checkin);
		date = (TextView) rowView.findViewById(R.id.checkin_date);
		
		checkin.setId(position);
		checkin_list.add(checkin);
		
		date.setId(position);
		date_list.add(date);
		
		checkin.setTextColor(Color.BLACK);
		checkin.setText(checkins[position]);
		date.setTextColor(Color.BLACK);
		date.setText("Date: " + dates[position]);
		 

		return rowView;
	}
	
}
