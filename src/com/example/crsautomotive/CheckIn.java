package com.example.crsautomotive;

import android.location.Location;

public class CheckIn {     
	private int _id;
	private int _tripId;
	private double _latitude;
	private double _longitude;
	private String _address;
	private String _note;
	private String _date;
  
    //Empty constructor
    public CheckIn(){
    	this._id = -1;
        this._tripId = -1;
        this._latitude = 0;
        this._longitude = 0;
        this._address = null;
        this._note = null;
        this._date = null;
    }
    
    // constructor
    public CheckIn(int id, int tripId, double latitude, double longitude, String address, String note, String date)
    {
        this._id = id;
        this._tripId = tripId;
        this._latitude = latitude;
        this._longitude = longitude;
        this._address = address;
        this._note = note;
        this._date = date;
    }
    
    // constructor
    public CheckIn(int tripId, double latitude, double longitude, String address, String note, String date)
    {
        this._tripId = tripId;
        this._latitude = latitude;
        this._longitude = longitude;
        this._address = address;
        this._note = note;
        this._date = date;
    }
    
    // constructor
    public CheckIn(int tripId, Location location, String address, String note, String date)
    {
        this._tripId = tripId;
        this._latitude = location.getLatitude();
        this._longitude = location.getLongitude();
        this._address = address;
        this._note = note;
        this._date = date;
    }
    
    //Get and set methods
    public void set_id(int _id) {
		this._id = _id;
	}
    
    public int get_id() {
		return _id;
	}
    
    public void set_tripId(int _tripId) {
		this._tripId = _tripId;
	}
    
    public int get_tripId() {
		return _tripId;
	}
    
    public void set_latitude(double _latitude) {
		this._latitude = _latitude;
	}
    
    public double get_latitude() {
		return _latitude;
	}
    
    public void set_longitude(double _longitude) {
		this._longitude = _longitude;
	}
    
    public double get_longitude() {
		return _longitude;
	}
    
    public void set_address(String _address) {
		this._address = _address;
	}
    
    public String get_address() {
		return _address;
	}
    
    public void set_note(String _note) {
		this._note = _note;
	}
    
    public String get_note() {
		return _note;
	}
    
    public void set_date(String _date) {
		this._date = _date;
	}
    
    public String get_date() {
		return _date;
	}
}
