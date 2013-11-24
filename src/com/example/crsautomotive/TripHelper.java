package com.example.crsautomotive;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class TripHelper {
	public static Trip getActiveTrip(Context context) {
		try {
			DataBaseHandler db = new DataBaseHandler(context);
			List<Trip> unfinishedTrips = db.getAllUnfinishedTrips();
			
			if (unfinishedTrips.size() < 1) {
				throw new Exception("There is no active trip!");
			}
			if (unfinishedTrips.size() > 1) {
				throw new Exception("There is more than one active trips!");
			}
			
			return unfinishedTrips.get(0);
		} catch (Exception e) {
			Toast.makeText(context, "Error in read active trip." + e.getMessage(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
		return null;
	}
	
	public static String createNewTrip(String tripType, String notes, Context context, String currentDateAndTime) {
		try {
			DataBaseHandler db = new DataBaseHandler(context);
			int numberOfTrips = db.getNumberOfTrips();
			String tripName = tripType + " " + numberOfTrips;
			
			// calculate distance for this trip
			
			Trip newTrip = new Trip(tripName, -1, null, 0, currentDateAndTime, null, tripType, notes);
			
			// get current location
			GPSTracker gpsTracker = new GPSTracker(context);
			Location currentLocation = gpsTracker.getLocation();
			if (currentLocation == null) {
				throw new Exception("Can't get device location.");
			}
			
			String currentAddress = null;
			try {
				currentAddress = gpsTracker.getAddress(currentLocation);
			} catch (Exception e) { }
			
			List<Trip> unfinishedTrips = db.getAllUnfinishedTrips();
			// set current coordinate for last check in for unfinished trips
			for (Trip trip : unfinishedTrips) {
				CheckInHelper.createCheckIn(trip.getID(), null, context, currentDateAndTime, currentLocation, currentAddress);
			}
			
			int oldTripID = -1;
			if (unfinishedTrips.size() > 0) {
				//oldTripID = db.finishActiveTrip(currentDateAndTime);
				oldTripID = finishTripAndSetTime(context, unfinishedTrips.get(0).getID(), currentDateAndTime);
			}
			// all trips finished
			
			// add new trip into database
			int newTripId = db.addTrip(newTrip);
			newTrip.setID(newTripId);
			
			// update trip with new calculated distance
			name(context, oldTripID);
			
			// create and add new check in into database
			CheckIn newCheckIn = new CheckIn(newTripId, currentLocation, currentAddress, null, currentDateAndTime);
			CheckInHelper.createCheckIn(context, newCheckIn, currentLocation, currentAddress);
			
			return newTrip.getNAME();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void name(Context context, int tripID) {
		if (tripID == -1) {
			return;
		}
		List<CheckIn> allCheckIns = CheckInHelper.getCheckInsForTrip(tripID, context);
		GPSTracker gpsTracker = new GPSTracker(context);
		gpsTracker.getTotalDistancne(allCheckIns, null, null);
	}
	
	public static boolean updateTripTotalDistance(int tripID, Context context, int newDistance) {
		try {
			DataBaseHandler db = new DataBaseHandler(context);
			db.updateTripTotalDistance(tripID, newDistance);
			
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static int getTripTotalDistance(int tripID, Context context) {
		try {
			DataBaseHandler db = new DataBaseHandler(context);
			int distance = db.getTripTotalDistance(tripID);
			
			return distance;
		} catch (Exception e) {
			return -1;
		}
	}
	
	public static void finishTrip(int tripID, Context context, String currentDateAndTime, int totalDistance) {
		try {
			DataBaseHandler db = new DataBaseHandler(context);
			
			// get current location
			GPSTracker gpsTracker = new GPSTracker(context);
			Location currentLocation = gpsTracker.getLocation();
			if (currentLocation == null) {
				throw new Exception("Can't get device location.");
			}
			
			String currentAddress = null;
			try {
				currentAddress = gpsTracker.getAddress(currentLocation);
			} catch (Exception e) { }
			
			// create and add new check in into database
			CheckIn newCheckIn = new CheckIn(tripID, currentLocation, currentAddress, null, currentDateAndTime);
			CheckInHelper.createCheckIn(context, newCheckIn, currentLocation, currentAddress);

			if (tripID > 0) {
				db.finishTrip(tripID, currentDateAndTime, totalDistance);
			}
			else {
				finishTripAndSetTime(context, tripID, currentDateAndTime);
			}
			db.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static int finishTripAndSetTime(Context context, int trip_id, String currentDateAndTime)
	{
		List<CheckIn> all_checkins;
		DataBaseHandler db = new DataBaseHandler(context);
		DateFormat df = DateFormat.getDateTimeInstance();
		Date dateOne = null;
		Date dateTwo = null;
		all_checkins = CheckInHelper.getCheckInsForTrip(trip_id, context);
		
		try {
			dateOne = df.parse(all_checkins.get(all_checkins.size()-1).get_date());
			dateTwo = df.parse(all_checkins.get(0).get_date());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		long time = dateTwo.getTime() - dateOne.getTime();
		long diffSeconds = time / 1000 % 60;
		long diffMinutes = time / (60 * 1000) % 60;
		long diffHours = time / (60 * 60 * 1000) % 24;
		long diffDays = time / (24 * 60 * 60 * 1000);
	    
		Log.d("asdas","difference:" + time);
		String daysText = diffDays > 1 ? " days " : " day ";
		
		String total_time = diffDays + daysText + diffHours + ":" + diffMinutes + ":" + diffSeconds;
		int tmp = db.finishActiveTrip(currentDateAndTime, total_time);
		db.close();
		return tmp;
	}
}
