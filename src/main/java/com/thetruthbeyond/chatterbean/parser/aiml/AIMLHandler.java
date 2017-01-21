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

package com.thetruthbeyond.chatterbean.parser.aiml;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.lang.reflect.Constructor;

import com.thetruthbeyond.chatterbean.aiml.*;
import com.thetruthbeyond.chatterbean.text.structures.Transformations;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class AIMLHandler extends DefaultHandler {

	// Ignored tags.
	private final Set<String> ignored = new HashSet<>(10);
	private final StringBuilder text = new StringBuilder(100);

	private boolean ignoreWhitespace = true;

	private Transformations transformations;

	/**
	 * The stack of AIML objects is used to build the Categories as AIML
	 * documents are parsed. The scope is defined as package for testing
	 * purposes.
	 */
	private final AIMLStack stack = new AIMLStack();

    AIMLHandler(String... ignore) {
		ignored.addAll(Arrays.asList(ignore));
	}

	public void setTransformations(Transformations transformations) {
		this.transformations = transformations;
	}

	/* Event Handling Section */

	@Override
	public void characters(char[] chars, int start, int length) {
		text.append(chars, start, length);
	}

	@SuppressWarnings("OverlyBroadCatchBlock")
	@Override
	public void startElement(String namespace, String name, String qualifiedname, Attributes attributes) throws SAXException {
		if(ignored.contains(qualifiedname))
			return;
		
		updateIgnoreWhitespace(attributes);
		pushTextNode();

		String className = buildClassName(qualifiedname);
		try {
			Class<?> tagClass = Class.forName(className);
			Constructor<?> constructor = tagClass.getConstructor(Attributes.class);
			AIMLElement tag = (AIMLElement) constructor.newInstance(attributes);
			stack.push(tag);
		} catch(Exception e) {
			throw new RuntimeException("Cannot instantiate class " + className, e);
		}
	}

	@Override
	public void endElement(String namespace, String name, String qualifiedName) throws SAXException {
		if(ignored.contains(qualifiedName))
			return;
		
		pushTextNode();
		ignoreWhitespace = true;

		String className = buildClassName(qualifiedName);
		for(List<AIMLElement> children = new LinkedList<>();;) {
			AIMLElement tag = stack.pop();
			if(tag == null)
				throw new SAXException("No matching start tag found for " + qualifiedName);
			else if(!className.equals(tag.getClass().getName()))
				children.add(0, tag);
			else
				try {
					if(!children.isEmpty())
						tag.appendChildren(children);

					if(tag instanceof NormalizedTag)
						((NormalizedTag) tag).normalizeContent(transformations);
					stack.push(tag);
					return;
				} catch(AIMLXmlException e) {
					throw e;
				} catch(ClassCastException e) {
					throw new RuntimeException("Tag <" + qualifiedName + "> used as node, but implementing "
													   + "class does not implement the AIMLElement interface", e);
				} catch(RuntimeException e) {
					throw new SAXException(e);
				}
		}
	}

	@SuppressWarnings("MethodMayBeStatic")
	private String buildClassName(String tag) {
		return "com.thetruthbeyond.chatterbean.aiml." + tag.substring(0, 1).toUpperCase() + tag.substring(1).toLowerCase();
	}

	private void pushTextNode() {
		String pushed = text.toString();
		if(ignoreWhitespace)
			pushed = pushed.replaceAll("^[\\s\n]+|[\\s\n]{2,}|\n", " ");

		if(!pushed.trim().isEmpty())
			stack.push(new Text(pushed));

		text.delete(0, text.length());
	}

	private void updateIgnoreWhitespace(Attributes attributes) {
		if(attributes != null)
			ignoreWhitespace = !"preserve".equals(attributes.getValue("xml:space"));
	}

    List<Category> unload() {
		List<Category> result = new LinkedList<>();

		Object poped;
		while((poped = stack.pop()) != null)
			if(poped instanceof Aiml)
				result.addAll(((Aiml) poped).getCategories());

		return result;
	}
}
