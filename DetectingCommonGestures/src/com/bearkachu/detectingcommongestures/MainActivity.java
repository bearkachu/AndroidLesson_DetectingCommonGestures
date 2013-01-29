package com.bearkachu.detectingcommongestures;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;

public class MainActivity extends Activity {
	// This example shows an activity, but you would use the same approach if
	// you were subclassing a View
	private static final String DEBUG_TAG = null;
	private CustomedTouchPad cTPad = new CustomedTouchPad();
	private long downTime;
	private long eventTime;
	private float x;
	private float pressure;
	private float y;
	private float size;
	private int metaState;
	private float xPrecision;
	private float yPrecision;
	private int deviceId;
	private int edgeFlags;
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
		int action = MotionEventCompat.getActionMasked(event);
		//Get USBManager from Android.
		UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
		// Find the first available driver.
		UsbSerialDriver driver = UsbSerialProber.acquire(manager);
		
		if (driver != null) {
			try {
				driver.open();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				driver.setBaudRate(57600);
				byte buffer[] = new byte[16];
				int numByteRead = driver.read(buffer, 1000);
				Log.d(DEBUG_TAG, "READ" + numByteRead + "bytes");
			} catch (IOException e) {
				//deal with error.
			} finally {
				try {
					driver.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
		switch(action) {
		case (MotionEvent.ACTION_DOWN):
			Log.d(DEBUG_TAG, "Action was DOWN");
		    return true;
		case (MotionEvent.ACTION_MOVE):
            cTPad.setX(event.getX());
            cTPad.setY(event.getY());
            // send USB-CDC data from here!! 
            System.out.println("x=" + cTPad.getX() + ", y=" + cTPad.getY() );
			Log.d(DEBUG_TAG, "Action was MOVE");
			return true;
		case (MotionEvent.ACTION_UP):
			Log.d(DEBUG_TAG, "Action was UP");
			return true;
		case (MotionEvent.ACTION_CANCEL):
			Log.d(DEBUG_TAG, "Action was CANCEL");
			return true;
		case (MotionEvent.ACTION_OUTSIDE):
			Log.d(DEBUG_TAG, "Movement occurred outside of bounds " + "of currnet screen element");
			return true;
		default:
			return super.onTouchEvent(event);	
		}
		

		
    }
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }    
}
