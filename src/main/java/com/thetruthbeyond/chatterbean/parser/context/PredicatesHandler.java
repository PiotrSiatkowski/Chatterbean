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

package com.thetruthbeyond.chatterbean.parser.context;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.thetruthbeyond.chatterbean.Context;

public class PredicatesHandler extends DefaultHandler {
	
	private final Context context;
	
	public PredicatesHandler(Context context) {
		this.context = context;
	}

	@Override
	public void startElement(String namespace, String name, String qualifiedName, Attributes attributes) throws SAXException {
		if(qualifiedName.equals("set")) {
			String attributeName = attributes.getValue("name");
			if(!context.hasSetting(attributeName))
				context.setPredicate(attributeName, attributes.getValue("value"));
		}
	}   
}
