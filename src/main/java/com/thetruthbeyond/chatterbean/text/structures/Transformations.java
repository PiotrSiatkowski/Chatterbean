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

import com.thetruthbeyond.chatterbean.text.transformations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.UNICODE_CASE;

import static com.thetruthbeyond.chatterbean.text.structures.Sentence.EMPTY_SENTENCE;

/**
 * Provides operations for normalizing a request, before submitting it to the
 * matching operation.
 */
public class Transformations {
	
	private final Pattern normalizePattern = Pattern.compile("[^A-Z0-9 | ( * ) | ( _ )]+");

	// The regular expression which will split entries by sentence splitters.
	private final SentenceSplitter splitter;

	// The regular expression which will break entries by word breaker.
	private final WordBreaker breaker;

	// The collection of substitutions known to the system.
	private final Map<String,String> correction;
	private final List<Substitution> person;
	private final List<Substitution> person2;
	private final List<Substitution> gender;

	private final Tokenizer tokenizer;

	/**
	 * Constructs a new Transformations out of a list of sentence splitters and
	 * several substitution maps.
	 */
	public Transformations(List<String> splitters, List<String> breakers, Map<String, Map<String, String>> substitutions) {
		splitter = new SentenceSplitter(substitutions.get("protection"), splitters);
		breaker  = new WordBreaker(breakers);

		tokenizer = new Tokenizer(splitters, new ArrayList<String>(0), new ArrayList<String>(0));
		
		correction 	= substitutions.get("correction");
		person 		= newSubstitutionList(substitutions.get("person"));
		person2 	= newSubstitutionList(substitutions.get("person2"));
		gender 		= newSubstitutionList(substitutions.get("gender"));
	}

	private List<Substitution> newSubstitutionList(Map<String, String> inputs) {
		List<Substitution> substitutions = new ArrayList<>(inputs.size());
		for(Entry<String, String> entry : inputs.entrySet()) {
			Substitution substitution = new Substitution(entry.getKey(), entry.getValue(), tokenizer);
			substitutions.add(substitution);
		}

		return substitutions;
	}

	public Response makeResponse(String output) {
		String[] split = splitter.splitOutput(output);

		Sentence[] sentences = new Sentence[split.length];
		for(int i = 0, n = split.length; i < n; i++)
			sentences[i] = createSentence(unifyAndClear(split[i]));

		return new Response(sentences);
	}

	public Request makeRequest(String input) {
		String[] split = splitter.splitInput(input);

		Sentence[] sentences = new Sentence[split.length];
		for(int i = 0, n = sentences.length; i < n; i++)
			sentences[i] = createSentence(unifyAndClear(split[i]));

		return new Request(sentences);
	}

	public Sentence makeSentence(String input) {
		String cleared = unifyAndClear(input);
		return createSentence(cleared);
	}

	@SuppressWarnings("MethodMayBeStatic")
	private String unifyAndClear(String input) {
		// Spaces here to treat first and last words the same way as others.
		String unified = " " + input + " ";

		// Replace all redundant whitespaces to one space character.
		String cleared = unified.replaceAll("\\s{2,}", " ");
		if(cleared.equals(" "))
			return "";
		else
			return cleared;
	}

	private Sentence createSentence(String input) {
		if(input.isEmpty())
			return EMPTY_SENTENCE;

		String broke = breaker.breakWords(input);

		Mapper mapper = new Mapper(broke);

		String corrected = correct(broke, mapper);
		String normalized = normalize(corrected, mapper);

		normalized = clearWhitespacesWithMapper(normalized, mapper);

		return new Sentence(broke, mapper.obtainMappings(), normalized);
	}

	private String correct(String input, Mapper mapper) {
		StringBuffer buffer = new StringBuffer(input.length());

		String corrected = input;
		for(Entry<String, String> substitution : correction.entrySet()) {
			Pattern pattern = Pattern.compile(Escaper.escapeRegex(substitution.getKey()), CASE_INSENSITIVE | UNICODE_CASE);
			Matcher matcher = pattern.matcher(corrected);

			mapper.prepare(substitution.getKey(), substitution.getValue());
			while(matcher.find() && !matcher.hitEnd()) {
				mapper.updateMapping(matcher.start());
				matcher.appendReplacement(buffer, substitution.getValue());
			}

			matcher.appendTail(buffer);

			corrected = buffer.toString();
			buffer.delete(0, buffer.length());
		}

		return corrected;
	}

	/**
	 * Turns the entry to UPPERCASE, takes sequences of non-alphanumeric
	 * characters out of it (replacing them with a single whitespace) and sees
	 * that the entry is trimmed off leading and trailing whitespaces.
	 */
	private String normalize(String input, Mapper mapper) {
		String normalized = input.toUpperCase();
		StringBuffer result = new StringBuffer(normalized.length());

		Matcher matcher = normalizePattern.matcher(normalized);
		while(!matcher.hitEnd() && matcher.find()) {
			mapper.prepare(matcher.group(), " ");
			mapper.updateMapping(matcher.start());
			matcher.appendReplacement(result, " ");
		}

		matcher.appendTail(result);
		return result.toString();
	}

	private String clearWhitespacesWithMapper(String input, Mapper mapper) {
		StringBuffer buffer = new StringBuffer(input.length());

		String cleared = input;

		Pattern pattern = Pattern.compile(Escaper.escapeRegex("  "), CASE_INSENSITIVE | UNICODE_CASE);
		Matcher matcher = pattern.matcher(cleared);

		mapper.prepare("  ", " ");
		while(matcher.find() && !matcher.hitEnd()) {
			mapper.updateMapping(matcher.start());
			matcher.appendReplacement(buffer, " ");
		}

		matcher.appendTail(buffer);

		cleared = buffer.toString();
		buffer.delete(0, buffer.length());

		return cleared;
	}

	public String gender(String input) {
		return transform(input, gender);
	}

	public String person(String input) {
		return transform(input, person);
	}

	public String person2(String input) {
		return transform(input, person2);
	}
	
	private String transform(String input, List<Substitution> substitutions) {
		List<String> tokens = tokenizer.tokenize(input);

		for(int i = 0; i < tokens.size();) {
			int offset = i;
			for(Substitution substitution : substitutions) {
				i = substitution.substitute(offset, tokens);
				if(i > offset)
					break;
			}

			// Only gets here if no substitution matches.
			if(i <= offset)
				i++;
		}

		return tokenizer.composeString(tokens);
	}
}
