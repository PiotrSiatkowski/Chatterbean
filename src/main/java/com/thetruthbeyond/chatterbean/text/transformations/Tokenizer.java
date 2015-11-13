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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.thetruthbeyond.chatterbean.text.transformations.Escaper.escapeRegex;

public class Tokenizer {

	private final Set<String> splitters = new HashSet<>(8);
	private final Set<String> breakersL = new HashSet<>(8);
	private final Set<String> breakersR = new HashSet<>(8);
	
	private boolean ignoreWhitespace = false;
	private Pattern splitPattern;
	private Pattern breakPattern;

	public Tokenizer(String[] splitters, String[] breakersL, String[] breakersR) {
		Collections.addAll(this.splitters, splitters);
		Collections.addAll(this.breakersL, breakersL);
		Collections.addAll(this.breakersR, breakersR);

		createSplitPattern();
		createBreakPattern();
	}


	public Tokenizer(List<String> splitters, List<String> breakersL, List<String> breakersR) {
		this.splitters.addAll(splitters);
		this.breakersL.addAll(breakersL);
		this.breakersR.addAll(breakersR);
		createSplitPattern();
		createBreakPattern();
	}

	private void createSplitPattern() {
		if(splitters.isEmpty())
			return;

		StringBuilder expression = new StringBuilder(2 * splitters.size());
		
		Iterator<String> iterator = splitters.iterator();
		while(true) {
			expression.append(escapeRegex(iterator.next()));
			if(!iterator.hasNext())
				break;
			expression.append('|');
		}

		String pattern;
		if(ignoreWhitespace)
			pattern = "(" + expression + ")\\s*|\\s+";
		else
			pattern = "(" + expression + "|\\s+)";

		splitPattern = Pattern.compile(pattern);
	}

	private void createBreakPattern() {
		StringBuilder expression = new StringBuilder(2 * breakersL.size() + 2 * breakersR.size());
		Iterator<String> iterator;
		
		iterator = breakersL.iterator();
		while(iterator.hasNext()) {
			expression.append(escapeRegex(iterator.next()));
			if(!iterator.hasNext())
				break;
			expression.append('|');
		}	
		
		iterator = breakersR.iterator();
		while(iterator.hasNext()) {
			expression.append('|');
			expression.append(escapeRegex(iterator.next()));
		}
		
		String pattern = "(" + expression + ")";
		breakPattern = Pattern.compile(pattern);
	}
	
	/* Method Section */

	public List<String> tokenize(String input) {
		List<String> output = new ArrayList<>(10);
		
		int beginIndex = 0;
		Matcher matcher = splitPattern.matcher(input);
		
		while(matcher.find()) {
			int endIndex = matcher.start();
			String token = input.substring(beginIndex, endIndex);
			if(!token.isEmpty()) {
				List<String> subwords = tokenizeWord(token);
				output.addAll(subwords);
			}

			String symbol = matcher.group(1);
			if(symbol != null) {
				output.add(symbol);
			}

			String breaker = matcher.group();
			beginIndex = endIndex + breaker.length();
		}

		if(beginIndex < input.length()) {
			String token = input.substring(beginIndex);
			List<String> subwords = tokenizeWord(token);
			output.addAll(subwords);
		}

		return output;
	}
	
	public List<String> tokenizeWord(String input) {
		List<String> output = new ArrayList<>(10);
		
		int beginIndex = 0;
		Matcher matcher = breakPattern.matcher(input);
		
		StringBuilder token = new StringBuilder(15);
		
		while(matcher.find()) {
			int endIndex = matcher.start();
			
			token = token.append(input.substring(beginIndex, endIndex));

			String symbol = matcher.group(1);
			if(symbol != null) {
				if(breakersR.contains(symbol)) {
					output.add(token + symbol);
					token.delete(0, token.length());
				} else {
					if(token.length() != 0)
						output.add(token.toString());
					token.replace(0, token.length(), symbol);
				}	
			}

			String breaker = matcher.group();
			beginIndex = endIndex + breaker.length();
		}

		if(beginIndex < input.length()) {
			token.append(input.substring(beginIndex));
			output.add(token.toString());
		} else if(token.length() != 0)
			output.add(token.toString());

		return output;
	}

	public String composeString(List<String> tokens) {
		StringBuilder output = new StringBuilder(15);
		
		String next = tokens.get(0);
		for(int i = 0, n = tokens.size();;) {
			output.append(next);
			if(++i >= n)
				break;
			next = tokens.get(i);
			Matcher matcher = splitPattern.matcher(next);
			if(!matcher.matches())
				output.append(' ');
		}

		return output.toString();
	}

	public void setIgnoreWhitespace(boolean ignore) {
		ignoreWhitespace = ignore;
		createSplitPattern();
	}

	public void setSplitters(String... splitters) {
		createSplitPattern();
	}
	
	public void setLeftBreakers(String... breakers) {
		Collections.addAll(breakersL, breakers);
		createBreakPattern();
	}
	
	public void setRightBreakers(String... breakers) {
		Collections.addAll(breakersR, breakers);
		createBreakPattern();
	}

	@Override
	public String toString() {
		return splitters.toString() + " " + breakersR.toString() + " " + breakersL.toString();
	}
}
