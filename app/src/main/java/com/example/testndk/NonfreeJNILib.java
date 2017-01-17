package com.example.testndk;

public class NonfreeJNILib {
	
	static 
    {
    	try
    	{ 
    		// Load necessary libraries.
    		System.loadLibrary("opencv_java");
    		System.loadLibrary("nonfree");
    		System.loadLibrary("detect_demo");
    	}
    	catch( UnsatisfiedLinkError e )
		{
           System.err.println("Native code library failed to load.\n" + e);		
		}
    }
	
	 public static native int InitDet();
	 
	 public static native void ReleaseTmp();
	 
	 public static native void ReleaseDet();
	 
	 public static native int DetLocate(byte[] img);
	 
	 public static native int DetFacePos(byte[] img, int[] rtnResult);
	 
	 public static native int DetEyeStatus(byte[] img, int[] rtnResult);
	 
	 public static native int DetMouthStatus(byte[] img, int[] rtnResult);

	 public static native void runDemo();
}
