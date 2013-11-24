package com.example.crsautomotive;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.Location;

public class CheckInHelper {
	
	/**
	 * Create CheckIn and insert into database
	 * @param tripID - TripId for this CheckIn
	 * @param notes - notes
	 * @param context - context
	 * @param date_time - current date and time
	 * @return <b>CheckIn</b> if everything is ok, otherwise return null
	 */
	public static CheckIn createCheckIn(int tripID, String notes,
			Context context, String date_time) {
		try {
			GPSTracker gpsTracker = new GPSTracker(context);
			Location currentLocation = gpsTracker.getLocation();

			if (currentLocation == null) {
				throw new Exception("Can't get device location.");
			}

			String address;
			try {
				address = gpsTracker.getAddress(currentLocation);
			} catch (Exception e) {
				address = null;
			}
			
			CheckIn newCheckIn = new CheckIn(tripID,
					currentLocation.getLatitude(),
					currentLocation.getLongitude(),
					address, notes, date_time);

			DataBaseHandler db = new DataBaseHandler(context);
			long checkInId = db.addCheckIn(newCheckIn);

			if (checkInId == -1) {
				throw new Exception("Error while inserting new Check In!");
			}

			newCheckIn.set_id((int) checkInId);

			return newCheckIn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Create CheckIn and insert into database
	 * @param tripID - TripId for this CheckIn
	 * @param notes - notes
	 * @param context - context
	 * @param date_time - current date and time
	 * @param currentLocation - CheckIn location
	 * @param currentAddress - CheckIn address
	 * @return CheckIn if everything is ok, otherwise return null
	 */
	public static CheckIn createCheckIn(int tripID, String notes,
			Context context, String date_time, Location currentLocation,
			String currentAddress) {
		try {
			if (currentLocation == null) {
				throw new Exception("Can't get device location.");
			}

			CheckIn newCheckIn = new CheckIn(tripID,
											 currentLocation.getLatitude(),
											 currentLocation.getLongitude(),
											 currentAddress,
											 notes,
											 date_time);

			DataBaseHandler db = new DataBaseHandler(context);
			long checkInId = db.addCheckIn(newCheckIn);

			if (checkInId == -1) {
				throw new Exception("Error while inserting new Check In!");
			}

			newCheckIn.set_id((int) checkInId);

			return newCheckIn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Insert CheckIn into database
	 * @param context - context
	 * @param checkIn - CheckIn for inserting
	 * @param currentLocation - CheckIn location
	 * @param currentAddress - CheckIn address
	 * @return CheckIn if everything is ok, otherwise return null
	 */
	public static CheckIn createCheckIn(Context context, CheckIn checkIn,
			Location currentLocation, String currentAddress) {
		try {
			if (currentLocation == null) {
				throw new Exception("Can't get device location.");
			}

			DataBaseHandler db = new DataBaseHandler(context);
			long checkInId = db.addCheckIn(checkIn);

			if (checkInId == -1) {
				throw new Exception("Error while inserting new Check In!");
			}

			checkIn.set_id((int) checkInId);

			return checkIn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Create CheckIn for non-finished trip
	 * @param notes - notes
	 * @param context - context
	 * @param date_time - current Date and Time
	 * @return CheckIn if everything is ok, otherwise return null
	 */
	public static CheckIn createCheckIn(String notes,
			Context context, String date_time) {
		try {
			int tripID = TripHelper.getActiveTrip(context).getID();
			
			GPSTracker gpsTracker = new GPSTracker(context);
			Location currentLocation = gpsTracker.getLocation();

			if (currentLocation == null) {
				throw new Exception("Can't get device location.");
			}

			CheckIn newCheckIn = new CheckIn(tripID,
					currentLocation.getLatitude(),
					currentLocation.getLongitude(),
					gpsTracker.getAddress(currentLocation), notes, date_time);

			DataBaseHandler db = new DataBaseHandler(context);
			long checkInId = db.addCheckIn(newCheckIn);

			if (checkInId == -1) {
				throw new Exception("Error while inserting new Check In!");
			}

			newCheckIn.set_id((int) checkInId);

			return newCheckIn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<CheckIn> getCheckInsForTrip(int tripId, Context context) {
		try {
			DataBaseHandler db = new DataBaseHandler(context);
			List<CheckIn> checkInsForTripId = db.getTripCheckIns(tripId);
			GPSTracker gpsTracker = new GPSTracker(context);
			for (CheckIn checkIn : checkInsForTripId) {
				if (checkIn.get_address() == null) {
					// try to get address from service
					String address = null;;
					try {
						address = gpsTracker.getAddress(checkIn.get_latitude(), checkIn.get_longitude());
					} catch (Exception e) {
						address = null;
					}
					
					if (address != null) {
						// service return address for location
						checkIn.set_address(address);
						db.updateCheckInAddress(checkIn.get_id(), address);
					}
					else {
						checkIn.set_address("Address not available.");
					}
				}
			}
			return checkInsForTripId;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static CheckIn getLastCheckInForTrip(int tripId, Context context) {
		try {
			DataBaseHandler db = new DataBaseHandler(context);
			CheckIn checkInForTripId = db.getLastCheckIn(tripId);
			return checkInForTripId;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
