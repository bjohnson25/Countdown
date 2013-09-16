package uk.co.bjoh.countdown;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Application;

public class CountdownApplication extends Application 
{     
	public static String[] dictionary;
	public static String[] conundrums;
	
	public void onCreate(){
		super.onCreate();
		try {
			dictionary = loadText(R.raw.engwords);
			conundrums = loadText(R.raw.conundrums);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public String[] loadText(int resourceId) throws IOException {
		String[] text;
		// The InputStream opens the resourceId and sends it to the buffer
		InputStream is = getResources().openRawResource(resourceId);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String word = null;
		ArrayList<String> newDict = new ArrayList<String>();
		try {
			// While the BufferedReader readLine is not null
			while ((word = br.readLine()) != null) {
				newDict.add(word);
			}
			// Close the InputStream and BufferedReader
			
			is.close();
			br.close();

			text = newDict.toArray(new String[newDict.size()]);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return text;
	}
	
	
	public String[] getDictionary(){
		return dictionary;
	}
}