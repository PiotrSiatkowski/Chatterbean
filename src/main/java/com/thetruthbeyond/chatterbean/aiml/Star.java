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

import com.thetruthbeyond.chatterbean.Section;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;

import com.thetruthbeyond.chatterbean.Match;

public class Star extends TemplateElement {

	// Tag id functionality.
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static int Id; @Override public int getId(int level) { return level == 0 ? Id : super.getId(level-1); }
	
	private final int index;
  
	public Star(Attributes attributes) {
		String value = attributes.getValue(0);
		index = value != null ? Integer.parseInt(value) : 1;
	}
  
	public Star(int index) {
		this.index = index;
	}

	/* Methods */

	@Override
	public void appendChild(AIMLElement child) {
		child.appendMeTo(this);
	}

	@Override
	public String process(Match match) {
		String wildcard = match.wildcard(Section.PATTERN, index);
    	return wildcard != null ? wildcard.trim() : "";
	}
	
	@Override
	public Node getNode(Document document, Node parent, int indentLevel) {
		Element element = document.createElement("star");

		element.setAttribute("index", String.valueOf(index));

		return element;
	}
	
	@Override
	public boolean equals(Object object) {
		if(object instanceof Star) {
			Star star = (Star) object;
			return index == star.index;
		} else
			return false;
	}

	@Override
	public int hashCode() {
		return index;
	}
  
	@Override
	public String toString() {
		return "<star index=\"" + index + "\"/>";
	}
}
