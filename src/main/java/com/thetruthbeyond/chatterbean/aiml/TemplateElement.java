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
import java.util.LinkedList;
import java.util.List;

import com.thetruthbeyond.chatterbean.Match;

public abstract class TemplateElement extends AIMLElement {

	private static final int CAPACITY = 30;

	// Tag id functionality.
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static int Id; @Override public int getId(int level) { return level == 0 ? Id : super.getId(level-1); }
	
	/* Attribute Section */
  
	protected final List<TemplateElement> children = new LinkedList<>();
  
	/* Constructor Section */

	protected TemplateElement() {}

	public TemplateElement(TemplateElement... children) {
		Collections.addAll(this.children, children);
	}

	public TemplateElement(List<TemplateElement> children) {
		this.children.addAll(children);
	}

	/* Method Section */

	@Override
	public void appendChild(AIMLElement child) {
		child.appendMeTo(this);
	}
	
	public void appendChild(TemplateElement child) {
		children.add(child);
	}

	@Override public void appendMeTo(Bot parent) 				{ parent.appendChild(this); }
	@Override public void appendMeTo(Condition parent) 			{ parent.appendChild(this); }
	@Override public void appendMeTo(Date parent)				{ parent.appendChild(this); }
	@Override public void appendMeTo(Formal parent) 			{ parent.appendChild(this); }
	@Override public void appendMeTo(Gender parent) 			{ parent.appendChild(this); }
	@Override public void appendMeTo(Get parent) 				{ parent.appendChild(this); }
	@Override public void appendMeTo(Input parent) 				{ parent.appendChild(this); }
	@Override public void appendMeTo(Learn parent) 				{ parent.appendChild(this); }
	@Override public void appendMeTo(Li parent) 				{ parent.appendChild(this); }
	@Override public void appendMeTo(Lowercase parent)			{ parent.appendChild(this); }
	@Override public void appendMeTo(Person parent) 			{ parent.appendChild(this); }
	@Override public void appendMeTo(Person2 parent) 			{ parent.appendChild(this); }
	@Override public void appendMeTo(Random parent) 			{ parent.appendChild(this); }
	@Override public void appendMeTo(Sentence parent) 			{ parent.appendChild(this); }
	@Override public void appendMeTo(Set parent) 				{ parent.appendChild(this); }
	@Override public void appendMeTo(Sr parent) 				{ parent.appendChild(this); }
	@Override public void appendMeTo(Srai parent) 				{ parent.appendChild(this); }
	@Override public void appendMeTo(TemplateElement parent) 	{ parent.appendChild(this); }
	@Override public void appendMeTo(Template parent) 			{ parent.appendChild(this); }

	public String process(Match match) {
		StringBuilder value = new StringBuilder(CAPACITY);
    	for(TemplateElement element : children)
    		value.append(element.process(match));
    
    	return value.toString();
  	}

  	/* Property Section */
  
  	public TemplateElement getChild(int index) {
	  return children.get(index);
  	}
  
  	public List<TemplateElement> getChildren() {
		return Collections.unmodifiableList(children);
	}
  	
  	public boolean hasChildren() {
  		return !children.isEmpty();
  	}
  	
  	public void setChildren(TemplateElement... elements) {
  		children.clear();
  		children.addAll(Arrays.asList(elements));
  	}

	@Override
	public String toString() {
		return "TemplateElement: Unsupported toString() operation.";
	}

	@Override
  	public boolean equals(Object object) {
  		if(object instanceof TemplateElement) {
			TemplateElement that = (TemplateElement) object;
			return children.equals(that.children);
		} else
			return false;
  	}

  	@Override
  	public int hashCode() {
  		return children.hashCode();
  	}
}
