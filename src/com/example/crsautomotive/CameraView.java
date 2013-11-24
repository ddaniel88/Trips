package com.example.crsautomotive;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class CameraView extends Activity implements OnClickListener{
	int current_id=0;
	int position = 0;
	String [] all_cameras_links = null; //url
	String [] all_camera_names = null; //name
	ViewFlipper page;
	Animation animFlipInForeward;
	Animation animFlipOutForeward;
	Animation animFlipInBackward; 
	Animation animFlipOutBackward;
	String direction = "right";
	ArrayList<Camera> list_of_cameras = new ArrayList<Camera>();
	Boolean first_time = true;
	
	 @SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      
	      this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	      setContentView(R.layout.camera_view);
	
	      
	      Intent intent = getIntent();
	      all_cameras_links =  intent.getStringArrayExtra("all_cameras");
	      all_camera_names =  intent.getStringArrayExtra("all_camera_names");
	      current_id = intent.getIntExtra("current_id", 0);
	      LoadListofCameras();
	      
	      // show The Image
	      new DownloadImageTask((ImageView) findViewById(R.id.camera_img))
	                  .execute(all_cameras_links[current_id]);
	        ImageView  InfoSlika=(ImageView) findViewById(R.id.camera_img);
	    
	        WindowManager   wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
		 
			//Set the image size 
			int	width = display.getWidth();
			int height = display.getHeight();
			int widthMain;
			int	heightMain;
			if(width<height)
			{
				widthMain = (int)(width);
				heightMain = (int)(width*0.68); // scale = img_width / img_height
				 LinearLayout.LayoutParams vp = 
					        new LinearLayout.LayoutParams(widthMain,heightMain);
				 InfoSlika.setLayoutParams(vp); 
			}
			else
			{
		
				LinearLayout.LayoutParams vp = 
					        new LinearLayout.LayoutParams
					        (LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.FILL_PARENT);
				 InfoSlika.setLayoutParams(vp);
			}
			
			//Set flip effect
			page = (ViewFlipper)findViewById(R.id.flipper);
			animFlipInForeward = AnimationUtils.loadAnimation(this, R.anim.flipin);
	        animFlipOutForeward = AnimationUtils.loadAnimation(this, R.anim.flipout);
	        animFlipInBackward = AnimationUtils.loadAnimation(this, R.anim.flipin_reverse);
	        animFlipOutBackward = AnimationUtils.loadAnimation(this, R.anim.flipout_reverse);
				 
			InfoSlika.setOnTouchListener(new SwipeDetect() {
	            public void onSwipeRight() {
	                //Your code here
	            	direction ="right";
	            	position = mod(current_id-1,all_cameras_links.length);
	            	new DownloadImageTask((ImageView) findViewById(R.id.camera_img))
	                  .execute(all_cameras_links[position] );
	            	current_id--;
	            }

	            public void onSwipeLeft() {
	                //Your code here
	            	direction ="left";
	            	position = mod(current_id+1,all_cameras_links.length);
	            	new DownloadImageTask((ImageView) findViewById(R.id.camera_img))
	                  .execute(all_cameras_links[position]);
	            	current_id++;
	            }
	            
	        });
		
		  //Set click listeners for tabs
	      View btn=findViewById(R.id.layout_trip);
	        btn.setOnClickListener(this);
	        btn=findViewById(R.id.layout_audio);
	        btn.setOnClickListener(this);
	        btn=findViewById(R.id.layout_cameras);
	        btn.setOnClickListener(this);
	        
	        LinearLayout ll = (LinearLayout) findViewById(R.id.layout_cameras);
	        ll.setBackgroundResource(R.drawable.narandzasto);
	 }
	 
	 
	 //load all cameras 
	 private void LoadListofCameras()
	 {
		 Camera tmp;
		 for(int i=0; i<all_cameras_links.length;i++)
		 {
			 tmp = new Camera();
			 tmp.setCameraName(all_camera_names[i]);
			 tmp.setCameraLink(all_cameras_links[i]);
			 list_of_cameras.add(tmp);
		 }
	 }
	 
	 private int mod(int x, int y)
	 {
	     int result = x % y;
	     return result < 0? result + y : result;
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
				
			case R.id.layout_cameras:
				
				i = new Intent(getApplicationContext(), Cameras.class);
				startActivity(i);
				break;

			default:
				break;			
			}
		}
	 
	 	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
		    ImageView bmImage;

		    public DownloadImageTask(ImageView bmImage) {
		        this.bmImage = bmImage;
		    }

		    protected Bitmap doInBackground(String... urls) {
		        String urldisplay = urls[0];
		        Bitmap mIcon11 = null;
		        try {
		            InputStream in = new java.net.URL(urldisplay).openStream();
		            mIcon11 = BitmapFactory.decodeStream(in);
		        } catch (Exception e) {
		            Log.e("Error", e.getMessage());
		            e.printStackTrace();
		        }
		        return mIcon11;
		    }

		    protected void onPostExecute(Bitmap result) {
		        bmImage.setImageBitmap(result);
		        TextView title = (TextView) findViewById(R.id.activity_title);
		      	title.setText(all_camera_names[position]);
		        if(direction.equals("right") && first_time == false)
		        {
		        	page.setInAnimation(animFlipInBackward);
					page.setOutAnimation(animFlipOutBackward);
					page.showPrevious();
		        }
		        
		        else if(direction.equals("left") && first_time == false)
		        {
		        	page.setInAnimation(animFlipInForeward);
		 			page.setOutAnimation(animFlipOutForeward);
		 			page.showNext();
		        }
		        first_time = false;
		    }
		}
	 	
	 	 

	    public class SwipeDetect implements OnTouchListener {

	    private final GestureDetector gestureDetector = new GestureDetector(new GestureListener());

	    public boolean onTouch(final View view, final MotionEvent motionEvent) {
	        return gestureDetector.onTouchEvent(motionEvent);
	    }

	    private final class GestureListener extends SimpleOnGestureListener {

	        private static final int SWIPE_THRESHOLD = 100;
	        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

	        @Override
	        public boolean onDown(MotionEvent e) {
	            return true;
	        }

	        @Override
	        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	            boolean result = false;
	            try {
	                float diffY = e2.getY() - e1.getY();
	                float diffX = e2.getX() - e1.getX();
	                if (Math.abs(diffX) > Math.abs(diffY)) {
	                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
	                        if (diffX > 0) {
	                            onSwipeRight();
	                        } else {
	                            onSwipeLeft();
	                        }
	                    }
	                } else {
	                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
	                        if (diffY > 0) {
	                            onSwipeBottom();
	                        } else {
	                            onSwipeTop();
	                        }
	                    }
	                }
	            } catch (Exception exception) {
	                exception.printStackTrace();
	            }
	            return result;
	        }
	    }

	    public void onSwipeRight() {
	    }

	    public void onSwipeLeft() {
	    }

	    public void onSwipeTop() {
	    }

	    public void onSwipeBottom() {
	    } }


}
