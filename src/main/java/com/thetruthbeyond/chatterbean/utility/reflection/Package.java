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

package com.thetruthbeyond.chatterbean.utility.reflection;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.thetruthbeyond.chatterbean.AliceBot;
import com.thetruthbeyond.chatterbean.utility.annotations.Accepted;
import com.thetruthbeyond.chatterbean.utility.logging.Logger;

@Accepted
public class Package {
	
	private String name = "";
	private String path = "";

	private JarFile me;
	private final Logger logger = new Logger();

	public Package(String name) {
		this.name = name;
		path = name.replaceAll("\\.", "/");
	}

	@SuppressWarnings("unchecked")
	public <T> List<Class<T>> getClassesFromJar(Class<T> superclass, JarFile file) {

		List<Class<T>> result = new LinkedList<>();
		if(file == null)
			return result;

		try {
			String path = AliceBot.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			String decodedPath = URLDecoder.decode(path, "UTF-8");
			me = new JarFile(decodedPath);
		} catch(UnsupportedEncodingException exception) {
			logger.writeMessage("Error: Unsupported encoding in package searching", exception.getMessage());
		} catch(IOException exception) {
			logger.writeMessage("Error: IOException in package searching", exception.getMessage());
		}


		Enumeration<JarEntry> entries = me.entries();
		while(entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String entryName = entry.getName();

			if(entryName.startsWith(path) && entryName.charAt(entryName.length() - 1) != '/' && !entryName.isEmpty()) {

				// Eliminate all path separated by "/" as long as dot "." isn't occurring.
				entryName = entryName.substring(entryName.lastIndexOf('/') + 1, entryName.lastIndexOf('.'));

				try {
					Class<?> type = Class.forName(name + "." + entryName);
					if(superclass.isAssignableFrom(type))
						result.add( (Class<T>) type);
				} catch (ClassNotFoundException exception) {
					logger.writeMessage("Error: IOException in package searching", exception.getMessage());
				}
			}
		}

		try {
			if(me != null)
				me.close();
		} catch(IOException exception) {
			logger.writeMessage("Warning: Cannot close archive file.", exception.getMessage());
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public <T> List<Class<T>> getClassesFromSource(Class<T> superclass, String moduleDirectory) {

		List<Class<T>> result = new LinkedList<>();

		// Search file tree in order to find moduleDirectory.
		File place = getModuleDirectory(moduleDirectory);
		if(place != null && place.isDirectory()) {
			place = new File(place.getPath() + "/src/" + path);

			String[] classes = place.list();
			for(String className : classes) {
				Class<?> type;
				try {
					type = Class.forName(name + "." + className.substring(0, className.lastIndexOf(".")));
					if(superclass.isAssignableFrom(type))
						result.add((Class<T>) type);
				} catch(ClassNotFoundException exception) {
					logger.writeMessage("Error in package search", exception.getMessage());
				}
			}
		}

		return result;
	}

	private File getModuleDirectory(String moduleDirectory) {
		File place = new File(".").getAbsoluteFile();
		while(place != null && place.isDirectory()) {
			List<String> list = Arrays.asList(place.list());
			if(list.contains(moduleDirectory))
				return new File(place.getPath() + "/" + list.get(list.indexOf(moduleDirectory)));
			else
				place = place.getParentFile();
		}

		return null;
	}
}
