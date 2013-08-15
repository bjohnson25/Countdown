package uk.co.bjoh.countdown;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class GameScreen extends Activity implements OnClickListener {
    // random characters, current guess, countdown timer & best guess
    TextView randString, guess, timer, bestGuessTv, round, overall;
    Button clear, submit;
    Button letters[] = new Button[10];
    int[] buttonViewIds = new int[10];
    String s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, sGuess, bestGuess;
    String[] s = new String[10];
    int curRound, overallScore;
    SharedPreferences prefs;
    CountDownTimer cdTimer;

    final int CONSTONANTS = 6;
    final int VOWELS = 4;

    /**
     * Called when the activity is first created.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        for (int i = 0; i < letters.length; i++) {
            String buttonID = "bLetter" + (i + 1);
            int resID = getResources().getIdentifier(buttonID, "id", this.getPackageName());
            buttonViewIds[i] = resID;
            letters[i] = ((Button) findViewById(resID));
            letters[i].setOnClickListener(this);
        }

        clear = (Button) findViewById(R.id.bClear);
        submit = (Button) findViewById(R.id.bSubmit);
        timer = (TextView) findViewById(R.id.tvTimer);

        sGuess = "";
        bestGuess = "";

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

        String s = (genVowels() + genCons());

        randString.setText(s.replace("", "  ").trim());
        round.setText("\nRound " + curRound + " of 8");
        overall.setText("Current score: " + overallScore);

        clear.setOnClickListener(this);
        submit.setOnClickListener(this);

        cdTimer = new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText("Seconds Remaining: " + millisUntilFinished
                        / 1000);
            }

            public void onFinish() {

                timer.setText("Done! Verifying word...");
                int validWord = searchDict(bestGuess);
                int score = 0;
                Intent a = new Intent(GameScreen.this, ResultsScreen.class);

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
                // TODO stuff
                bestGuess = sGuess;

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
                    //array of ints at top check if v matches then use i if they do
                    if (v.getId() == buttonViewIds[i]) {
                        sGuess += letters[i].getText().toString();
                        guess.setText(sGuess);
                        letters[i].setEnabled(false);
                    }
                }
                break;

//            case R.id.bLetter10:

        }
    }

    private void clear() {
        sGuess = "";
        guess.setText(bestGuess);
        for (int i = 0; i < letters.length; i++) {
            letters[i].setEnabled(true);
        }
    }

    private String genCons() {
        String cons = "";
        String[] consArr = {"K", "L", "D", "L", "L", "R", "D", "F", "G", "S",
                "L", "D", "H", "J", "L", "N", "N", "N", "Z", "P", "T", "B",
                "C", "T", "P", "Q", "R", "R", "R", "R", "S", "S", "S", "S",
                "B", "C", "T", "F", "G", "R", "S", "T", "T", "R", "S", "D",
                "G", "H", "T", "R", "P", "D", "P", "S", "M", "N", "T", "M",
                "N", "T", "M", "N", "T", "V", "W", "X", "C", "D", "Y"};
        Random randomGenerator = new Random();

        for (int i = 0; i < CONSTONANTS; i++) {

            int index = randomGenerator.nextInt(69);
            while (consArr[index] == null) {
                index = randomGenerator.nextInt(69);
            }
            cons = cons + consArr[index];
            // SET BUTTONS
            letters[i + VOWELS].setText(consArr[index]);

            consArr[index] = null;
        }
        return cons;
    }

    private String genVowels() {
        String vowels = "";
        String[] vowelsArr = {"A", "E", "I", "O", "U", "A", "E", "I", "O",
                "U", "A", "E", "I", "O", "U", "A", "E", "I", "O", "U", "A",
                "E", "I", "I", "O", "A", "O", "U", "O", "A", "E", "I", "O",
                "A", "E", "I", "O", "A", "E", "E", "I", "O", "A", "E", "I",
                "O", "A", "E", "I", "O", "A", "E", "A", "E", "A", "E", "I",
                "E", "A", "E", "I", "E", "I", "E", "E", "E", "I",};

        Random randomGenerator = new Random();

        for (int i = 0; i < VOWELS; i++) {
            int index = randomGenerator.nextInt(66);
            while (vowelsArr[index] == null) {
                index = randomGenerator.nextInt(66);
            }

            vowels = vowels + vowelsArr[index];

            // SET BUTTONS
            letters[i].setText(vowelsArr[index]);

            vowelsArr[index] = null;
        }
        return vowels;
    }

    public void LoadText(int resourceId) throws IOException {
        long startTime = System.currentTimeMillis();
        String[] arr;

        InputStream is = getResources().openRawResource(resourceId);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String word = null;
        ArrayList<String> newDict = new ArrayList<String>();
        try {

            while ((word = br.readLine()) != null) {
                // if (word.length() < 10) {
                newDict.add(word);
                // }
            }

            is.close();
            br.close();

            arr = newDict.toArray(new String[newDict.size()]);

            long endTime = System.currentTimeMillis();
            long total = endTime - startTime;
            bestGuessTv.setText("..." + total);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int searchDict(String bestGuess) {
        return Arrays.binarySearch(Splash.arr, bestGuess);
    }
}