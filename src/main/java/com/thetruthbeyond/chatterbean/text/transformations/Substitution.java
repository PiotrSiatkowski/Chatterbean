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

import java.util.List;

public class Substitution {
	
	/* Inner Class Section */

	private interface FindReplaceOperation {
		boolean matches(int index, List<String> input);
		int replacement(int index, List<String> output);
	}

	private class FindReplaceFragment implements FindReplaceOperation {
		private final List<String> replacement = tokenizer.tokenize(replace);
		private final List<String> fragment;

		FindReplaceFragment(List<String> fragment) {
			this.fragment = fragment;
		}

		@Override
		public boolean matches(int index, List<String> input) {
			for(int i = 0, j = index, n = index + fragment.size(); j < n; i++, j++) {
				String token = fragment.get(i);
				String find = input.get(j);
				if(!find.equalsIgnoreCase(token))
					return false;
			}

			return true;
		}

		@Override
		public int replacement(int index, List<String> tokens) {
			for(int i = 0, n = fragment.size(); i < n; i++)
				tokens.remove(index);

			tokens.addAll(index, replacement);
			return replacement.size();
		}
	}

	private class FindReplacePrefix implements FindReplaceOperation {
		private String token;
		private String TOKEN;

		@Override
		public boolean matches(int index, List<String> input) {
			token = input.get(index);
			TOKEN = token.toUpperCase();
			return (TOKEN.indexOf(find) == 0);
		}

		@Override
		public int replacement(int index, List<String> tokens) {
			int beginIndex = find.length();
			List<String> replacement = tokenizer.tokenize(replace + token.substring(beginIndex));
			tokens.remove(index);
			tokens.addAll(index, replacement);
			return replacement.size();
		}
	}

	private class FindReplaceSuffix implements FindReplaceOperation {
		private String token;
		private String TOKEN;

		@Override
		public boolean matches(int index, List<String> input) {
			token = input.get(index);
			TOKEN = token.toUpperCase();
			return TOKEN.endsWith(find);
		}

		@Override
		public int replacement(int index, List<String> tokens) {
			int endIndex = TOKEN.lastIndexOf(find);
			List<String> replacement = tokenizer.tokenize(token.substring(0, endIndex) + replace);
			tokens.remove(index);
			tokens.addAll(index, replacement);
			return replacement.size();
		}
	}

	private class FindReplaceWord implements FindReplaceOperation {
		private final List<String> replacement = tokenizer.tokenize(replace);

		@Override
		public boolean matches(int index, List<String> input) {
			String token = input.get(index);
			return find.equalsIgnoreCase(token);
		}

		@Override
		public int replacement(int index, List<String> tokens) {
			tokens.remove(index);
			tokens.addAll(index, replacement);
			return replacement.size();
		}
	}

	/* Attribute Section */

	private FindReplaceOperation operation;

	private String find;
	private final String replace;

	private final Tokenizer tokenizer;

	/* Constructor Section */

	public Substitution(String find, String replace, Tokenizer tokenizer) {
		this.find = find;
		this.replace = replace;
		this.tokenizer = tokenizer;
		choseFindReplaceOperation();
	}

	private void choseFindReplaceOperation() {
		if(find == null || replace == null || tokenizer == null)
			return;

		List<String> tokens = tokenizer.tokenize(find);
		if(tokens.size() > 1)
			operation = new FindReplaceFragment(tokens);
		else if(find.charAt(0) != ' ')
			operation = new FindReplaceSuffix();
		else if(find.charAt(find.length() - 1) != ' ')
			operation = new FindReplacePrefix();
		else
			operation = new FindReplaceWord();

		find = find.toUpperCase().trim();
	}

	/* Method Section */

	public void substitute(List<String> input) {
		if(operation == null)
			throw new NullPointerException("Substitution state incomplete\n" + "Find: " + find + '\n' + "Replace: " + replace + '\n' + "Tokenizer: " + tokenizer);

		// The input size can change due to the
		// successive substitutions.
		for(int i = 0; i < input.size();)
		{
			if(operation.matches(i, input))
				i += operation.replacement(i, input);
			else
				i++;
		}
	}

	public int substitute(int offset, List<String> input) {
		if(operation == null)
			throw new NullPointerException("Substitution state incomplete\n" + "Find: " + find + '\n' + "Replace: " + replace + '\n' + "Tokenizer: " + tokenizer);

		if(operation.matches(offset, input))
			offset += operation.replacement(offset, input);

		return offset;
	}
}
