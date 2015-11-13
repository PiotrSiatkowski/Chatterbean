/*
 * Copyleft (C) 2015 Piotr Siatkowski find me on Facebook;
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

package com.thetruthbeyond.chatterbean.parser.api;

import java.io.*;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import com.thetruthbeyond.chatterbean.AliceBot;
import com.thetruthbeyond.chatterbean.Context;
import com.thetruthbeyond.chatterbean.graph.Graphmaster;
import com.thetruthbeyond.chatterbean.parser.aiml.AIMLParser;
import com.thetruthbeyond.chatterbean.parser.aiml.AIMLParserConfigurationException;
import com.thetruthbeyond.chatterbean.parser.aiml.AIMLParserException;
import com.thetruthbeyond.chatterbean.parser.context.ContextParser;
import com.thetruthbeyond.chatterbean.parser.transformations.TransformationsParser;
import com.thetruthbeyond.chatterbean.text.structures.Transformations;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

public class BotFactory {

	private final ContextParser contextParser;
	private final AIMLParser aimlParser;
	private final TransformationsParser transformationsParser;

	public BotFactory() throws AliceBotParserConfigurationException {
		try {
			contextParser = new ContextParser();
			aimlParser = new AIMLParser();
			transformationsParser = new TransformationsParser();
		} catch(AIMLParserConfigurationException | ParserConfigurationException | SAXException | RuntimeException exception) {
			throw new AliceBotParserConfigurationException(exception);
		}
	}
	
	public AliceBot createAliceBot(String name, Explorer explorer) throws AliceBotParserException, AliceBotExplorerException {
		AliceBot bot = parse(explorer);

		bot.getContext().setProperty("name", name);
		return bot;
	}

	private AliceBot parse(Explorer explorer) throws AliceBotParserException, AliceBotExplorerException {

		AliceBot bot = null;

		try(InputStream properties 		= explorer.getProperties();
			InputStream predicates 		= explorer.getPredicates();
			InputStream splitters 		= explorer.getSplitters();
			InputStream substitutions 	= explorer.getSubstitutions()) {

			InputStream[] categories = null;
			try {
				categories = explorer.getAIMLS();

				Properties configuration = new Properties();
				configuration.loadFromXML(explorer.getConfiguration());

				try {
					Context context = contextParser.parse(properties, predicates);
					Transformations transformations = transformationsParser.parse(splitters, substitutions);
					Graphmaster graphmaster = aimlParser.parse(new Graphmaster(), transformations, categories);

					bot =  new AliceBot(explorer, context, graphmaster, transformations);
					bot.setPassword(configuration.getProperty("password", ""));

				} catch(AIMLParserException | RuntimeException | SAXException | IOException exception) {
					throw new AliceBotParserException(exception.getMessage(), exception);
				}
			} finally {
				if(categories != null)
					for(InputStream stream : categories)
						stream.close();
			}

			return bot;
		} catch(FileNotFoundException exception) {
			throw new AliceBotExplorerException("Explorer's file cannot be found. Check explorer's implementation.", exception);
		} catch(InvalidPropertiesFormatException exception) {
			throw new AliceBotExplorerException("One of the explorer file has wrong format.", exception);
		} catch(IOException exception) {
			throw new AliceBotExplorerException("Problem with explorer's files. Check its implementation.", exception);
		}
	}
}
