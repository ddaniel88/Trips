package com.example.crsautomotive;



//import com.example.firstapp.su  .support.appnavigation.app.ContentViewActivity;

//import com.example.tripplanner.*;

import java.io.IOException;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;

import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
//import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

public class AudioNews extends Activity implements  OnClickListener, OnTouchListener, OnCompletionListener, OnBufferingUpdateListener {
	
	private Button btn_play, btn_pause, btn_stop;
	private SeekBar seekBar;
	private MediaPlayer mediaPlayer;
	
	private int lengthOfAudio;
	private final String URL = "http://pmd.680news.com/podcasts/traffic/680traffic.mp3";
	private final Handler handler = new Handler();
	private final Runnable r = new Runnable() {	
		@Override
		public void run() {
			updateSeekProgress();					
		}
	};
	
	@Override
	public void onStop()
	{
		mediaPlayer.release();
		//mediaPlayer = null;
		//seekBar = null;
		handler.removeCallbacks(r);
		super.onStop();		
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
  public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      this.requestWindowFeature(Window.FEATURE_NO_TITLE);
      setContentView(R.layout.activity_audio_news);
		
      	//setTitle("Traffic Audio News");
   
      
      	btn_play = (Button)findViewById(R.id.btn_play);
		btn_play.setOnClickListener(this);
		btn_pause = (Button)findViewById(R.id.btn_pause);
		btn_pause.setOnClickListener(this);
		btn_pause.setEnabled(false);
		btn_stop = (Button)findViewById(R.id.btn_stop);
		btn_stop.setOnClickListener(this);
		btn_stop.setEnabled(false);
		
		seekBar = (SeekBar)findViewById(R.id.seekBar);
		seekBar.setOnTouchListener(this);
		
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnCompletionListener(this);
		
		View btn=findViewById(R.id.layout_trip);
        btn.setOnClickListener(this);
        btn=findViewById(R.id.layout_audio);
        btn.setOnClickListener(this);
        btn=findViewById(R.id.layout_cameras);
        btn.setOnClickListener(this);
		
        LinearLayout ll = (LinearLayout) findViewById(R.id.layout_audio);
        ll.setBackgroundResource(R.drawable.narandzasto);
        
  }

	@Override
	public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
		seekBar.setSecondaryProgress(percent);
	}

	@Override
	public void onCompletion(MediaPlayer mediaPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onClick(View view) {

	/*	try {
			mediaPlayer.setDataSource(URL);
			mediaPlayer.prepare();
			lengthOfAudio = mediaPlayer.getDuration();
		} catch (Exception e) {
			//Log.e("Error", e.getMessage());
		}*/
		
		Intent i;
		switch (view.getId()) {
		case R.id.btn_play:
			try {
				mediaPlayer.setDataSource(URL);
				mediaPlayer.prepare();
			} catch (Exception e) {
				//Log.e("Error", e.getMessage());
			}
			lengthOfAudio = mediaPlayer.getDuration();
			playAudio();
			break;
		case R.id.btn_pause:
			pauseAudio();
			break;
		case R.id.btn_stop:
			stopAudio();
			break;
		case R.id.layout_trip:
			i = new Intent(getApplicationContext(), TripLog.class);
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
		
		updateSeekProgress();
	}
	
	private void updateSeekProgress() {
		if (mediaPlayer.isPlaying()) {
			seekBar.setProgress((int)(((float)mediaPlayer.getCurrentPosition() / lengthOfAudio) * 100));
			handler.postDelayed(r, 1000);
		}
	}
	
	private void stopAudio() {
		mediaPlayer.pause();
		mediaPlayer.seekTo(0);
		btn_play.setEnabled(true);
		btn_pause.setEnabled(false);
		btn_stop.setEnabled(false);
		seekBar.setProgress(0);
	}

	private void pauseAudio() {
		mediaPlayer.pause();
		btn_play.setEnabled(true);
		btn_pause.setEnabled(false);
	}

	private void playAudio() {
		mediaPlayer.start();
		btn_play.setEnabled(false);
		btn_pause.setEnabled(true);
		btn_stop.setEnabled(true);
	}
  
  

}
