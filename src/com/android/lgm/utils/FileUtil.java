package com.android.lgm.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore.Files;
import android.util.Log;
/**
 * 
 * @author AN
 *
 */
public class FileUtil {
/*
 * field
 */
    //public
    private static final int DEFAULT_BUFFER_SIZE_IN_BYTES = 8 * 1024;
    private static final int LARGE_BUFFER_SIZE_IN_BYTES = 18 * 1024;
    private static byte[] buffer = new byte[DEFAULT_BUFFER_SIZE_IN_BYTES];
    public static final String USER_AGENT = "Mozilla/5.0";
    public static final int TIME_OUT=1000;
   // download file sound
    public static boolean downloadFile(String requestUrl,String outputPath){
        
    	try {
        	//Files.copy(url.openStream(), outputPath, StandardCopyOption.REPLACE_EXISTING);
        	FileUtils.copyURLToFile(new URL(requestUrl), new File(outputPath));
        	return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
    	
    	/*
    	InputStream reader = null;
        OutputStream writer = null;
        URLConnection connection=null;

        // Delete if file existed
//        File f = new File(outputPath);
//        if(f.exists()) f.delete();
        
        try {
             connection = new URL(requestUrl).openConnection();
             connection.setConnectTimeout(TIME_OUT);
             connection.setRequestProperty("Accept-Charset", "UTF-8");
             connection.setRequestProperty("User-Agent", USER_AGENT);
            
             
             reader = new BufferedInputStream(connection.getInputStream(),DEFAULT_BUFFER_SIZE_IN_BYTES);
             
             writer = new FileOutputStream(outputPath);

            int readByte = 0;
            synchronized (buffer) {
            	
                while ((readByte = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, readByte);
                }
                Arrays.fill(buffer, (byte) 0);
            }
            return true;
            
        } catch (Exception e) {
        	Log.i("download", e.getMessage());
            return false;
        } finally {
        	closeInputStream(reader);
         	closeOutputStream(writer);
	    }
	    
	    */
        
    }

//close input
    private static void closeInputStream(InputStream reader) throws Exception {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch(IOException ioe) {
            throw new Exception(ioe.getMessage());
        } 
    }
    //close output
    private static void closeOutputStream(OutputStream writer) throws Exception {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch(IOException ioe) {
            throw new Exception(ioe.getMessage());
        } 
    }
    
    public static void saveBitmapToSDCard(Bitmap bmp, String fname){
		File file = new File (Statics.LOGO_STORAGE, fname);
		if (file.exists ()) file.delete (); 
		try {
		       FileOutputStream out = new FileOutputStream(file);
		       bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
		       out.flush();
		       out.close();

		} catch (Exception e) {
		       e.printStackTrace();
		}
	}

    public static Bitmap loadResizedBitmap(String filename, int width, int height, boolean exact ) {
	    Bitmap bitmap = null;
	    BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(filename, options );
	    
	    if ( options.outHeight > 0 && options.outWidth > 0 ) {
	        options.inJustDecodeBounds = false;
	        options.inSampleSize = 2;
	        options.inPreferredConfig = Bitmap.Config.RGB_565;
	        while (    options.outWidth  / options.inSampleSize > width
	                && options.outHeight / options.inSampleSize > height ) {
	            options.inSampleSize++;
	        }
	        options.inSampleSize--;

	        bitmap = BitmapFactory.decodeFile( filename, options );
	        
	        if(bitmap != null && bitmap.getWidth() > bitmap.getHeight() && bitmap.getWidth() > width){
	        	bitmap = Bitmap.createScaledBitmap( bitmap, width, (int)(bitmap.getHeight() / (bitmap.getWidth()/width)), false );
	        	if(bitmap.getHeight() > height){
	        		bitmap = Bitmap.createScaledBitmap( bitmap, (int)(bitmap.getWidth() / (bitmap.getHeight()/height)), height, false );
	        	}
	        }else if(bitmap != null && bitmap.getHeight() > bitmap.getWidth() && bitmap.getHeight() > height){
	        	bitmap = Bitmap.createScaledBitmap( bitmap, (int)(bitmap.getWidth() / (bitmap.getHeight()/height)), height, false );
	        }
	        
	        if ( bitmap != null && exact ) {
	            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false );
	        }
	    }
	    if ( bitmap != null){
	    	Log.i("BITMAP SIZE",bitmap.getWidth()+" - "+bitmap.getHeight());
	    }
	    
	    
	    return bitmap;
	}
    public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFile(String filePath, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;		
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}
		
}
