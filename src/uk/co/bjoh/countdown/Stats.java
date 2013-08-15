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
import android.widget.TextView;

public class Stats extends Activity {

	SharedPreferences prefs;
	TextView stats;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.stats);

		stats = (TextView) findViewById(R.id.tvStats);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		stats.setText("Current game\n\nYou are currently in round "
				+ prefs.getInt("round", 0) + " of 8.\nYour current score is "
				+ prefs.getInt("overall", 0)
				+ ".\n\n\nOverall Stats\n\nYour highscore is "
				+ prefs.getInt("highscore", 0) + ".\nYou have played "
				+ prefs.getInt("totalRounds", 0) + " rounds in total.\n"
				+ "You have started " + prefs.getInt("gamesStart", 0) + " games so far.\n"
				+ "You have completed " + prefs.getInt("gamesComp", 0) + " games so far.");
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
