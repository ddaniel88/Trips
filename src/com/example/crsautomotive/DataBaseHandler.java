package com.example.crsautomotive;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper{
	
	// Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "crs_automotive";
    
    private static final String TABLE_TRIPS = "Trip";
    
    private static final String TABLE_CHECKIN = "CheckIn";
    
    //Trip columns
    private static final String KEY_TRIPID = "TripID";
    private static final String KEY_NAME = "Name";
    private static final String KEY_TYPE = "Type";
    private static final String KEY_TOTAL_DISTANCE = "TotalDistance";
    private static final String KEY_COMPLETE = "IsComplete";
    private static final String KEY_NOTE = "Note";
    private static final String KEY_TOTAL_TIME = "TotalTime";
    private static final String KEY_START_DATE = "StartDate";
    private static final String KEY_END_DATE = "EndDate";
    private static final String KEY_GUID = "GUID";
    
    //CheckIn columns
    private static final String KEY_CID = "CheckInID";
    private static final String KEY_LATITUDE = "Latitude";
    private static final String KEY_LONGITUDE = "Longitude";
    private static final String KEY_ADRESS = "Address";
    private static final String KEY_CNOTE = "Note";
    private static final String KEY_CDATE = "Date";

	public DataBaseHandler(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
		
		//Create table trip
		String CREATE_TRIPS_TABLE =
			"CREATE TABLE " + TABLE_TRIPS + "("
                + KEY_TRIPID + " INTEGER PRIMARY KEY NOT NULL, "
				+ KEY_NAME + " VARCHAR NOT NULL, "
				+ KEY_TYPE + " VARCHAR NOT NULL, " 
                + KEY_COMPLETE + " INTEGER NOT NULL, "
                + KEY_TOTAL_DISTANCE + " INTEGER, "
                + KEY_TOTAL_TIME + " VARCHAR, "
                + KEY_NOTE + " TEXT, "
                + KEY_START_DATE + " DATETIME NOT NULL, "
                + KEY_END_DATE + " DATETIME, "
                + KEY_GUID + " VARCHAR NOT NULL "
            + ")";
        db.execSQL(CREATE_TRIPS_TABLE);
        
        
        //Create table CheckIn
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECKIN);
		String CREATE_CHECKIN_TABLE =
				"CREATE TABLE " + TABLE_CHECKIN + "("
					+ KEY_CID + " INTEGER PRIMARY KEY NOT NULL, "
					+ KEY_TRIPID + " INTEGER NOT NULL, "
					+ KEY_LATITUDE + " DOUBLE NOT NULL, "
					+ KEY_LONGITUDE + " DOUBLE NOT NULL, "
					+ KEY_ADRESS + " VARCHAR, "
					+ KEY_CNOTE + " TEXT, "
					+ KEY_CDATE + " DATETIME NOT NULL, "
					+ "FOREIGN KEY(" + KEY_TRIPID + ") REFERENCES " + TABLE_TRIPS + "(" + KEY_TRIPID + ")"
				+ ")";
		
		db.execSQL(CREATE_CHECKIN_TABLE);
	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIPS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECKIN);
		
	}
	
	//Database Operations
	// Add new Trip
    public int addTrip(Trip trip)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	 
        ContentValues values = new ContentValues();
        String guid = java.util.UUID.randomUUID().toString();
        
        values.put(KEY_NAME, trip.getNAME()); 
        values.put(KEY_COMPLETE, Integer.toString(trip.getCOMPLETE())); 
        values.put(KEY_START_DATE, trip.getSTARTDATE());
        values.put(KEY_END_DATE, trip.getENDDATE());
        values.put(KEY_TYPE, trip.getTYPE());
        values.put(KEY_NOTE, trip.getNOTE());
        values.put(KEY_TOTAL_DISTANCE, Double.toString(trip.getDISTANCE()));
        values.put(KEY_TOTAL_TIME, trip.getTIME());
        values.put(KEY_GUID, guid);
        // Inserting Row
        long newTripId = db.insert(TABLE_TRIPS, null, values);
        db.close(); // Closing database connection
        
        return (int)newTripId;
    }
    
    // Getting single trip
    public Trip getTrip(int id)
    {
    	Trip trip = null;
    	SQLiteDatabase db = this.getReadableDatabase();
    	
        Cursor cursor = db.query(TABLE_TRIPS, new String[] { KEY_TRIPID, KEY_NAME, KEY_TOTAL_DISTANCE,  KEY_TOTAL_TIME,
        		KEY_COMPLETE, KEY_START_DATE, KEY_END_DATE, KEY_TYPE, KEY_NOTE, KEY_GUID
        	}, KEY_TRIPID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        
        if (cursor != null)
        {
        	if(cursor.moveToFirst())
        	{
        		trip = new Trip(
	        		Integer.parseInt(cursor.getString(0)),
	        		cursor.getString(1),
	        		Double.parseDouble(cursor.getString(2)),
	        		cursor.getString(3),
	        		Integer.parseInt(cursor.getString(4)),
	        		cursor.getString(5),
	        		cursor.getString(6),
	        		cursor.getString(7),
	        		cursor.getString(8),
	        		UUID.fromString(cursor.getString(9))
        		);
        	}
        }
        
        cursor.close();
        db.close();
        // return trip
        return trip;	
    }
    
    // Getting All Trips
    public List<Trip> getAllTrips() 
    {
    	List<Trip> tripList = new ArrayList<Trip>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_TRIPS + " order by " + KEY_TRIPID + " desc";
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) 
        {
            do 
            {
            	Trip trip = new Trip();
            	trip.setID(Integer.parseInt(cursor.getString(0)));
            	trip.setNAME(cursor.getString(1));
            	trip.setCOMPLETE(Integer.parseInt(cursor.getString(3)));
            	trip.setSTARTDATE(cursor.getString(7));
            	trip.setENDDATE(cursor.getString(8));
            	trip.setTYPE(cursor.getString(2));
         		trip.setNOTE(cursor.getString(6));
            	trip.setDISTANCE(Double.parseDouble(cursor.getString(4)));
            	trip.setTIME(cursor.getString(5));
            	trip.setGUID(UUID.fromString(cursor.getString(9)));
            	tripList.add(trip);
            } 
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return trip list
        return tripList;    	
    }
    
    //get Number of trips
    public int getNumberOfTrips()
    {
    	
    	String selectQuery = "SELECT * FROM " + TABLE_TRIPS;
    	SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int numberOfTrips = cursor.getCount();
        db.close();
        return  numberOfTrips;
    }
    
    //get Number of Personal trips
    public int getNumberOfPersonalTrips()
    {
    	
    	String selectQuery = "SELECT * FROM " + TABLE_TRIPS + " WHERE " + KEY_TYPE +" = Personal";
    	SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int numberOfTrips = cursor.getCount();
        db.close();
        return  numberOfTrips;
    }
    
  //get Number of Buisiness trips
    public int getNumberOfBuisinessTrips()
    {
    	
    	String selectQuery = "SELECT * FROM " + TABLE_TRIPS + " WHERE " + KEY_TYPE + " = Buisiness";
    	SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int numberOfTrips = cursor.getCount();
        db.close();
        return  numberOfTrips;
    }
    
    //Get all unfinished trips
    public List<Trip> getAllUnfinishedTrips()
    {
    	List<Trip> tripList = new ArrayList<Trip>();
    	String selectQuery = "SELECT * FROM " + TABLE_TRIPS + " WHERE " + KEY_COMPLETE +" = 0";
    	SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) 
        {
            do 
            {
            	Trip trip = new Trip();
            	trip.setID(Integer.parseInt(cursor.getString(0)));
            	trip.setNAME(cursor.getString(1));
            	trip.setCOMPLETE(Integer.parseInt(cursor.getString(3)));
            	trip.setSTARTDATE(cursor.getString(7));
            	trip.setENDDATE(cursor.getString(8));
            	trip.setTYPE(cursor.getString(2));
         		trip.setNOTE(cursor.getString(6));
            	trip.setDISTANCE(Double.parseDouble(cursor.getString(4)));
            	trip.setTIME(cursor.getString(5));
            	trip.setGUID(UUID.fromString(cursor.getString(9)));
            	tripList.add(trip);
            } 
            while (cursor.moveToNext());
        }
        db.close();
        return  tripList;
    }
    
    //Update trip name with given ID
    public void updateTripName(int trip_id, String name)
    {
    	String selectQuery = "UPDATE " + TABLE_TRIPS + " SET " + KEY_NAME + " ='"+ name + "' WHERE " + KEY_TRIPID+ " = " + trip_id;
    	SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(selectQuery);
        db.close();
    }
    
    //Finish active trip
    public int finishActiveTrip(String date, String total_time)
    {
    	String selectQuery = "SELECT " + KEY_TRIPID + " FROM " + TABLE_TRIPS;
        
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int tripID = -1;
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) 
        {
            tripID = Integer.parseInt(cursor.getString(0));
        }
        cursor.close();

    	selectQuery = "UPDATE " + TABLE_TRIPS +
    				  " SET " + KEY_END_DATE + " ='" + date + "', " +
    				  KEY_COMPLETE + " = 1, " +
    				  KEY_TOTAL_TIME + " ='" + total_time + "'" +
    				  " WHERE " + KEY_COMPLETE + " = 0";
        db.execSQL(selectQuery);
        db.close();
        
        return tripID;
    }
    
    public void finishTrip(int tripID, String date, int totalDistance) {
    	String selectQuery = "UPDATE " + TABLE_TRIPS +
    						 " SET " + KEY_END_DATE + " ='" + date + "', " +
    						 KEY_TOTAL_DISTANCE + " = " + totalDistance + ", " +
						 	 KEY_COMPLETE + " = 1" +
    						 " WHERE " + KEY_TRIPID + " = " + tripID;
    	SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(selectQuery);
        db.close();
	}
    
    public String getTotalTimeForTrip(int tripID)
    {
    	String selectQuery = "SELECT  "+ KEY_TOTAL_TIME + " FROM " + TABLE_TRIPS + " WHERE " + KEY_TRIPID + " = " + tripID;
    	SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String total_time = null;
        if (cursor.moveToFirst())
        {
        	total_time = cursor.getString(0);
        }
        db.close();
        return  total_time;
    }
   
    
    /**
     * Adding new Check In
     * @param checkIn Check In for insert
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long addCheckIn(CheckIn checkIn)
    {
    	SQLiteDatabase db = this.getWritableDatabase();
    	 
        ContentValues values = new ContentValues();
        
        values.put(KEY_TRIPID, checkIn.get_tripId()); 
        values.put(KEY_LATITUDE, checkIn.get_latitude());
        values.put(KEY_LONGITUDE, checkIn.get_longitude());
        values.put(KEY_CDATE, checkIn.get_date());
        
        if (checkIn.get_address() != null) {
			values.put(KEY_ADRESS, checkIn.get_address());
		}
        
		if (checkIn.get_note() != null) {
			values.put(KEY_CNOTE, checkIn.get_note());
		}
		
		// Inserting Row
        long checkInId = db.insert(TABLE_CHECKIN, null, values);
        
        // Closing database connection
        db.close();
        
        return checkInId;
    }

    // Getting single CheckIn
    public CheckIn getCheckIn(int cid)
    {
    	CheckIn checkIn = null;
    	SQLiteDatabase db = this.getReadableDatabase();
    	 
        Cursor cursor = db.query(TABLE_CHECKIN, new String[] { KEY_CID, KEY_TRIPID, KEY_LATITUDE, KEY_LONGITUDE, KEY_ADRESS, KEY_CNOTE, KEY_CDATE
        	}, KEY_CID + "=?",
                new String[] { String.valueOf(cid) }, null, null, null, null);
        if (cursor != null)
            
        	if(cursor.moveToFirst())
        	{
        		checkIn = new CheckIn(
        				Integer.parseInt(cursor.getString(0)),
        				Integer.parseInt(cursor.getString(1)),
        				Double.parseDouble(cursor.getString(2)),
        				Double.parseDouble(cursor.getString(3)),
        				cursor.getString(4),
        				cursor.getString(5),
        				cursor.getString(6)
    				);
        	}
        cursor.close();
        db.close();
        // return trip
        return checkIn;	
    }
    
//    Get All CheckIns
    public List<CheckIn> getAllCheckIns() 
    {
    	List<CheckIn> checkInList = new ArrayList<CheckIn>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_CHECKIN + " order by " + KEY_CDATE + " desc"; //+ KEY_TRIPID + " asc, " + KEY_CID + " desc";
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) 
        {
            do 
            {
            	CheckIn checkIn = new CheckIn();
            	checkIn.set_id(Integer.parseInt(cursor.getString(0)));
            	checkIn.set_tripId(Integer.parseInt(cursor.getString(1)));
            	checkIn.set_latitude(Double.parseDouble(cursor.getString(2)));
            	checkIn.set_longitude(Double.parseDouble(cursor.getString(3)));
            	checkIn.set_address(cursor.getString(4));
            	checkIn.set_note(cursor.getString(5));
            	checkIn.set_date(cursor.getString(6));
            	checkInList.add(checkIn);
            } 
            while (cursor.moveToNext());
        }
        cursor.close();
     
        // return trip list
        return checkInList;    	
    }
    
    //Get CheckIns for Trip
    public List<CheckIn> getTripCheckIns(int tripID) 
    {
    	List<CheckIn> checkInList = new ArrayList<CheckIn>();
        // Select All Query
        String selectQuery = "SELECT ci.*, t.StartDate, t.TotalDistance, t.TotalTime FROM " + TABLE_TRIPS + " t inner join " 
        + TABLE_CHECKIN + " ci on t.TripID = ci.TripID where t.TripID = " + tripID + " order by ci.Date desc";
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) 
        {
            do 
            {
            	CheckIn checkIn = new CheckIn();
            	checkIn.set_id(Integer.parseInt(cursor.getString(0)));
            	checkIn.set_tripId(Integer.parseInt(cursor.getString(1)));
            	checkIn.set_latitude(Double.parseDouble(cursor.getString(2)));
            	checkIn.set_longitude(Double.parseDouble(cursor.getString(3)));
            	checkIn.set_address(cursor.getString(4));
            	checkIn.set_note(cursor.getString(5));
            	checkIn.set_date(cursor.getString(6));
            	checkInList.add(checkIn);
            } 
            while (cursor.moveToNext());
        }
        cursor.close();
     
        // return trip list
        return checkInList;    	
    }
    
    public CheckIn getLastCheckIn(int tripID) {
        String selectQuery = "SELECT ci.*, t.StartDate, t.TotalDistance, t.TotalTime FROM " + TABLE_TRIPS + " t INNER JOIN " 
        + TABLE_CHECKIN + " ci ON t.TripID = ci.TripID WHERE t.TripID = " + tripID + " ORDER BY ci.Date DESC LIMIT 1";
        
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        CheckIn checkIn = new CheckIn();
        
        if (cursor.moveToFirst())
        {
			checkIn.set_id(Integer.parseInt(cursor.getString(0)));
			checkIn.set_tripId(Integer.parseInt(cursor.getString(1)));
			checkIn.set_latitude(Double.parseDouble(cursor.getString(2)));
			checkIn.set_longitude(Double.parseDouble(cursor.getString(3)));
			checkIn.set_address(cursor.getString(4));
			checkIn.set_note(cursor.getString(5));
			checkIn.set_date(cursor.getString(6));
        }
        cursor.close();
     
        // return trip list
        return checkIn;    
	}
    
    public void updateTripTotalDistance(int trip_id, int newDistance)
    {
    	String selectQuery = "UPDATE " + TABLE_TRIPS +
    						 " SET " + KEY_TOTAL_DISTANCE + " ='" + newDistance +
    						 "' WHERE " + KEY_TRIPID + " = " + trip_id;
    	SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(selectQuery);
        db.close();
    }
    
    public int getTripTotalDistance(int tripID) {
    	String selectQuery = "SELECT t.TotalDistance FROM " + TABLE_TRIPS + " t " +
    	        			 "WHERE t.TripID = " + tripID;
    	        
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int distance = -1;
        
        if (cursor.moveToFirst())
        {
			distance = Integer.parseInt(cursor.getString(0));
        }
        cursor.close();
        
        return distance;
	}
    
    public void updateCheckInAddress(int checkInID, String address) {
    	String selectQuery = "UPDATE " + TABLE_CHECKIN +
				 " SET " + KEY_ADRESS + " ='" + address +
				 "' WHERE " + KEY_CID + " = " + checkInID;
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(selectQuery);
		db.close();
	}
}
