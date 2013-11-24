package com.example.crsautomotive;

public class Camera {
	
	private String CAMERA_NAME;
	private String CAMERA_LINK;
	
	public Camera()
	{
		CAMERA_NAME = null;
		CAMERA_LINK = null;
	}
	
	public Camera(String name, String link)
	{
		CAMERA_NAME = name;
		CAMERA_LINK = link;
	}
	
	public void setCameraName(String name)
	{
		CAMERA_NAME = name;
	}
	
	public String getCameraName()
	{
		return CAMERA_NAME;
	}
	
	public void setCameraLink(String link)
	{
		CAMERA_LINK = link;
	}
	
	public String getCameraLink()
	{
		return CAMERA_LINK;
	}

}
