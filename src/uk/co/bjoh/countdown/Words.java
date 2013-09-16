package uk.co.bjoh.countdown;

import java.util.Random;

/**
 * Words class
 */
public class Words {
	String[] letters;
	String conundrum;

	private String[] consArr = { "R", "S", "K", "L", "D", "L", "T", "V", "W",
			"X", "L", "L", "D", "H", "J", "L", "N", "N", "N", "Z", "P", "T",
			"B", "C", "T", "P", "Q", "R", "D", "F", "G", "S", "C", "D", "Y",
			"S", "R", "S", "P", "S", "M", "N", "S", "B", "C", "T", "F", "G",
			"R", "S", "T", "T", "R", "S", "D", "G", "H", "T", "R", "P", "D",
			"T", "M", "N", "T", "M", "R", "R", "N" };

	private String[] vowelsArr = { "A", "E", "I", "O", "U", "A", "E", "I", "O",
			"U", "A", "E", "I", "O", "U", "A", "E", "I", "O", "U", "A", "E",
			"I", "I", "O", "A", "O", "U", "O", "A", "E", "I", "O", "A", "E",
			"I", "O", "A", "E", "E", "I", "O", "A", "E", "I", "O", "A", "E",
			"I", "O", "A", "E", "A", "E", "A", "E", "I", "E", "A", "E", "I",
			"E", "I", "E", "E", "I", };

	public Words() {
		letters = new String[9];
	}

	public String[] generateLetters(int numOfCons, int numOfVowels) {

		Random randomGenerator = new Random();

		for (int i = 0; i < numOfCons; i++) {
			int index = randomGenerator.nextInt(69);
			while (consArr[index] == null) {
				index = randomGenerator.nextInt(69);
			}
			letters[i] = consArr[index];

			consArr[index] = null;
		}

		Random randomVowels = new Random();

		for (int i = numOfCons; i < letters.length; i++) {
			int index = randomVowels.nextInt(66);
			while (vowelsArr[index] == null) {
				index = randomVowels.nextInt(66);
			}
			// SET BUTTONS
			letters[i] = vowelsArr[index];
			vowelsArr[index] = null;
		}
		return letters;
	}

	public void setConundrum() {
		Random r = new Random();
		int i = r.nextInt(CountdownApplication.conundrums.length);
		conundrum = CountdownApplication.conundrums[i];
	}

	public String getConundrum() {
		return conundrum;
	}
	
	/*
	 * Return all letters for word game
	 */
	public String[] getLetters() {
		return letters;
	}

	/*
	 * Return letter i for a word game
	 */
	public String getLetter(int i) {
		return letters[i];
	}

	/*
	 * Return letter i for a conundrum 
	 */
	public String getConundrumLetter(int i) {
		String[] conundrumLetters = new String[8];
		conundrumLetters = conundrum.split("");

		return conundrumLetters[i + 1];
	}

}
