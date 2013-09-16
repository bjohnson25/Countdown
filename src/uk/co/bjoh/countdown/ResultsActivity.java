package uk.co.bjoh.countdown;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultsActivity extends Activity {

	Button next, menu;
	TextView resultText, roundScore;
	int score, round, overall, curHighscore, totalRounds, gamesComp;
	String best;
	SharedPreferences prefs;
	SharedPreferences.Editor editor;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.results);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		editor = prefs.edit();

		Bundle b = getIntent().getExtras();
		score = b.getInt("score");
		best = b.getString("best");

		next = (Button) findViewById(R.id.bNext);
		menu = (Button) findViewById(R.id.bMenu);
		resultText = (TextView) findViewById(R.id.tvResults);
		roundScore = (TextView) findViewById(R.id.tvRaS);

		if (best.length() == 0) {
			resultText
					.setText("You did not submit a guess! You scored 0 this round.");
		} else if (score == 0) {
			resultText
					.setText("Your guess was incorrect! You scored 0 points this round.");
		} else {
			resultText.setText("Your best guess was " + best
					+ " which scored you " + score + " points.");
		}

		round = prefs.getInt("round", 0);
		overall = prefs.getInt("overall", 0);
		curHighscore = prefs.getInt("highscore", 0);
		totalRounds = prefs.getInt("totalRounds", 0);
		gamesComp = prefs.getInt("gamesComp", 0);

		if (round >= 8) {
			if (score == 0) {
				resultText.setText("You failed to solve the conundrum");
			} else {
				resultText
						.setText("Congratuations! You solved the Countdown conundrum which earned you 10 points!");
			}

			roundScore.setText("Game Over! \nYour final score was " + overall);

			next.setText("New Game");
			next.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					editor.putInt("totalRounds", ++totalRounds);
					editor.putInt("round", 0);
					editor.putInt("overall", 0);
					editor.putInt("gamesComp", ++gamesComp);
					if (overall > curHighscore) {
						editor.putInt("highscore", overall);
					}
					editor.commit();
					Intent a = new Intent(ResultsActivity.this,
							MenuActivity.class);
					startActivity(a);
				}
			});
			menu.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					editor.putInt("totalRounds", ++totalRounds);
					editor.putInt("round", 0);
					editor.putInt("overall", 0);
					editor.putInt("gamesComp", ++gamesComp);
					if (overall > curHighscore) {
						editor.putInt("highscore", overall);
					}
					editor.commit();
					Intent a = new Intent(ResultsActivity.this,
							MenuActivity.class);
					startActivity(a);
				}
			});
		} else if (round == 7) {
			roundScore.setText("Round " + round + " of 8\nCurrent Score: "
					+ overall);

			editor.putInt("totalRounds", ++totalRounds);
			editor.commit();
			next.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent a = new Intent(ResultsActivity.this,
							ConundrumActivity.class);
					Bundle b = new Bundle();
					b.putBoolean("fullGame", true);
					a.putExtras(b);
					startActivity(a);
				}
			});
			menu.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent a = new Intent(ResultsActivity.this,
							MenuActivity.class);
					startActivity(a);
				}
			});

		} else {
			roundScore.setText("Round " + round + " of 8\nCurrent Score: "
					+ overall);

			editor.putInt("totalRounds", ++totalRounds);
			editor.commit();
			next.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent a = new Intent(ResultsActivity.this,
							LettersRoundActivity.class);
					startActivity(a);
				}
			});
			menu.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent a = new Intent(ResultsActivity.this,
							MenuActivity.class);
					startActivity(a);
				}
			});
		}

	}
}