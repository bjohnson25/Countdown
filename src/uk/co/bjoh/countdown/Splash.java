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

		Intent i = new Intent(Splash.this, MenuActivity.class);
		startActivity(i);
			
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}
	
}
