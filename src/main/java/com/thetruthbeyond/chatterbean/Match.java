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

package com.thetruthbeyond.chatterbean;

import java.util.*;

import com.thetruthbeyond.chatterbean.text.structures.Sentence;

import static com.thetruthbeyond.chatterbean.text.structures.Sentence.ASTERISK;

/**
 * Contains information about a match operation, which is needed by the classes
 * of the {@code com.thetruthbeyond.chatterbean.com.thetruthbeyond.chatterbean.aiml} to produce a proper response.
 */
public class Match {

	public static final String TOPIC_MATCH = "<TOPIC>";
	public static final String THAT_MATCH = "<THAT>";
	public static final String PATTERN_MATCH = "<PATTERN>";

	private final Map<Section, List<String>> sections = new EnumMap<>(Section.class);

	private AliceBot callback;

	private final Sentence pattern;
	private final Sentence that;
	private final Sentence topic;

	private List<String> matchPath;

	public Match(Sentence input) {
		this(null, input, ASTERISK, ASTERISK);
	}

	public Match(Sentence input, Sentence that) {
		this(null, input, that, ASTERISK);
	}

	public Match(AliceBot callback, Sentence pattern, Sentence that, Sentence topic) {
		sections.put(Section.TOPIC, new ArrayList<String>(2)); 	 // Topic 	wildcards.
		sections.put(Section.THAT, new ArrayList<String>(2)); 	 // That 	wildcards.
		sections.put(Section.PATTERN, new ArrayList<String>(2)); // Pattern wildcards.

		this.callback = callback;
		this.pattern = pattern;
		this.that = that;
		this.topic = topic;
		setUpMatchPath(pattern.getNormalizedWords(), that.getNormalizedWords(), topic.getNormalizedWords());
	}

	private void setUpMatchPath(String[] pattern, String[] that, String[] topic) {
		matchPath = new ArrayList<>(topic.length + that.length + pattern.length + 3);
		matchPath.add(TOPIC_MATCH);   matchPath.addAll(Arrays.asList(topic));
		matchPath.add(THAT_MATCH);    matchPath.addAll(Arrays.asList(that));
		matchPath.add(PATTERN_MATCH); matchPath.addAll(Arrays.asList(pattern));
	}

	public void appendWildcard(int begWord, int endWord) {

		begWord = begWord - 1;
		endWord = endWord - 1;

		int topicLength = topic.getNormalizedWordsSize();

		// Wildcard from topic.
		if(begWord < topicLength)
			appendWildcard(Section.TOPIC, topic, begWord, endWord);

		begWord = begWord - (topicLength + 1);
		endWord = endWord - (topicLength + 1);

		int thatLength = that.getNormalizedWordsSize();

		// Wildcard from that.
		if(begWord <= thatLength) {
			appendWildcard(Section.THAT, that, begWord, endWord);
			return;
		}

		begWord = begWord - (thatLength + 1);
		endWord = endWord - (thatLength + 1);

		// Wildcard from pattern.
		if(begWord <= pattern.getNormalizedWordsSize())
			appendWildcard(Section.PATTERN, pattern, begWord, endWord);
	}

	private void appendWildcard(Section section, Sentence source, int beginIndex, int endIndex) {
		if(beginIndex == endIndex)
			sections.get(section).add(0, "");
		else {
			try {
				sections.get(section).add(0, source.getOriginalFromBetweenNormalizedWords(beginIndex, endIndex));
			} catch(RuntimeException exception) {
				throw new RuntimeException("Source: {\"" + source.getOriginal() + "\", \"" + source.getNormalized() +
										   "\"}\n" + "Begin Index: " + beginIndex + "\n" + "End Index: " + endIndex, exception);
			}
		}
	}

	/**
	 * Gets the contents for the (index)th wildcard in the matched section.
	 */
	public String wildcard(Section section, int index) {
		List<String> wildcards = sections.get(section);
		return wildcards.get(index - 1);
	}

	/* Properties */

	public AliceBot getCallback() {
		return callback;
	}

	public void setCallback(AliceBot callback) {
		this.callback = callback;
	}

	@SuppressWarnings("ReturnOfCollectionOrArrayField")
	public List<String> getMatchPath() {
		return matchPath;
	}

	public String getMatchPath(int index) {
		return matchPath.get(index);
	}

	public int getMatchPathLength() {
		return matchPath.size();
	}
}
