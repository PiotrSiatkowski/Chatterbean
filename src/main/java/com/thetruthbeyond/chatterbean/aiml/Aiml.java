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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Aiml extends AIMLElement {

	private static final int CAPACITY = 30;

	// Tag id functionality.
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static int Id; @Override public int getId(int level) { return level == 0 ? Id : super.getId(level - 1); }

	private final Topic defaultTopic = new Topic("*");  
	private final List<Topic> topics = new LinkedList<>();

	private final List<Category> categories = new LinkedList<>();
  
	private String version = "Undefined";

	/* Constructors */
  
	public Aiml(Topic topic) {
		topics.add(topic);
	}
	
	public Aiml(Attributes attributes) {
		topics.add(defaultTopic);
		version = attributes.getValue(0);
	}
  
	public Aiml(Category... categories) {
		topics.add(defaultTopic);
		Collections.addAll(this.categories, categories);
	}

	public Aiml(List<Category> categories) {
		topics.add(defaultTopic);
		this.categories.addAll(categories);
	}

	/* Method Section */
  	
	@Override
	public void appendChild(AIMLElement child) {
		child.appendMeTo(this);
	}
	
	public void appendChild(Category child) {
		child.setTopic(defaultTopic);
	 	defaultTopic.appendChild(child);
	 	categories.add(child);
	}
  
	public void appendChild(Topic child) {
		topics.add(child);
		categories.addAll(child.getCategories());
	}
	
	@Override
	public Node getNode(Document document, Node parent, int indentLevel) {
						
		Element element = document.createElement("aiml");
		
		for(Topic topic : topics)
			element.appendChild(topic.getNode(document, element, indentLevel + 1));
		
		for(Category category : categories)
			element.appendChild(category.getNode(document, element, indentLevel + 1));
		
		element.appendChild(document.createTextNode(NEW_LINE));
		return element;
	}
	
	public List<Category> getCategories() {
		return Collections.unmodifiableList(categories);
	}
  
	public String getVersion() {
		return version;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Aiml && categories.equals(((Aiml) obj).categories);
	}
  
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(CAPACITY);
		for(Category category : categories) {
			result.append(category);
			result.append(NEW_LINE);
		}
    
		return result.toString();
	}
}
