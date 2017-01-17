package utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class PicProcessUtils {

	public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
		//旋转图片 动作
		Matrix matrix = new Matrix();;
		matrix.postRotate(angle);
		System.out.println("angle2=" + angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}
	public static void decodeYUV420SP(byte[] pbSrcImgData, byte[] yuv420sp, int width, int height) {   	              
	    final int frameSize = width * height;	  
	    int nPixel =0 ;
	    for (int j = 0, yp = 0; j < height; j++) {       
	        int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;   
	        for (int i = 0; i < width; i++, yp++) {   
	           int y = (0xff & ((int) yuv420sp[yp])) - 16;   
	           if (y < 0)  y = 0;   
	           if ((i & 1) == 0) {   
	               v = (0xff & yuv420sp[uvp++]) - 128;   
	               u = (0xff & yuv420sp[uvp++]) - 128;   
	           }	  
	           int y1192 = 1192 * y;   
	           int r = (y1192 + 1634 * v);   
	           int g = (y1192 - 833 * v - 400 * u);   
	           int b = (y1192 + 2066 * u);   
	  
	           if (r < 0)    r = 0;
	           else if (r > 262143)  r = 262143;   
	           if (g < 0)    g = 0;
	           else if (g > 262143)  g = 262143;   
	           if (b < 0)    b = 0;
	           else if (b > 262143)  b = 262143;   
	  
	           nPixel = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff); 
	           pbSrcImgData[yp * 3 + 0] =  (byte) (nPixel & 0xff);
	           pbSrcImgData[yp * 3 + 1] = (byte) (nPixel >> 8 & 0xff);
	           pbSrcImgData[yp * 3 + 2] = (byte) (nPixel >> 16 & 0xff);
	        }   
	    }   
	}
	
	 public static void rotateYUV240SP_Clockwise(byte[] src,byte[] des,int width,int height)  
	 { 
		 	int wh = width * height;
	        //旋转Y   
	        int k = 0;  
	        for(int i=0;i<width;i++) {  
	            for(int j=0;j<height;j++)   
	            {  
	                  des[k] = src[width*(height-j-1) + i];              
	                  k++;  
	            }  
	        }  
	          
	        for(int i=0;i<width;i+=2) {  
	            for(int j=0;j<height/2;j++)   
	            {     
	                  des[k] = src[wh+ width*(height/2-j-1) + i];      
	                  des[k+1]=src[wh + width*(height/2-j-1) + i+1];  
	                  k+=2;  
	            }  
	        }            
	          
	 }
	 public static void rotateYUV240SP_AntiClockwise(byte[] src,byte[] des,int width,int height)  
	 {  	         
	        int wh = width * height;  
	        //旋转Y   
	        int k = 0;  
	        for(int i=0;i<width;i++) {  
	            for(int j=0;j<height;j++)   
	            {  
	                  des[k] = src[width*j + width-i-1];              
	                  k++;  
	            }  
	        }  
	          
	        for(int i=0;i<width;i+=2) {  
	            for(int j=0;j<height/2;j++)   
	            {     
	                  des[k+1] = src[wh+ width*j + width-i-1];      
	                  des[k]=src[wh + width*j + width-(i+1)-1];  
	                  k+=2;  
	            }  
	        } 
	          
	}

	 public static void rotateYUV240SP_FlipY180(byte[] src,byte[] des,int width,int height)  
	 {  	         
	        int wh = width * height;  
	        //旋转Y   
	        int k = 0;  
	       for(int i=0;i<height;i++) {  
	            for(int j=0;j<width;j++)   
	            {  
	                  des[k] = src[width*(height-i-1) + j];              
	                  k++;  
	            }  
	        }  	     
	        for(int i=0;i<height/2;i++) {  
	            for(int j=0;j<width;j+=2)   
	            {     
	            	  des[k]= src[wh + width*(height/2 -i-1) + j];  
	                  des[k+1] = src[wh+ width*(height/2 -i-1) + j+1];   		                 
	                  k+=2;  
	            }  
	        } 
	          
	}
	 //it works becuase in YCbCr_420_SP and YCbCr_422_SP, the Y channel is planar and appears first
    public static void rotateYuvData(byte[] rotatedData, byte[] data, int width, int height,int nCase)
    {
    	if( nCase == 0)
    	{
    		rotateYUV240SP_Clockwise(data,rotatedData,width,height);
    	}
    	else if(nCase == 2)
    	{    		
    		rotateYUV240SP_FlipY180(data,rotatedData,width,height);
    	}  	
    	else 
    	{    		
    		rotateYUV240SP_AntiClockwise(data,rotatedData,width,height);
    	}  	
      
    }

}
