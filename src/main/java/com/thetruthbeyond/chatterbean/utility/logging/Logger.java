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

package com.thetruthbeyond.chatterbean.utility.logging;

import com.thetruthbeyond.chatterbean.AliceBot;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

	private static final int CHATLOG_SPACE_INTENT = 15;
	private final String humanIndent = new String(new char[CHATLOG_SPACE_INTENT - "Human".length()]).replace("\0", " ");
	private String botIndent;

	private final DateFormat time = new SimpleDateFormat("HH:mm:ss");
	private final DateFormat date = new SimpleDateFormat("dd-MM-yy");

	private PrintWriter writer = null;

	private AliceBot currentBot;

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public Logger() {
		File logDirectory = new File("Logs");

		if(!logDirectory.exists())
			logDirectory.mkdir();
	}

	@SuppressWarnings({"ImplicitDefaultCharsetUsage", "UseOfSystemOutOrSystemErr"})
	public void openLogFile(AliceBot bot) {
		currentBot = bot;

		try {
			if(currentBot != null) {
				botIndent = new String(new char[CHATLOG_SPACE_INTENT - currentBot.toString().length()]).replace("\0", " ");

				for(int id = 0;; id++) {
					File directory = new File("Logs/" + currentBot);

					boolean wasCreated = true;
					if(!directory.exists())
						wasCreated = directory.mkdir();

					if(wasCreated) {
						File file = new File("Logs/" + currentBot + "/log(" + date.format(new Date()) + ")-" + id + ".txt");
						if(!file.exists()) {
							writer = new PrintWriter(new BufferedWriter(new FileWriter(file.getPath(), true)));
							break;
						}
					}
				}
			}

		} catch(IOException exception) {
			System.err.println(exception.getMessage());
		}
	}

	@SuppressWarnings({"ImplicitDefaultCharsetUsage", "UseOfSystemOutOrSystemErr"})
	public void appendMessage(String request, String response) {
		String now = time.format(new Date());
		if(writer != null) {
			writer.println("[" + now + "]");
			writer.println(humanIndent + "Human: " + request);
			writer.println(botIndent + currentBot + ": " + response);
		}
	}

	public void closeLogFile() {
		if(writer != null)
			writer.close();
	}

	@SuppressWarnings({"ImplicitDefaultCharsetUsage", "UseOfSystemOutOrSystemErr"})
	public void writeMessage(String tag, String message) {
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter("." + File.separator + "Logs/failure.txt", true)));

			String now = time.format(new Date());
			writer.println("[" + now + "][" + tag + "][" + message + "]");
		} catch(IOException exception) {
			System.err.println(exception.getMessage());
		} finally {
			if(writer != null)
				writer.close();
		}
	}

	@SuppressWarnings({"ImplicitDefaultCharsetUsage", "UseOfSystemOutOrSystemErr"})
	public void writeError(Exception error) {
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter("." + File.separator + "Logs/failure.txt", true)));

			error.printStackTrace(writer);
		} catch(IOException exception) {
			System.err.println(exception.getMessage());
		} finally {
			if(writer != null)
				writer.close();
		}
	}
}
