package com.example.crsautomotive;

import java.util.UUID;

public class Trip {
	
	    private int _id;
	    private String _name;
	    private double _total_distance;
	    private String _total_time;
	    private int _complete;
	    private String _start_date;
	    private String _end_date;
	    private String _type;
	    private String _note;
	    private UUID _guid;
	  
	    //Empty constructor
	    public Trip(){
	 
	    }
	    // constructor
	    public Trip(int id, String name, double total_distance, String total_time, int complete, String start_date,
	    		String end_date, String type, String note, UUID guid)
	    {
	        this._id = id;
	        this._name = name;
	        this._total_distance = total_distance;
	        this._total_time = total_time;
	        this._complete = complete;
	        this._start_date = start_date;
	        this._end_date = end_date;
	        this._type = type;
	        this._note = note;
	        this._guid = guid;
	    }
	    
	    
	    // constructor
	    public Trip(String name, double total_distance, String total_time, int complete, String start_date,
	    		String end_date, String type, String note)
	    {
	        this._name = name;
	        this._total_distance = total_distance;
	        this._total_time = total_time;
	        this._complete = complete;
	        this._start_date = start_date;
	        this._end_date = end_date;
	        this._type = type;
	        this._note = note;
	    }
	    
	    //Get and set methods
	    
	    public void setID(int id)
	    {
	    	this._id = id;
	    }
	    
	    public int getID()
	    {
	    	return this._id;
	    }
	    
	    public void setNAME(String name)
	    {
	    	this._name = name;
	    }
	    
	    public String getNAME()
	    {
	    	return this._name;
	    }
	    
	    public void setDISTANCE(double distance)
	    {
	    	this._total_distance = distance;
	    }
	    
	    public double getDISTANCE()
	    {
	    	return this._total_distance;
	    }
	    
	    public void setTIME(String time)
	    {
	    	this._total_time = time;
	    }
	    
	    public String getTIME()
	    {
	    	return this._total_time;
	    }
	    
	    public void setCOMPLETE(int complete)
	    {
	    	this._complete = complete;
	    }
	    
	    public int getCOMPLETE()
	    {
	    	return this._complete;
	    }
	    
	    public void setSTARTDATE(String date)
	    {
	    	this._start_date = date;
	    }
	    
	    public String getSTARTDATE()
	    {
	    	return this._start_date;
	    }
	    
	    public void setENDDATE(String date)
	    {
	    	this._end_date = date;
	    }
	    
	    public String getENDDATE()
	    {
	    	return this._end_date;
	    }
	    
	    public void setTYPE(String type)
	    {
	    	this._type = type;
	    }
	    
	    public String getTYPE()
	    {
	    	return this._type;
	    }
	    
	    public void setNOTE(String note)
	    {
	    	this._note = note;
	    }
	    
	    public String getNOTE()
	    {
	    	return this._note;
	    }
	    
	    public void setGUID(UUID guid)
	    {
	    	this._guid = guid;
	    }
	    
	    public UUID getGUID()
	    {
	    	return this._guid;
	    }

}
