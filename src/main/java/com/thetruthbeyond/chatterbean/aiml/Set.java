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
import com.thetruthbeyond.chatterbean.Context;
import com.thetruthbeyond.chatterbean.Match;

public class Set extends TemplateElement {
  
	// Tag id functionality.
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static int Id; @Override public int getId(int level) { return level == 0 ? Id : super.getId(level-1); }
	
	/* Attributes */
  
	private final String name;
  
	/* Constructors */

	public Set(Attributes attributes) {
		name = attributes.getValue(0);
	}
  
	public Set(String name, TemplateElement... children) {
		super(children);
		this.name = name;
	}
  
	/* Methods */

	@Override
	public void appendChild(AIMLElement child) {
		child.appendMeTo(this);
	}

	@Override
	public Node getNode(Document document, Node parent, int indentLevel) {

		String indent = new String(new char[indentLevel]).replace("\0", BASE_INDENT);

		Element set = document.createElement("set");
		set.setAttribute("name", name);

		boolean hasNoTextChild = false;
		for(TemplateElement child : children) {
			set.appendChild(child.getNode(document, set, indentLevel + 1));
			if(child.getId(0) == Think.Id)
				set.appendChild(document.createTextNode("\n" + indent));
			else

			if(child.getId(0) != Text.Id)
				hasNoTextChild = true;
		}

		if(hasNoTextChild)
			set.appendChild(document.createTextNode(NEW_LINE + indent));
		return set;
	}
	
	@Override
	public String process(Match match) {
		String output = super.process(match);
		if(match == null)
			output = "<set name=\"" + name + "\">" + output + "</set>";
		else {
			AliceBot bot = match.getCallback();
			
			Context context = bot != null ? bot.getContext() : null;
			if(context != null) {
				if(context.hasSetting(name))
					bot.launchFunction(name, output);
				else
					context.setPredicate(name, output);
			}
		}
    
		return output;
	}
	
	@Override
	public boolean equals(Object object) {
		if(object == null)
			return false;

		if(object instanceof Set) {
			Set compared = (Set) object;
			if(!name.equals(compared.name))
				return false;
			return super.equals(compared);
		} else
			return false;
	}
}
