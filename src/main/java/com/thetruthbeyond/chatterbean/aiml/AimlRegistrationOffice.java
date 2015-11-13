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

import com.thetruthbeyond.chatterbean.AliceBot;
import com.thetruthbeyond.chatterbean.utility.logging.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.List;
import java.util.jar.JarFile;

public class AimlRegistrationOffice {

	private int currentId = 0;
	
	public void registerAllAimlTags() {

		try {
			com.thetruthbeyond.chatterbean.utility.reflection.Package aimlPackage = new com.thetruthbeyond.chatterbean.utility.reflection.Package("com.thetruthbeyond.chatterbean.aiml");

			String jarPath = AliceBot.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			jarPath = URLDecoder.decode(jarPath, "UTF-8");

			List<Class<AIMLElement>> classes = aimlPackage.getClassesFromJar(AIMLElement.class, new JarFile(jarPath));

			for(Class<AIMLElement> type : classes) {
				try {
					Field field = type.getDeclaredField("Id");
					field.setAccessible(true);

					field.setInt(null, currentId++);
					field.setAccessible(false);
				} catch(IllegalArgumentException | SecurityException | IllegalAccessException | NoSuchFieldException exception) {
					new Logger().writeMessage("Error in com.thetruthbeyond.chatterbean.aiml tags registration", exception.getMessage());
				} catch(RuntimeException exception) {
					new Logger().writeMessage("Error in com.thetruthbeyond.chatterbean.aiml tags registration. Undefined one.", exception.getMessage());
				}
			}
		} catch(IOException exception) {
			new Logger().writeMessage("Error", "Application jar file couldn't be found during call of \"registerAllAimlTags\" method");
			new Logger().writeError(exception);
		}
	}
}
