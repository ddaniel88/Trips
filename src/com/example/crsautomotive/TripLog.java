package com.example.crsautomotive;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class TripLog extends ListActivity implements OnClickListener {

	ListView trip_list;
	RadioButton radioBuisiness, radioPrivate, tmp;
	EditText tripName;
	String get_tip_name = null;
	String get_trip_type = null;
	DataBaseHandler db = new DataBaseHandler(this);
	int trip_count = 1;
	String trip_name = null;
	Drawable[] more;
	ArrayAdapter<String> adapter;
	int pos;
	AlertDialog dialog;
	String csv_filepath = null;
	private static final int PICKFILE_RESULT_CODE = 1;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_trip_log);

		// Reading all contacts
		Log.d("Reading: ", "Reading all contacts..");
		LoadListData();

		// trip_list = (ListView) findViewById(R.id.trip_list);

		// ListAdapter ad = new
		// ArrayAdapter<String>(this,android.R.layout.simple_list_item_1
		// ,readTripNames(all_trips));
		// trip_list.setAdapter(ad);
		// setTitle("Trip Log");

		View btn = findViewById(R.id.layout_trip);
		btn.setOnClickListener(this);
		btn = findViewById(R.id.layout_audio);
		btn.setOnClickListener(this);
		btn = findViewById(R.id.layout_cameras);
		btn.setOnClickListener(this);

		LinearLayout ll = (LinearLayout) findViewById(R.id.layout_trip);
		ll.setBackgroundResource(R.drawable.narandzasto);
		
//		  ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
//          NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
//          if (mWifi.isConnected()) 
//          {
//              Toast.makeText(getApplicationContext(), "wi-fi is turned on", Toast.LENGTH_SHORT).show();
//          }

		this.getListView().setLongClickable(true);
		this.getListView().setOnItemLongClickListener(
				new OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> parent,
							View v, int position, long id) {
						// Do some
						setPos(position);
						final View inflator = View.inflate(TripLog.this,
								R.layout.change_name_dialog, null);
						final EditText name = (EditText) inflator
								.findViewById(R.id.trip_name);

						AlertDialog.Builder builder;
						if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
						{
							builder = new AlertDialog.Builder(TripLog.this, dialog.THEME_DEVICE_DEFAULT_DARK);
						}
						else
						{
							builder =  new AlertDialog.Builder(TripLog.this);
						}
						builder.setTitle("Trip name")
								.setView(inflator)
								.setCancelable(false)
								.setPositiveButton("Confirm",
										new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int id) {
												ChangeTripName(name.getText().toString(), getPos());
											}
										})
								.setNegativeButton("Cancel",
										new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int id) {
												dialog.cancel();
											}
										})
								.setOnKeyListener(new OnKeyListener() {
									@Override
									public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
										if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
											dialog.dismiss();
										}
										return false;
									}
								});

						dialog = builder.create();
						name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
							@Override
							public void onFocusChange(View v, boolean hasFocus) {
								if (hasFocus) {
									dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
								}
							}
						});
						dialog.show();

						return true;
					}
				});

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
		List<Trip> all_trips = db.getAllTrips();
		int new_id = (int) id;
		Intent i;
		if(all_trips.get(new_id).getCOMPLETE() == 0)
		{
			i = new Intent(getApplicationContext(), ActiveTrip.class);
		}
		else
		{
			i = new Intent(getApplicationContext(), TripDetails.class);
		}
		i.putExtra("tripID", all_trips.get(position).getID());
		startActivity(i);
		
		//finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true);
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		// android.os.Process.killProcess(android.os.Process.myPid());

		moveTaskToBack(true);
	}

	private void setPos(int v) {
		this.pos = v;
	}

	private int getPos() {
		return pos;
	}

	private void ChangeTripName(String name, int id) {
		List<Trip> all_trips = db.getAllTrips();
		String[] trips = readTripNames(all_trips);
		trips[id] = name;

		// insert data into database and change adapter
		adapter = new TripListAdapter(getApplicationContext(), trips,
				readTripStartDates(all_trips), readTripEndDates(all_trips),
				readTripDistance(all_trips), more);
		adapter.notifyDataSetChanged();
		setListAdapter(adapter);
		db.updateTripName(all_trips.get(id).getID(), name);
	}

	// Load list items
	private void LoadListData() {
		List<Trip> all_trips = db.getAllTrips();
		readAllImagesForNextButton();

		adapter = new TripListAdapter(getApplicationContext(),
				readTripNames(all_trips), readTripStartDates(all_trips),
				readTripEndDates(all_trips), readTripDistance(all_trips), more);
		adapter.notifyDataSetChanged();
		setListAdapter(adapter);

		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
	}

	private void readAllImagesForNextButton() {
		int length = db.getNumberOfTrips();
		more = new Drawable[length];
		for (int i = 0; i < length; i++) {
			more[i] = getResources().getDrawable(R.drawable.more);
		}
	}

	private String[] readTripNames(List<Trip> all_trips) {
		String[] tmp_list = new String[all_trips.size()]; // u f-ji get_names
		for (int i = 0; i < all_trips.size(); i++) {
			tmp_list[i] = all_trips.get(i).getNAME();
		}
		return tmp_list;
	}

	private String[] readTripStartDates(List<Trip> all_trips) {
		String[] tmp_list = new String[all_trips.size()]; // u f-ji get_names
		for (int i = 0; i < all_trips.size(); i++) {
			tmp_list[i] = all_trips.get(i).getSTARTDATE().split(" ")[0]; // get
																			// ondly
																			// date
		}
		return tmp_list;
	}

	private String[] readTripEndDates(List<Trip> all_trips) {
		String[] tmp_list = new String[all_trips.size()]; // u f-ji get_names
		for (int i = 0; i < all_trips.size(); i++) {
			if (all_trips.get(i).getENDDATE() != null)
				tmp_list[i] = all_trips.get(i).getENDDATE().split(" ")[0]; // get
																			// only
																			// date
		}
		return tmp_list;
	}

	private String[] readTripDistance(List<Trip> all_trips) {
		String[] tmp_list = new String[all_trips.size()]; // u f-ji get_names
		for (int i = 0; i < all_trips.size(); i++) {
			tmp_list[i] = String.valueOf(all_trips.get(i).getDISTANCE());
		}
		return tmp_list;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

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

	public void OnNewTripClick(View v) {

		final View inflator = View.inflate(this, R.layout.trip_dialog, null);
		final RadioGroup group = (RadioGroup) inflator
				.findViewById(R.id.radioType);
		
		AlertDialog.Builder builder;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		{
			builder = new AlertDialog.Builder(TripLog.this, dialog.THEME_DEVICE_DEFAULT_DARK);
		}
		else
		{
			builder =  new AlertDialog.Builder(TripLog.this);
		}
		builder.setTitle("New Trip")
				.setView(inflator)
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								int selectedId = group.getCheckedRadioButtonId();

								tmp = (RadioButton) inflator.findViewById(selectedId);

								get_trip_type = tmp.getText().toString();
								String currentDateAndTime = DateFormat.getDateTimeInstance().format(new Date());

								trip_name = TripHelper.createNewTrip(get_trip_type, null, getApplicationContext(), currentDateAndTime);
								if (trip_name != null) {
									LoadListData();
									trip_name = null;
								}
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		AlertDialog dialog = builder.create();
		dialog.show();
	}
	
	public void OnExportClick(View v) throws IOException 
	{
		DataBaseHandler dbhelper = new DataBaseHandler(getApplicationContext());
		File exportDir = new File(Environment.getExternalStorageDirectory(), "/Trip-planner");
		File phoneDir = new File(Environment.getExternalStorageDirectory(), "/Trip-planner/"+android.os.Build.MODEL);
	    if(!exportDir.exists()) 
        {
            exportDir.mkdirs();
            if(!phoneDir.exists()) 
	        {
	            phoneDir.mkdirs();
	        }
        }
        else if(!phoneDir.exists()) 
        {
            phoneDir.mkdirs();
        }
		 File file = new File(phoneDir, "data.csv");
		 try 
	        {
	            file.createNewFile();                
	            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));
	            SQLiteDatabase db = dbhelper.getReadableDatabase();
	            Cursor curCSV = db.rawQuery("SELECT * FROM Trip",null);
	            curCSV.getColumnNames();
	            Cursor curCSV2 = null;
	            int trip_id_helper = 1;
	            int checkin_id_helper = 1;
	            String[] columns = getResources().getStringArray(R.array.csv_columns);
	            csvWrite.writeNext(columns);
	            while(curCSV.moveToNext())
	            {
	               //Which column you want to exprort
	                String arrStr[] ={
	                		//curCSV.getString(0), //tripID
	                		String.valueOf(trip_id_helper),
	                		curCSV.getString(1),
	                		curCSV.getString(2),
	                		curCSV.getString(3),
	                		curCSV.getString(4),
	                		curCSV.getString(5),
	                		curCSV.getString(6),
	                		curCSV.getString(7),
	                		curCSV.getString(8),
	                		curCSV.getString(9)
	                };
	                
	               // csvWrite.writeNext(arrStr);
	               curCSV2 = db.rawQuery("SELECT  ci.*, t.StartDate, t.TotalDistance, t.TotalTime FROM Trip t inner join" +
	                		" CheckIn ci on t.TripID = ci.TripID where t.TripID = " + curCSV.getString(0) + " order by ci.Date desc",null);
		           curCSV2.getColumnNames();
		            while(curCSV2.moveToNext())
		            {
		               //Which column you want to exprort
		                String arrStr2[] ={
		                		//curCSV2.getString(0), //CheckinId
		                		//curCSV2.getString(1), //TripID
		                		String.valueOf(checkin_id_helper),
		                		curCSV2.getString(2),
		                		curCSV2.getString(3),
		                		curCSV2.getString(4),
		                		curCSV2.getString(5),
		                		curCSV2.getString(6)
//		                		curCSV2.getString(7),
//		                		curCSV2.getString(8),
//		                		curCSV2.getString(9)
		                };
		               
		                csvWrite.writeNext(ConcatenateArrays(arrStr,arrStr2));
		                checkin_id_helper++;
		                
		            }
		            trip_id_helper++;
		            checkin_id_helper = 1;
	            }
	            csvWrite.close();
	            curCSV.close();
	            curCSV2.close();
	            db.close();
	    }
		catch(Exception sqlEx)
        {
            Log.e("ExportTripActivity", sqlEx.getMessage(), sqlEx);
        }
	}
	
	
	public void OnImportClick(View v) throws IOException 
	{
		 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
         intent.setType("text/csv");
         startActivityForResult(intent,PICKFILE_RESULT_CODE);

	}
	@Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	  // TODO Auto-generated method stub
		  switch(requestCode){
		  case PICKFILE_RESULT_CODE:
		   if(resultCode==RESULT_OK)
		   {
			   try{
			   csv_filepath = data.getData().getPath();
			   File file = new File(csv_filepath);
				Toast.makeText(getApplicationContext(), "Import", Toast.LENGTH_SHORT).show();
				//String csvFilename = "C:\\sample.csv";
				CSVReader csvReader = new CSVReader(new FileReader(file));
				String[] row = null;
				Trip tmp_trip;
				CheckIn tmp_checkin;
				DataBaseHandler db = new DataBaseHandler(getApplicationContext());
				csvReader.readNext();
				int newtrip_id=-1;
				int id = -1;
				while((row = csvReader.readNext()) != null) {
				    System.out.println(row[0] + " # " + row[1] + " # " + row[2]  + " # " + row[3]  + " #  " + row[4] + " #  " +  row[5]  + " #  " + row[6]
				    		+ " # " + row[7] + " # " + row[8] + " # " + row[9]  + " # " + row[10]  + " #  " + row[11] + " #  " +  row[12]  + " #  " + row[13]
			    		+ " # " + row[14] + " # "+ row[15]);
//				    public Trip(String name, double total_distance, String total_time, int complete, String start_date,
//				    		String end_date, String type, String note)
				    
				    if(Integer.parseInt(row[0]) != newtrip_id)
				    {
					    //Create Trip
					    tmp_trip = new Trip();
					    tmp_trip.setNAME(row[1]);
					    tmp_trip.setTYPE(row[2]);
					    tmp_trip.setCOMPLETE(Integer.parseInt(row[3]));
					    tmp_trip.setDISTANCE(Integer.parseInt(row[4]));
					    tmp_trip.setTIME(row[5]);
					    tmp_trip.setNOTE(row[6]);
					    tmp_trip.setSTARTDATE(row[7]);
					    tmp_trip.setENDDATE(row[8]);
					    id = db.addTrip(tmp_trip);
					    newtrip_id = Integer.parseInt(row[0]);
				    }
				    //Create CheckIn
				    if(id != -1)
				    {
					    tmp_checkin = new CheckIn();
					    tmp_checkin.set_tripId(id);
					    tmp_checkin.set_latitude(Double.parseDouble(row[10]));
					    tmp_checkin.set_longitude(Double.parseDouble(row[11]));
					    tmp_checkin.set_address(row[12]);
					    tmp_checkin.set_note(row[13]);
					    tmp_checkin.set_date(row[14]);
					    tmp_checkin.set_date(row[15]);
					    db.addCheckIn(tmp_checkin);
				    }
				}
				db.close();
				csvReader.close();
				LoadListData();
			   }
			   catch (IOException e)
			   {
				   e.printStackTrace();
			   }
			   catch (Exception e)
			   {
				   e.printStackTrace();
			   }
		   }
		   break;
		 }
	  }
	
	String[] ConcatenateArrays(String[] first, String[] second) {
	    List<String> both = new ArrayList<String>(first.length + second.length);
	    Collections.addAll(both, first);
	    Collections.addAll(both, second);
	    return both.toArray(new String[both.size()]);
	}
}
