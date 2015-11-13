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

import java.util.ArrayList;
import java.util.List;

public class Category extends AIMLElement {
  
	// Tag id functionality.
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static int Id; @Override public int getId(int level) { return level == 0 ? Id : super.getId(level-1); }
	
	private Pattern pattern;
	private Template template;
	private That that;
	private Topic topic;

	/* Constructor Section */
	
	public Category(String pattern, TemplateElement... children) {
		this(new Pattern(pattern), new That("*"), new Topic("*"), new Template(children));
	}
  
	public Category(Pattern pattern, Template template) {
		this(pattern, new That("*"), new Topic("*"), template);
	}

	public Category(Pattern pattern, That that, Template template) {
		this(pattern, that, new Topic("*"), template);
	}
  
	public Category(Pattern pattern, That that, Topic topic, Template template) {
		this.pattern = pattern;
		this.template = template;
		this.that = that;
		this.topic = topic;
	}
  
	public Category(Attributes attributes) {
		this(new Pattern(""), new That("*"), new Topic("*"), new Template());
	}
  
	/* Method Section */
	
	@Override
	public void appendChild(AIMLElement child) {
		child.appendMeTo(this);
	}
	
	public void appendChild(Pattern child) {
		pattern = child;
	}
	
	public void appendChild(That child) {
		that = child;
	}
	
	public void appendChild(Template child) {
		template = child;
	}

	@Override
	public void appendMeTo(Aiml aiml) {
		aiml.appendChild(this);
	}

	@Override
	public void appendMeTo(Topic topic) {
		topic.appendChild(this);
	}

	@Override
	public String process(Match match) {
		return template.process(match);
	}

	@Override
	public Node getNode(Document document, Node parent, int indentLevel) {

		// Indent
		String indent = new String(new char[indentLevel]).replace("\0", BASE_INDENT);
		parent.appendChild(document.createTextNode(NEW_LINE + indent));
				
		Element element = document.createElement("category");
		
		element.appendChild(pattern.getNode(document, element, indentLevel + 1));
		
		if(!that.isAsterisk())
			element.appendChild(that.getNode(document, element, indentLevel + 1));
		
		element.appendChild(template.getNode(document, element, indentLevel + 1));
		
		element.appendChild(document.createTextNode(NEW_LINE + indent));
		return element;
	}
	
	/* Properties */
  
  	public List<String> getMatchPath() {
		List<String> topiPath = topic.getMatchPath();
		List<String> thatPath = that.getMatchPath();
		List<String> pattPath = pattern.getMatchPath();

		List<String> match = new ArrayList<>(topiPath.size() + thatPath.size() + pattPath.size() + 3);

		match.add(Match.TOPIC_MATCH);   match.addAll(topiPath);
	  	match.add(Match.THAT_MATCH);    match.addAll(thatPath);
	  	match.add(Match.PATTERN_MATCH); match.addAll(pattPath);

	  	return match;
  	}

  	public Pattern getPattern() { return pattern; }
  	public void setPattern(Pattern pattern) { this.pattern = pattern; }

  	public Template getTemplate() { return template; }
  	public void setTemplate(Template template) { this.template = template; }

  	public That getThat() { return that; }
  	public void setThat(That that) { this.that = that; }

  	public Topic getTopic() { return topic; }
 	public void setTopic(Topic topic) { this.topic = topic; }
  
  	@Override
  	public boolean equals(Object obj) {
		if(obj instanceof Category) {
			Category compared = (Category) obj;
			return pattern.equals(compared.pattern) && template.equals(compared.template) && that.equals(compared.that);
		} else
			return false;
  	}

  	@Override
  	public String toString() {
  		return "[" + pattern + "][" + that + "][" + template + "]";
  	}
}
