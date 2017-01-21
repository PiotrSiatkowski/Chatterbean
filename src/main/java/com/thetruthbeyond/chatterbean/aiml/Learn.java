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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.thetruthbeyond.chatterbean.parser.aiml.AIMLParserConfigurationException;
import com.thetruthbeyond.chatterbean.parser.aiml.AIMLParserException;
import org.xml.sax.Attributes;

import com.thetruthbeyond.chatterbean.AliceBot;
import com.thetruthbeyond.chatterbean.Match;
import com.thetruthbeyond.chatterbean.parser.aiml.AIMLParser;

public class Learn extends TemplateElement {
  
	// Tag id functionality.
	@SuppressWarnings("FieldNameHidesFieldInSuperclass")
	public static int Id; @Override public int getId(int level) { return level == 0 ? Id : super.getId(level-1); }
	
	/* Constructors */

	public Learn(Attributes attributes) {}

	public Learn(TemplateElement... children) {
		super(children);
	}

	/* Methods */

	@Override
	public void appendChild(AIMLElement child) {
		child.appendMeTo(this);
	}

	@Override
	public String process(Match match) {
		try {
			AliceBot bot = match.getCallback();

			String address = super.process(match);
			URL url = new URL(address);

			AIMLParser parser = new AIMLParser();
			parser.parse(bot.getGraph(), bot.getTransformations(), url.openStream());
		} catch(MalformedURLException exception) {
			throw new RuntimeException("Malformed URL", exception);
		} catch(AIMLParserException | AIMLParserConfigurationException | IOException | RuntimeException exception) {
			throw new RuntimeException(exception);
		}

		return "";
	}
}