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

package com.thetruthbeyond.chatterbean.functors;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.thetruthbeyond.chatterbean.parser.api.AliceBotExplorerException;
import com.thetruthbeyond.chatterbean.parser.api.Explorer;
import org.xml.sax.SAXException;

import com.thetruthbeyond.chatterbean.utility.logging.Logger;
import com.thetruthbeyond.chatterbean.AliceBot;
import com.thetruthbeyond.chatterbean.Context;
import com.thetruthbeyond.chatterbean.parser.context.PredicatesHandler;
import com.thetruthbeyond.chatterbean.text.structures.Sentence;
import com.thetruthbeyond.chatterbean.text.structures.Transformations;

public class UserFunctor extends SpecialSettingFunctor {

	private final Context context;
	private final Transformations transformations;
	
	public UserFunctor(AliceBot callback) {
		super(callback);
		
		context = callback.getContext();
		transformations = callback.getTransformations();
	}

	@Override
	public Object execute(Object argument) {
		String input = ((String) argument).trim();
		if(input.isEmpty() || input.equals("*") || input.equals("Default"))
			context.setSetting(AliceBot.USER, Sentence.ASTERISK);
		else {
			Sentence newUser = transformations.makeSentence(input);	
			Sentence currentUser = context.getSetting(AliceBot.USER);
			
			if(!newUser.equals(currentUser)) {
				Explorer explorer = callback.getExplorer();
				explorer.updatePredicatesFile(context);
				
				context.setSetting(AliceBot.USER, newUser);
				
				// Parsing new user predicates file.
				try {
					SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
					
					context.clearPredicates();
					
					PredicatesHandler predicatesHandler = new PredicatesHandler(context);
					parser.parse(explorer.getPredicates(newUser.getOriginal()), predicatesHandler);
				} catch(SAXException ignored) {
					new Logger().writeMessage("SAXException", "Error in parsing new user predicates file.");
				} catch(ParserConfigurationException ignored) {
					new Logger().writeMessage("ParserConfigurationException", "Error in parsing new user predicates file.");
				} catch(IOException ignored) {
					new Logger().writeMessage("IOException", "Error in parsing new user predicates file.");
				} catch(AliceBotExplorerException ignored) {
					new Logger().writeMessage("AliceBotExplorerException", "Error in parsing new user predicates file.");
				} catch(RuntimeException ignored) {
					new Logger().writeMessage("Exception", "Error in parsing new user predicates file.");
				}
			}
		}
		
		return null;
	}
}
