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

import com.thetruthbeyond.chatterbean.Match;

public class Think extends TemplateElement {

	// Tag id functionality.
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static int Id; @Override public int getId(int level) { return level == 0 ? Id : super.getId(level-1); }

	public Think() {}

	public Think(Attributes attributes) {}
  
	public Think(TemplateElement... children) {
		super(children);
	}

	/*Methods */

	@Override
	public void appendChild(AIMLElement child) {
		child.appendMeTo(this);
	}

	@Override
	public Node getNode(Document document, Node parent, int indentLevel) {
		
		// Indent
		String indent = new String(new char[indentLevel]).replace("\0", BASE_INDENT);
		parent.appendChild(document.createTextNode("\n" + indent));
				
		Element think = document.createElement("think");
		for(TemplateElement child : children)
			think.appendChild(child.getNode(document, think, indentLevel + 1));
		
		return think;
	}
	
	@Override
	public String process(Match match) {
		super.process(match);
		return "";
	}
}
