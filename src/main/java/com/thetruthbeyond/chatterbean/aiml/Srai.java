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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;

import com.thetruthbeyond.chatterbean.AliceBot;
import com.thetruthbeyond.chatterbean.Match;

import java.util.List;

public class Srai extends TemplateElement {

	// Tag id functionality.
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static int Id; @Override public int getId(int level) { return level == 0 ? Id : super.getId(level-1); }
	
	public Srai(Attributes attributes) {}
  
	public Srai(TemplateElement... children) {
		super(children);
	}

	public Srai(List<TemplateElement> children) {
		super(children);
	}

	public Srai(int index) {
		super(new Star(index));
  	}

	/* Methods */

	@Override
	public void appendChild(AIMLElement child) {
		child.appendMeTo(this);
	}

	@Override
  	public String process(Match match) {
	  	String request = super.process(match);

	  	try {
		  	AliceBot bot = match != null ? match.getCallback() : null;
		  	return bot != null ? bot.respond(request) : "";
	  	} catch (RuntimeException exception) {
		  	throw new RuntimeException("While trying to respond \"" + request + "\"", exception);
	  	}
  	}
  
  	@Override
	public Node getNode(Document document, Node parent, int indentLevel) {
				
		Element element = document.createElement("srai");
				
		for(TemplateElement child : children)
			element.appendChild(child.getNode(document, element, indentLevel + 1));
		
		return element;
	}
  	
  	@Override
  	public String toString() {
  		return "<srai>" + super.toString() + "</srai>";
  	}
}
