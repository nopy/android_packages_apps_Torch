package net.cactii.flash2;

import android.os.Build;

import android.util.Log;

import java.io.FileWriter;
import java.io.IOException;

public class FlashDevice {

    private static String TAG = "FlashDevice" ;
	
    public static final int STROBE    = -1;
	public static final int OFF       = 0;
	public static final int ON        = 1;
	public static final int DEATH_RAY = 3;
	public static final int HIGH      = 128;
	
	private static FlashDevice instance;
	
	private static boolean useDeathRay = !Build.DEVICE.equals("supersonic");
	
	private boolean opened = false ;
	
	private int mFlashMode = OFF;
	
	private FlashDevice() {}
	
	public static synchronized FlashDevice instance() {
	    if (instance == null) {
	        instance = new FlashDevice();
	    }
	    return instance;
	}
	
	public synchronized void setFlashMode(int mode) {	
	        if (! opened) {
	            openFlash() ;
		    opened = true ;		    
	        }
	        
	        int value = mode;
	        switch (mode) {
		    case ON :
		      if( mFlashMode == OFF || mFlashMode == STROBE ) {
			setFlashOn() ;
		      }
		      break;
	            case STROBE:
	                setFlashOff() ;
    
	                break;
	            case DEATH_RAY:
		      if( mFlashMode == OFF || mFlashMode == STROBE ) {
			setFlash2On() ;
		      }
	                break;
	        }
	        //mWriter.write(String.valueOf(value));
	        //mWriter.flush();
	       
	        
	        if (mode == OFF) {
		    if(mFlashMode == DEATH_RAY ) {
		      setFlash2Off() ;
		    }  else {
		      setFlashOff() ;
		    }
	            closeFlash() ;
	            opened = false ;
	        }
		mFlashMode = mode;
	}

	public synchronized int getFlashMode() {
	    return mFlashMode;
	}
    
	public static native String openFlash() ;
	public static native String closeFlash() ;

	public static native String setFlashOn() ;
	public static native String setFlashOff() ;
	public static native String setFlash2On() ;
	public static native String setFlash2Off() ;
	public static native String setFlashLevel( int level ) ;
    // Load libflash once on app startup.
    static {
    System.loadLibrary("jni_flash");
    }
}
