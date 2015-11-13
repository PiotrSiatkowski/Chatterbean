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

import java.util.List;

import com.thetruthbeyond.chatterbean.Match;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@SuppressWarnings("UnusedParameters")
public abstract class AIMLElement {

	protected static final String EMPTY_STRING = "";
	protected static final String NEW_LINE = "\n";
	protected static final String BASE_INDENT = "  ";

	protected static final String ATTRIBUTE_NAME = "name";
	protected static final String ATTRIBUTE_VALUE = "value";

	// Tag id functionality.
	public static int Id; public int getId(int level) { return Id; }

	public final void appendChildren(List<? extends AIMLElement> children) {
		for(AIMLElement child : children)
			appendChild(child);
	}

	public void appendChild(AIMLElement child) {}

	public void appendMeTo(AIMLElement parent) {}
	public void appendMeTo(Aiml parent) {}
	public void appendMeTo(Bot parent) {}
	public void appendMeTo(Category parent) {}
	public void appendMeTo(Condition parent) {}
	public void appendMeTo(Date parent) {}
	public void appendMeTo(Formal parent) {}
	public void appendMeTo(Gender parent) {}
	public void appendMeTo(Get parent) {}
	public void appendMeTo(Input parent) {}
	public void appendMeTo(Learn parent) {}
	public void appendMeTo(Li parent) {}
	public void appendMeTo(Lowercase parent) {}
	public void appendMeTo(Pattern parent) {}
	public void appendMeTo(Person parent) {}
	public void appendMeTo(Person2 parent) {}
	public void appendMeTo(Random parent) {}
	public void appendMeTo(Sentence parent) {}
	public void appendMeTo(Set parent) {}
	public void appendMeTo(Sr parent) {}
	public void appendMeTo(Srai parent) {}
	public void appendMeTo(TemplateElement parent) {}
	public void appendMeTo(Template parent) {}
	public void appendMeTo(Text parent) {}
	public void appendMeTo(That parent) {}
	public void appendMeTo(Thatstar parent) {}
	public void appendMeTo(Think parent) {}
	public void appendMeTo(Topic parent) {}
	public void appendMeTo(Topicstar parent) {}
	public void appendMeTo(Uppercase parent) {}
	public void appendMeTo(Version parent) {}
	
	public Node getNode(Document document, Node parent, int indentLevel) {
		throw new AIMLXmlException("Xml node transformation is not supported for com.thetruthbeyond.chatterbean.aiml tag named " + getClass());
	}

	public String process(Match match) { return EMPTY_STRING; }
}
