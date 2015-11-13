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

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.thetruthbeyond.chatterbean.aiml.AimlRegistrationOffice;
import com.thetruthbeyond.chatterbean.graph.Graphmaster;
import com.thetruthbeyond.chatterbean.text.structures.Transformations;
import org.xml.sax.SAXException;

public class AIMLParser {

	private final AIMLHandler handler = new AIMLHandler();
	private final SAXParser parser;

	public AIMLParser() throws AIMLParserConfigurationException {
		AimlRegistrationOffice office = new AimlRegistrationOffice();
		office.registerAllAimlTags();
		
		try {
			parser = SAXParserFactory.newInstance().newSAXParser();
		} catch(ParserConfigurationException | RuntimeException | SAXException exception) {
			throw new AIMLParserConfigurationException(exception);
		}
	}
	
	public Graphmaster parse(Graphmaster graphmaster, Transformations transformations, InputStream... sources) throws AIMLParserException {
		try {
			handler.setTransformations(transformations);
			for(InputStream aiml : sources)
				parser.parse(aiml, handler);

			graphmaster.append(handler.unload());
		} catch(SAXException | IOException exception) {
			throw new AIMLParserException(exception.getMessage(), exception);
		}

		return graphmaster;
	}
}