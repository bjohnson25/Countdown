package uk.co.bjoh.countdown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ConundrumActivity extends Activity implements OnClickListener {

	// random characters, current guess, countdown timer & best guess
	TextView conundrum, guess, timer, round, overall;
	Button clear, submit;
	Button letters[] = new Button[8];
	int[] buttonViewIds = new int[8];
	String currentAttemptString, conundrumString;
	int curRound, overallScore;
	SharedPreferences prefs;
	CountDownTimer cdTimer;
	Words w;
	boolean fullGame;

	/**
	 * Called when the activity is first created.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.conundrum);
		Bundle b = getIntent().getExtras();
		fullGame = b.getBoolean("fullGame");
		for (int i = 0; i < letters.length; i++) {
			String buttonID = "bLetter" + (i + 1);
			int resID = getResources().getIdentifier(buttonID, "id",
					this.getPackageName());
			buttonViewIds[i] = resID;
			letters[i] = ((Button) findViewById(resID));
			letters[i].setOnClickListener(this);
		}
		w = new Words();
		clear = (Button) findViewById(R.id.bClear);
		submit = (Button) findViewById(R.id.bSubmit);
		timer = (TextView) findViewById(R.id.tvTimer);

		currentAttemptString = "";
		conundrumString = "";

		guess = (TextView) findViewById(R.id.tvGuess);
		round = (TextView) findViewById(R.id.tvRound);
		overall = (TextView) findViewById(R.id.tvOverall);
		conundrum = (TextView) findViewById(R.id.tvRandString);
		w.setConundrum();
		conundrumString = w.getConundrum();

		for (int i = 0; i < letters.length; i++) {
			letters[i].setText(w.getConundrumLetter(i));
		}
		if (fullGame) {
			// Shared Preferences
			prefs = PreferenceManager.getDefaultSharedPreferences(this);
			// Get current round
			curRound = prefs.getInt("round", 0);
			curRound++;
			// Get overall score
			overallScore = prefs.getInt("overall", 0);

			round.setText("\nRound " + curRound + " of 8");
			overall.setText("Current score: " + overallScore);
		}

		conundrum.setText(conundrumString);

		clear.setOnClickListener(this);
		submit.setOnClickListener(this);

		// Start the game
		startTimer();
	}

	/*
	 * Starts the game and provides intent for end of round
	 */
	private void startTimer() {
		cdTimer = new CountDownTimer(30000, 1000) {

			public void onTick(long millisUntilFinished) {
				timer.setText("Seconds Remaining: " + millisUntilFinished
						/ 1000);
			}

			public void onFinish() {
				timer.setText("Done! Verifying word...");
				endGame();
			}
		}.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)
				|| (keyCode == KeyEvent.KEYCODE_HOME)) {
			cdTimer.cancel();
			if (fullGame) {
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt("round", ++curRound);
				editor.putInt("overall", overallScore);
				editor.commit();
			}
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bSubmit:

			if (currentAttemptString.equals(w.getConundrum())) {
				endGame();
			} else {
				// clear method
				clear();
			}
			break;
		case R.id.bClear:
			clear();
			break;
		default:
			for (int i = 0; i < letters.length; i++) {
				// array of ints at top check if v matches then use i if they do
				if (v.getId() == buttonViewIds[i]) {
					currentAttemptString += letters[i].getText().toString();
					guess.setText(currentAttemptString);
					letters[i].setEnabled(false);
				}
			}
			break;
		}
	}

	/*
	 * Clears the current attempt and re-enables buttons
	 */
	private void clear() {
		currentAttemptString = "";
		guess.setText(currentAttemptString);
		for (int i = 0; i < letters.length; i++) {
			letters[i].setEnabled(true);
		}
	}

	/*
	 * 
	 */
	private void endGame() {

		int score = 0;

		if (currentAttemptString.equals(w.getConundrum())) {
			score = 10;
		}
		
		if (fullGame) {
			overallScore += score;
			Intent a = new Intent(ConundrumActivity.this, ResultsActivity.class);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("round", curRound);
			editor.putInt("overall", overallScore);
			editor.commit();
			Bundle b = new Bundle();
			b.putString("best", currentAttemptString);
			b.putInt("score", score);
			a.putExtras(b);
			startActivity(a);
			finish();
		} else {
			// TODO add a dialog or something similar
			finish();
		}
	}
}