package com.example.crsautomotive;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class Cameras extends Activity implements OnClickListener{
	
	String[] list_array=null;
	String[] camera_links=null;// 
	ListView gta_list;
	
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_cameras);
	        
	        //setTitle("GTA Traffic Cameras");
	   
	        
	        
	       list_array = getResources().getStringArray(R.array.c400); //Default first element
	       camera_links = getResources().getStringArray(R.array.u400);
	       gta_list = (ListView) findViewById(R.id.camera_list);
	       setlistAdapter();
	        Spinner spinner = (Spinner) findViewById(R.id.gta_camera_spinner);
	        
	        List<String> list = new ArrayList<String>();
	    	list.add("400");
	    	list.add("401");
	    	list.add("403");
	    	list.add("404");
	    	list.add("410");
	    	list.add("427");
	    	
	    	ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
    		android.R.layout.simple_spinner_item, list);
    		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    		spinner.setAdapter(dataAdapter);
    		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    			int count=0;
    		    @Override
    		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
    		    	 if(count >= 1){
	    		        // your code here
	    		    	String camera = ((TextView) selectedItemView).getText().toString();
	    		    	Toast.makeText(getApplicationContext(),camera , Toast.LENGTH_SHORT).show();
	    		    	if(camera.equals("401"))
	    		    	{
	    		        	list_array = getResources().getStringArray(R.array.c401);
	    		        	camera_links = getResources().getStringArray(R.array.u401);
	    		    		setlistAdapter();
			        	}
	    		        else if(camera.equals("400"))
	    		        {
	    		        	list_array = getResources().getStringArray(R.array.c400);
	    		        	camera_links = getResources().getStringArray(R.array.u400);
	    		        	setlistAdapter();
	    		        }
	    		        else if(camera.equals("403"))
	    		        {
	    		        	list_array = getResources().getStringArray(R.array.c403);
	    		        	camera_links = getResources().getStringArray(R.array.u403);
	    		        	setlistAdapter();
	    		        }
	    		        else if(camera.equals("404"))
	    		        {
	    		        	list_array = getResources().getStringArray(R.array.c404);
	    		        	camera_links = getResources().getStringArray(R.array.u404);
	    		        	setlistAdapter();
	    		        }
	    		        else if(camera.equals("410"))
	    		        {
	    		        	list_array = getResources().getStringArray(R.array.c410);
	    		        	camera_links = getResources().getStringArray(R.array.u410);
	    		        	setlistAdapter();
	    		        }
	    		        else if(camera.equals("427"))
	    		        {
	    		        	list_array = getResources().getStringArray(R.array.c427);
	    		        	camera_links = getResources().getStringArray(R.array.u427);
	    		        	setlistAdapter();
	    		        }
    		    	 }
    		    	 count++;
    		        	
    		    }

    		    @Override
    		    public void onNothingSelected(AdapterView<?> parentView) {
    		        // your code here
    		    }

    		});
    		
    		gta_list.setOnItemClickListener(new OnItemClickListener() {
	            public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id) {
		            	
		            	Intent i = new Intent(getApplicationContext(), CameraView.class);
		            	i.putExtra("all_cameras", camera_links);
		            	i.putExtra("all_camera_names", list_array);
		            	i.putExtra("current_id", position);
						startActivity(i);
	                }
    		});
	        
	        View btn=findViewById(R.id.layout_trip);
	        btn.setOnClickListener(this);
	        btn=findViewById(R.id.layout_audio);
	        btn.setOnClickListener(this);
	        btn=findViewById(R.id.layout_cameras);
	        btn.setOnClickListener(this);
	        
	        LinearLayout ll = (LinearLayout) findViewById(R.id.layout_cameras);
	        ll.setBackgroundResource(R.drawable.narandzasto);
	    }
	 
	 public void setlistAdapter()
	 {
		 ListAdapter ad = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 ,list_array);
 		 gta_list.setAdapter(ad);
	 }
	 
	 @Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		Intent i = new Intent(getApplicationContext(), TripLog.class);
		startActivity(i); 
		finish();
	}
	
	 
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i;
			switch (v.getId()) {
			
			case R.id.layout_trip:
				
				i = new Intent(getApplicationContext(), TripLog.class);
				startActivity(i);
				finish();
				break;
				
			case R.id.layout_audio:
				
				i = new Intent(getApplicationContext(), AudioNews.class);
				startActivity(i);
				finish();
				break;

			default:
				break;			
			}
		}
		
}


