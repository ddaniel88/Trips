package com.example.crsautomotive;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TripDetails extends ListActivity implements OnClickListener{

	private ArrayAdapter<String> adapter;
	private List<CheckIn> all_checkins;
	private DataBaseHandler db; 
	private int tripID;
	private static TextView textDistance;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.activity_trip_details);
	    
	    View btn=findViewById(R.id.layout_trip);
        btn.setOnClickListener(this);
        btn=findViewById(R.id.layout_audio);
        btn.setOnClickListener(this);
        btn=findViewById(R.id.layout_cameras);
        btn.setOnClickListener(this);
        
        LinearLayout ll = (LinearLayout) findViewById(R.id.layout_trip);
        ll.setBackgroundResource(R.drawable.narandzasto);
        
        Intent intent = getIntent();
        tripID = intent.getIntExtra("tripID", 0);
        
        db = new DataBaseHandler(getApplicationContext());
        //all_checkins =  db.getAllCheckIns();
       // all_checkins = db.getTripCheckIns(tripID);
        LoadListData();
       
		adapter.notifyDataSetChanged();
		setListAdapter(adapter); 
		
		//set active trip info
		Trip finished_trip = db.getTrip(tripID);
		
		TextView start_date = (TextView) findViewById(R.id.active_date);
		start_date.setText(finished_trip.getSTARTDATE());
		
		SetDistanceValue();
		String tmp = db.getTotalTimeForTrip(tripID);
		SetTotalTime(tmp);
		db.close();
	}
	
	public int getTripId() {
		return this.tripID;
	}
	
	//Total time
	private void SetTotalTime(String totaltime)
	{
//		DateFormat df = DateFormat.getDateTimeInstance();
//		Date dateOne = null;
//		Date dateTwo = null;
//		try {
//			dateOne = df.parse(all_checkins.get(all_checkins.size()-1).get_date());
//			dateTwo = df.parse(all_checkins.get(0).get_date());
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		
//		long time = dateTwo.getTime() - dateOne.getTime();
//		long diffSeconds = time / 1000 % 60;
//		long diffMinutes = time / (60 * 1000) % 60;
//		long diffHours = time / (60 * 60 * 1000) % 24;
//		long diffDays = time / (24 * 60 * 60 * 1000);
//	    
//		Log.d("asdas","difference:" + time);
//		String daysText = diffDays > 1 ? " days " : " day ";
//		
//		String totaltime = diffDays + daysText + diffHours + ":" + diffMinutes + ":" + diffSeconds;
//		
		TextView text_time = (TextView) findViewById(R.id.complete_time);
		text_time.setText(totaltime);
	}
	
	//Set Distance value
	
	private void SetDistanceValue()
	{
		GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
		
		int distanceFromDb = TripHelper.getTripTotalDistance(tripID, getApplicationContext());
		if (distanceFromDb == -1) {
			gpsTracker.getTotalDistancne(all_checkins, null, this);
		}
		else {
			SetTotalDistanceTextToTextView(distanceFromDb);
		}
	}
	
	public void SetTotalDistanceTextToTextView(int totalDistance) {
		textDistance = (TextView) findViewById(R.id.complete_distance);
		if (totalDistance == -1) {
			textDistance.setText("N/A");
		}
		else {
			textDistance.setText(String.valueOf(totalDistance / 1000) + " km, " + String.valueOf(totalDistance % 1000) + " m");
		}
	}
	
	// Load list items
		private void LoadListData() {
			
//			all_checkins =  db.getAllCheckIns();
//			all_checkins = db.getTripCheckIns(tripID);
			all_checkins = CheckInHelper.getCheckInsForTrip(tripID, getApplicationContext());
			adapter = new CheckinListAdapter(getApplicationContext(), readAllCheckIns(), readAllCheckInDates());
			setListAdapter(adapter);

			ListView listView = getListView();
			listView.setTextFilterEnabled(true);
			db.close();
		}
		
		//Get All check ins
		private String[] readAllCheckIns() 
		{
			String[] tmp = new String[all_checkins.size()];
			for(int i=0; i<all_checkins.size(); i++)
			{
				tmp[i] = all_checkins.get(i).get_address();
			}
			return tmp;
		}

		
		//Get All check ins
		private String[] readAllCheckInDates() 
		{
			String[] tmp = new String[all_checkins.size()];
			for(int i=0; i<all_checkins.size(); i++)
			{
				tmp[i] = all_checkins.get(i).get_date();
			}
			return tmp;
		}


	@Override
	public void onClick(View v) {
		Intent i;
		switch (v.getId()) {
				
		case R.id.layout_audio:
			i = new Intent(getApplicationContext(), AudioNews.class);
			startActivity(i);
			finish();
			break;
			
		case R.id.layout_cameras:
			i = new Intent(getApplicationContext(), Cameras.class);
			startActivity(i);
			finish();
			break;

		default:
			break;			
		}
		
	}

}
