package uk.co.bjoh.countdown;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		try {
			LoadText(R.raw.engwords);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Intent i = new Intent(Splash.this, MainMenu.class);
		startActivity(i);
			
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
	
	public static String[] arr;

	public void LoadText(int resourceId) throws IOException {
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

			arr = newDict.toArray(new String[newDict.size()]);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
