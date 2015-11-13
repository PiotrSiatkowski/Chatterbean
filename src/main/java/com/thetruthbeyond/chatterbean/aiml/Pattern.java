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

package com.thetruthbeyond.chatterbean.aiml;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.thetruthbeyond.chatterbean.text.structures.Transformations;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;

public class Pattern extends AIMLElement implements NormalizedTag {

	private static final int CAPACITY = 30;

	// Tag id functionality.
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static int Id; @Override public int getId(int level) { return level == 0 ? Id : super.getId(level-1); }

	private String pattern = "";
	private List<String> match;
  
	/* Constructor Section */
  
	public Pattern() {}
  
	public Pattern(String pattern) {
		this.pattern = pattern;
		match = Arrays.asList(pattern.split(" "));
	}
  
	public Pattern(Attributes attributes) {}
  
	/* Method Section */

	@Override
	public void normalizeContent(Transformations transformations) {
		pattern = transformations.makeSentence(pattern).getNormalized().trim();
		if(match != null)
			match = Arrays.asList(pattern.split(" "));
		else
			match = Arrays.asList(pattern.split(" "));
	}

	@Override
	public void appendChild(AIMLElement child) {		
		child.appendMeTo(this);
	}
	
	public void appendChild(Text child) {
		pattern = pattern + child;
		if(match != null)
			match.addAll(Arrays.asList(child.toString().split(" ")));
		else
			match = Arrays.asList(pattern.split(" "));
	}

	@Override
	public void appendMeTo(Category category) {
		category.appendChild(this);
	}

	@Override
	public Node getNode(Document document, Node parent, int indentLevel) {
		
		// Indent
		String indent = new String(new char[indentLevel]).replace("\0", BASE_INDENT);
		parent.appendChild(document.createTextNode("\n" + indent));
				
		Element pattern = document.createElement("pattern");
		pattern.setTextContent(this.pattern);
		
		return pattern;
	}
	
	public boolean isEmpty() {
		return match == null || match.isEmpty();
	}
	
	/* Properties */
	
	public List<String> getMatchPath() {
		return Collections.unmodifiableList(match);
	}

	public void setElements(String[] pattern) {
		match = Arrays.asList(pattern);
		this.pattern = toString();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Pattern) {
			Pattern compared = (Pattern) obj;
			if(match != null)
				return match.equals(compared.match);
			else
				return compared.match == null;
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return match.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder(CAPACITY);

		for(int i = 0, n = match.size(); i != n; ++i) {
			buffer.append(match.get(i));
			if(i != n - 1)
				buffer.append(" ");
		}

		return buffer.toString();
	}
}
