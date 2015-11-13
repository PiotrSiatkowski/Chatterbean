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

package com.thetruthbeyond.chatterbean.parser.transformations;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class SubstitutionHandler extends DefaultHandler {

	/* Attributes */

	private final Map<String, Map<String, String>> substitutions;
	private Map<String, String> sections;

	/* Constructor */

	public SubstitutionHandler() {
		substitutions = new HashMap<>(12);

		substitutions.put("correction", 	new LinkedHashMap<String, String>(80));
		substitutions.put("protection", 	new LinkedHashMap<String, String>(80));
		substitutions.put("accentuation", 	new LinkedHashMap<String, String>(12));
		substitutions.put("punctuation", 	new LinkedHashMap<String, String>(12));
		substitutions.put("person", 		new LinkedHashMap<String, String>(20));
		substitutions.put("person2", 		new LinkedHashMap<String, String>(20));
		substitutions.put("gender", 		new LinkedHashMap<String, String>(20));
	}

	public SubstitutionHandler(Map<String, Map<String, String>> substitutions) {
		this.substitutions = substitutions;
	}

	/* Event Handlers */
	
	@Override
	public void startElement(String namespace, String name, String qualifiedName, Attributes attributes) {
		if(qualifiedName.equals("substitute")) {
			String find = attributes.getValue(0);
			String replace = attributes.getValue(1);
			sections.put(find, replace);
		} else {
			if(substitutions.containsKey(qualifiedName))
				sections = substitutions.get(qualifiedName);
		}
	}

	/* Method Section */

	public void clear() {
		for(Map<String, String> map : substitutions.values())
			map.clear();
	}

	public Map<String, Map<String, String>> getSubstitutions() {
		return new HashMap<>(substitutions);
	}
}
