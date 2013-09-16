package uk.co.bjoh.countdown;

import java.util.Arrays;

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

public class LettersRoundActivity extends Activity implements OnClickListener {

	private final int CONSONANTS = 6;
	private final int VOWELS = 3;
	// SETUP SINGLETON
	// random characters, current guess, countdown timer & best guess
	TextView randString, guess, timer, bestGuessTv, round, overall;
	Button clear, submit;
	Button letters[] = new Button[9];
	int[] buttonViewIds = new int[9];
	String currentAttemptString, bestGuess, randomString;
	int curRound, overallScore;
	SharedPreferences prefs;
	CountDownTimer cdTimer;
	Words w;

	// static boolean dialogGone;
	/**
	 * Called when the activity is first created.
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);

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
		bestGuess = "";
		randomString = "";

		bestGuessTv = (TextView) findViewById(R.id.tvBest);
		guess = (TextView) findViewById(R.id.tvGuess);
		round = (TextView) findViewById(R.id.tvRound);
		overall = (TextView) findViewById(R.id.tvOverall);
		randString = (TextView) findViewById(R.id.tvRandString);

		// Shared Preferences
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		// Get current round
		curRound = prefs.getInt("round", 0);
		curRound++;
		// Get overall score
		overallScore = prefs.getInt("overall", 0);

		w.generateLetters(CONSONANTS, VOWELS);

		round.setText("\nRound " + curRound + " of 8");
		overall.setText("Current score: " + overallScore);

		clear.setOnClickListener(this);
		submit.setOnClickListener(this);

		// LetterGameDialog lgd = new LetterGameDialog();
		// lgd.show(getFragmentManager(), "");
		// if(dialogGone){
		startTimer();
		// }

	}

	public void startTimer() {
		for (int i = 0; i < letters.length; i++) {
			randomString += w.getLetter(i);
			letters[i].setText(w.getLetter(i));
		}

		randString.setText(randomString);
		cdTimer = new CountDownTimer(30000, 1000) {

			public void onTick(long millisUntilFinished) {
				timer.setText("Seconds Remaining: " + millisUntilFinished
						/ 1000);
			}

			public void onFinish() {

				timer.setText("Done! Verifying word...");
				int validWord = searchDict(bestGuess);
				int score = 0;
				Intent a = new Intent(LettersRoundActivity.this,
						ResultsActivity.class);

				if (validWord > 0) {
					score = bestGuess.length();
				} else {
					score = 0;
				}
				overallScore += score;
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt("round", curRound);
				editor.putInt("overall", overallScore);
				editor.commit();
				Bundle b = new Bundle();
				b.putString("best", bestGuess);
				b.putInt("score", score);
				a.putExtras(b);
				startActivity(a);
				finish();

			}
		}.start();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)
				|| (keyCode == KeyEvent.KEYCODE_HOME)) {
			cdTimer.cancel();
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("round", ++curRound);
			editor.putInt("overall", overallScore);
			editor.commit();
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bSubmit:
			bestGuess = currentAttemptString;

			bestGuessTv.setText("Your current guess is " + bestGuess + " ("
					+ bestGuess.length()
					+ " points), but make sure it's a valid word!");
			// clear method
			clear();
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
		guess.setText(bestGuess);
		for (int i = 0; i < letters.length; i++) {
			letters[i].setEnabled(true);
		}
	}

	public static int searchDict(String bestGuess) {
		return Arrays.binarySearch(CountdownApplication.dictionary, bestGuess);
	}

	// public static void setDialog(boolean b){
	// dialogGone = b;
	// }
}