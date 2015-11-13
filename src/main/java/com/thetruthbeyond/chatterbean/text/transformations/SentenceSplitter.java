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

package com.thetruthbeyond.chatterbean.text.transformations;

import com.thetruthbeyond.chatterbean.utility.annotations.Optimization;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.thetruthbeyond.chatterbean.text.transformations.Escaper.escapeRegex;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.UNICODE_CASE;

public class SentenceSplitter {

	/** Map of sentence-protection substitution patterns. */
	private final Map<String, String> protection;
	
	/** List of sentence-splitting patterns. */
	private final List<String> splitters;

	/** The regular expression which will split entries by sentence splitters. */
	private final Pattern splitPattern;

	@Optimization
	private final List<String> splitted = new LinkedList<>();

	public SentenceSplitter(Map<String, String> protection, List<String> splitters) {
		this.protection = protection;

		// Assing splitters accoridng to sequence length order.
		this.splitters = new ArrayList<>(splitters.size());
		for(String splitter : splitters) {
			if(this.splitters.isEmpty())
				this.splitters.add(splitter);
			else {
				for(int i = 0; i != this.splitters.size(); i++) {
					String temp = this.splitters.get(i);
					if(temp.length() > splitter.length()) {
						this.splitters.add(i, splitter);
						break;
					}

					if(i == this.splitters.size() - 1)
						this.splitters.add(temp);
				}
			}
		}

		StringBuilder pattern = new StringBuilder("\\s*(");
		for(String splitter : this.splitters) {
			pattern.append(escapeRegex(splitter));
			pattern.append("|");
		}

		if(!this.splitters.isEmpty())
			pattern.delete(pattern.length() - 1, pattern.length());
		pattern.append(")\\s*");
		splitPattern = Pattern.compile(pattern.toString());
	}
	
	public String[] splitOutput(String original) {
		splitted.clear();

		Matcher matcher = splitPattern.matcher(original);

		// Sentence begin index.
		int beginIndex = 0;

		while(matcher.find()) {
			if(isProtectedSequence(original, matcher.start(), matcher.end()))
				continue;

			String sentence = original.substring(beginIndex, matcher.end());
			if(!splitters.contains(sentence.trim()))
				splitted.add(sentence);
			beginIndex += sentence.length();
		}
		
		if(beginIndex < original.length()) {
			String sentence = original.substring(beginIndex, original.length());
			if(!splitters.contains(sentence))
				splitted.add(sentence);
		}

		return splitted.toArray( new String[splitted.size()] );
	}
	
	private boolean isProtectedSequence(String original, int beginIndex, int endIndex) {
		for(String find : protection.keySet()) {
			Pattern pattern = Pattern.compile(escapeRegex(find), CASE_INSENSITIVE | UNICODE_CASE);
			Matcher matcher = pattern.matcher(original);
			
			while(matcher.find()) {
				if(matcher.start() >= endIndex)
					break;
				if(matcher.end() < beginIndex)
					continue;
				
				// Check whether sequence between begin and end indexes is protected.
				if(beginIndex >= matcher.start() && endIndex < matcher.end() ||
				   beginIndex > matcher.start() && endIndex <= matcher.end())
					return true;
			}
		}
		
		return false;
	}
	
	public String[] splitInput(String original) {
		String prepared = protect(original);

		splitted.clear();

		Matcher matcher = splitPattern.matcher(prepared);

		// Sentence begin index.
		int beginIndex = 0;

		// Note that group(1) is only splitter (without whitespaces), when group() contains whole matched string.
		while(matcher.find()) {
			int endIndex = matcher.start(1);
			String sentence = prepared.substring(beginIndex, endIndex) + matcher.group(1);

			// If obtained sentence is in fact equal to one of the splitters.
			if(!splitters.contains(sentence.trim()))
				splitted.add(sentence);
			// Add the rest of the sentence.
			beginIndex = endIndex + matcher.end() - matcher.start(1);
		}

		if(beginIndex < prepared.length()) {
			String sentence = prepared.substring(beginIndex, prepared.length());
			if(!splitters.contains(sentence))
				splitted.add(sentence);
		}

		return splitted.toArray( new String[splitted.size()] );
	}
	
	private String protect(String input) {
		String protectedInput = input;
		for(Entry<String, String> entry : protection.entrySet()) {
			Pattern pattern = Pattern.compile(escapeRegex(entry.getKey()), CASE_INSENSITIVE | UNICODE_CASE);
			Matcher matcher = pattern.matcher(protectedInput);

			// Find any protected sequence and convert it to convenient word.
			protectedInput = matcher.replaceAll(entry.getValue());
		}

		return protectedInput;
	}
}
