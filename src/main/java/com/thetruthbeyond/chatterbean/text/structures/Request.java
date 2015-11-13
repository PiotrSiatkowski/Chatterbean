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

public class Request {
	
	protected String wholeExpression;
	protected Sentence[] sentences;

	/* Construct */
	
	Request(Sentence... sentences) {
		this.sentences = sentences;

		StringBuilder builder = new StringBuilder(sentences.length * 20);
		for(int i = 0; i != sentences.length; i++)
			builder.append(sentences[i].getOriginal());
		
		// Cut spaces from begin and end of string and every double space.
		wholeExpression = builder.toString().replaceAll("\\s{2,}", " ").trim();
	}
	
	/* Methods */

	public String getWholeExpression() {
		return wholeExpression;
	}
	
	public Sentence getSentence(int index) {
		return sentences[index];
	}
	
	public boolean isWholeExpressionAnEmptyString() {
		return wholeExpression.isEmpty();
	}

	public int size() {
		return sentences.length;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Request))
			return false;

		Request compared = (Request) obj;
		return wholeExpression.equals(compared.wholeExpression) && Arrays.equals(sentences, compared.sentences);
	}

	@Override
	public String toString() {
		return wholeExpression;
	}
}