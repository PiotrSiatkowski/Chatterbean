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
import com.thetruthbeyond.chatterbean.text.structures.Transformations;

public class Condition extends TemplateElement {
	
	// Tag id functionality.
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static int Id; @Override public int getId(int level) { return level == 0 ? Id : super.getId(level-1); }
	
	private String name = null;
	private String value = null;

	/* Constructors */

	public Condition() {}

	public Condition(Attributes attributes) {
		name = attributes.getValue(ATTRIBUTE_NAME);
		value = attributes.getValue(ATTRIBUTE_VALUE);
	}

	/* Methods */

	@Override
	public void appendChild(AIMLElement child) {
		child.appendMeTo(this);
	}

	@Override
	public void appendMeTo(TemplateElement element) {
		element.appendChild(this);
	}

	@Override
	public String process(Match match) {
		AliceBot bot = match.getCallback();
		Context context = bot.getContext();
		Transformations transformations = bot.getTransformations();

		if(name == null) {
			// Only condition list.
			Li li = find(context, transformations);
			return li == null ? "" : li.process(match);
		} else

		if(value == null) {
			// Condition list without name.
			Li li = find(name, context, transformations);
			return li == null ? "" : li.process(match);
		} else {
			// Condition with name and value.
			String predicate = context.getPredicate(name);
			if(predicate == null)
				predicate = context.getProperty(name);
			if(predicate == null)
				return "";

			return super.process(match);
		}
	}

	private Li find(Context context, Transformations transformations) {
		for(AIMLElement child : children) {
			if(child.getId(0) != Li.Id)
				continue;
			Li li = (Li) child;

			// Kind of else statement.
			if(li.getName() == null)
				return li;

			if(li.getValue() == null)
				continue;

			String returned = context.getPredicate(li.getName());
			if(returned == null)
				returned = context.getProperty(li.getName());
			if(returned == null)
				continue;

			// Normalization and comparision..
			returned = transformations.makeSentence(returned).getNormalized();
			if(returned.equals(transformations.makeSentence(li.getValue()).getNormalized()))
				return li;
		}

		return null;
	}

	private Li find(String name, Context context, Transformations transformations) {
		for(AIMLElement child : children) {
			if(child.getId(0) != Li.Id)
				continue;
			Li li = (Li) child;

			if(li.getValue() == null)
				continue;

			String returned = context.getPredicate(name);
			if(returned == null)
				returned = context.getProperty(name);
			if(returned == null)
				continue;

			// Normalization and comparision..
			returned = transformations.makeSentence(returned).getNormalized();
			if(returned.equals(transformations.makeSentence(li.getValue()).getNormalized()))
				return li;
		}

		return null;
	}

	@Override
	public Node getNode(Document document, Node parent, int indentLevel) {
		
		// Indent
		String indent = new String(new char[indentLevel]).replace("\0", BASE_INDENT);
		parent.appendChild(document.createTextNode(NEW_LINE + indent));
				
		Element element = document.createElement("condition");

		if(name != null && value != null)
			element.setAttribute(name, value);

		boolean hasNoTextChild = false;
		for(TemplateElement child : children) {
			element.appendChild(child.getNode(document, element, indentLevel + 1));
			if(child.getId(0) != Text.Id)
				hasNoTextChild = true;
		}

		if(!(name != null && value != null || hasNoTextChild))
			element.appendChild(document.createTextNode(NEW_LINE + indent));

		return element;
	}
	
	/* Properties */

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getValue() { return value; }
	public void setValue(String value) { this.value = value; }
}
