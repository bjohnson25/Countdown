package uk.co.bjoh.countdown;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends Activity implements OnClickListener {

	Button game, newGame, stats, conundrum;
	SharedPreferences prefs;
	int gamesStart;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		game = (Button) findViewById(R.id.bGame);
		newGame = (Button) findViewById(R.id.bNewGame);
		stats = (Button) findViewById(R.id.bStats);
		conundrum = (Button) findViewById(R.id.bConundrum);

		game.setOnClickListener(this);
		newGame.setOnClickListener(this);
		stats.setOnClickListener(this);
		conundrum.setOnClickListener(this);
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		gamesStart = prefs.getInt("gamesStart", 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bNewGame:
			if (prefs.getInt("round", 0) > 0) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						this);
				alertDialogBuilder.setTitle("New Game");

				alertDialogBuilder
						.setMessage(
								"Are you sure you want to overwrite the current game?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										SharedPreferences.Editor editor = prefs
												.edit();
										editor.putInt("round", 0);
										editor.putInt("overall", 0);
										editor.putInt("gamesStart",
												++gamesStart);
										editor.commit();

										Intent l = new Intent(MenuActivity.this,
												LettersRoundActivity.class);
										startActivity(l);
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();
									}
								});
				// Create and show
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			} else if (prefs.getInt("round", 0) == 0) {
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt("round", 0);
				editor.putInt("overall", 0);
				editor.putInt("gamesStart", ++gamesStart);
				editor.commit();

				Intent l = new Intent(MenuActivity.this, LettersRoundActivity.class);
				Bundle b = new Bundle();
				b.putBoolean("fullGame", true);
				l.putExtras(b);
				startActivity(l);
			}

			break;
		case R.id.bGame:
			if (prefs.getInt("round", 0) == 0) {
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt("round", 0);
				editor.putInt("overall", 0);
				editor.putInt("gamesStart", ++gamesStart);
				editor.commit();
			}
			Intent i = new Intent(MenuActivity.this, LettersRoundActivity.class);
			Bundle b = new Bundle();
			b.putBoolean("fullGame", true);
			i.putExtras(b);
			startActivity(i);
			break;

		case R.id.bStats:
			Intent k = new Intent(MenuActivity.this, StatsActivity.class);
			startActivity(k);
			break;
			
		case R.id.bConundrum:
			Intent l = new Intent(MenuActivity.this, ConundrumActivity.class);
			Bundle bc = new Bundle();
			bc.putBoolean("fullGame", false);
			l.putExtras(bc);
			startActivity(l);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.help:
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
			alertDialogBuilder.setTitle("Help");

			alertDialogBuilder
					.setMessage(R.string.help)
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// Do nothing
								}
							});
			// Create and show
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
			break;
		case R.id.share:
			Intent share = new Intent(android.content.Intent.ACTION_SEND);
			share.setType("text/plain");
			share.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"Play Countdown");
			share.putExtra(android.content.Intent.EXTRA_TEXT,
					"Come and play this app!");
			startActivity(share);
			break;
		}
		return false;
	}
}