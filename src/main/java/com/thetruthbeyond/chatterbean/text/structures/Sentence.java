/*
 * Copyleft (C) 2015 Piotr Siatkowski find me on Facebook;
 * Copyleft (C) 2005 Helio Perroni Filho xperroni@yahoo.com ICQ: 2490863;
 * This file is part of BotMaker. BotMaker is free software; you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version. BotMaker is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details. You should have received a
 * copy of the GNU General Public License along with BotMaker (look at the
 * Documents directory); if not, either write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA, or visit
 * (http://www.gnu.org/licenses/gpl.txt).
 */

package com.thetruthbeyond.chatterbean.text.structures;

import java.util.Arrays;

public class Sentence {

	private final String original;
	private final Integer[] mappings; // The index mappings of normalized elements to

	private final String normalized;
	private final String[] splitted; // The normalized entry, splitted in an array of words.
	
	public static final Sentence ASTERISK = new Sentence(" * ", new Integer[] { 0, 1, 2 }, " * ");
	public static final Sentence EMPTY_SENTENCE = new Sentence("", new Integer[] {}, "");

	Sentence(String original, Integer[] mappings, String normalized) {
		this.original = original;
		this.mappings = mappings;
		this.normalized = normalized;

		splitted = normalized.trim().split(" ");
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Sentence))
			return false;

		Sentence compared = (Sentence) obj;
		return (original.equals(compared.original) && Arrays.equals(mappings, compared.mappings) 
				                                   && normalized.equals(compared.normalized));
	}

	/** Gets the number of individual words contained by the Sentence. */
	public int getNormalizedWordsSize() {
		return splitted.length;
	}

	/** Returns the normalized as an array of String words. */
	public String[] getNormalizedWords() {
		return Arrays.copyOf(splitted, splitted.length);
	}

	/** Gets the (index)th word of the Sentence, in its normalized form. */
	public String getNormalizedWord(int index) {
		return splitted[index];
	}

	public String getOriginalFromBetweenNormalizedWords(int begWord, int endWord) {
		int word = 0, beg = 0, end = 0;
		for(int i = 0, n = normalized.length(); i != n; ++i)
			if(normalized.charAt(i) == ' ') {
				if(word == begWord)
					beg = i;
				else

				if(word == endWord)
					end = i;
				word++;
			}

		return original.substring(mappings[beg], mappings[end]).replaceAll("^[^A-Za-z0-9]+|[^A-Za-z0-9]+$", " ");
	}

	public String getOriginal() {
		return original;
	}

	public String getNormalized() {
		return normalized;
	}
	
	public boolean isEmpty() {
		return equals(EMPTY_SENTENCE);
	}
	
	/**
	 * Returns a string representation of the Sentence. This is useful for
	 * printing the state of Sentence objects during tests.
	 * 
	 * @return A string formed of three bracket-separated sections: the original
	 *         sentence string, the normalized-to-original word mapping array,
	 *         and the normalized string.
	 */
	@Override
	public String toString() {
		return "[" + original + "]" + Arrays.toString(mappings) + "[" + normalized + "]";
	}
}
