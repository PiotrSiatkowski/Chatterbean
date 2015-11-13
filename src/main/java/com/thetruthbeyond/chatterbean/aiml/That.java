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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.thetruthbeyond.chatterbean.text.structures.Transformations;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;

import com.thetruthbeyond.chatterbean.AliceBot;
import com.thetruthbeyond.chatterbean.Context;
import com.thetruthbeyond.chatterbean.Match;
import com.thetruthbeyond.chatterbean.text.structures.Response;

public class That extends TemplateElement implements NormalizedTag {
  
	// Tag id functionality.
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static int Id; @Override public int getId(int level) { return level == 0 ? Id : super.getId(level-1); }

	private int responseIndex = 1, sentenceIndex = 1;
	private final List<String> match = new ArrayList<>(Collections.singletonList("*"));

	/* Constructor Section */
  
	public That(Attributes attributes) {
		String value = attributes.getValue(0);
		if(value == null)
			return;
    
		String[] indexes = value.split(",");
		responseIndex = Integer.parseInt(indexes[0].trim());
		if(indexes.length > 1) 
			sentenceIndex = Integer.parseInt(indexes[1].trim());
	}

	public That(String text) {
		super(new Text(text));
	}

	public That(TemplateElement... children) {
		super(children);
	}
  
	public That(int responseIndex, int sentenceIndex) {
		this.responseIndex = responseIndex;
		this.sentenceIndex = sentenceIndex;
	}
  
	/* Method Section */

	@Override
	public void normalizeContent(Transformations transformations) {
		if(!children.isEmpty())
			for(TemplateElement child : children) {
				String text = child.toString();
				text = transformations.makeSentence(text).getNormalized().trim();
				match.addAll(Arrays.asList(text.split(" ")));
			}
	}

	@Override
	public void appendChild(AIMLElement child) {
		child.appendMeTo(this);
	}

	@Override
	public void appendMeTo(Category category) {
		category.appendChild(this);
	}

	public List<String> getMatchPath() {
		return Collections.unmodifiableList(match);
	}

	public boolean isAsterisk() {
		if(children.size() != 1)
			return false;
	  
		TemplateElement child = children.get(0);
		if(child.getId(0) != Text.Id)
			return false;
		String text = child.toString().trim();
		return text.equals("*");
	}

	@Override
	public String process(Match match) {
	  	if(match == null)
		  	return "";
    
	  	AliceBot bot = match.getCallback();
	  	Context context = bot.getContext();
	  	Response response = context.getResponses(responseIndex - 1);
	  
	  	if(response.size() == 0)
		  	return "";
	  	return response.getSentence(response.size() - sentenceIndex).getOriginal().trim();
  	}
  
	@Override
	public Node getNode(Document document, Node parent, int indentLevel) {
		Element element = document.createElement("that");
		
		// Indent
		String indent = new String(new char[indentLevel]).replace("\0", BASE_INDENT);
		parent.appendChild(document.createTextNode("\n" + indent));
				
		if(!children.isEmpty()) {
			TemplateElement child = children.get(0);
			element.setTextContent(child.toString());
		}
		
		return element;
	}
	
	@Override
  	public String toString() {
  		if(children.isEmpty())
  			return "<that index=\"" + responseIndex + ", " + sentenceIndex + "\"/>";
  		else {
  			StringBuilder builder = new StringBuilder(10);
  			for(TemplateElement element : children)
  				builder.append(element);

  			return builder.toString();
	  	}
  	}
  
	@Override
  	public boolean equals(Object object) {
	  	if(object instanceof That) {
			That compared = (That) object;

			return responseIndex == compared.responseIndex &&
				   sentenceIndex == compared.sentenceIndex;
		} else
			return false;
  	}

	@Override
  	public int hashCode() {
	  	return responseIndex + sentenceIndex;
  	}	
}
