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

package com.thetruthbeyond.chatterbean;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thetruthbeyond.chatterbean.text.structures.Request;
import com.thetruthbeyond.chatterbean.text.structures.Response;
import com.thetruthbeyond.chatterbean.text.structures.Sentence;
import static com.thetruthbeyond.chatterbean.text.structures.Sentence.ASTERISK;

/**
 * A conversational context. This class stores information such as the history
 * of a conversation and predicate values, which the Alice Bot can refer to
 * while responding user requests.
 */
public class Context {

	/** Map of context settings. */
	private final Map<String, Sentence> settings = new HashMap<>(16);

	/** Map of context properties. */
	private final Map<String, String> properties = new LinkedHashMap<>(32);

	/** Map of context predicates. */
	private final Map<String, String> predicates = new LinkedHashMap<>(64);
	
	private final List<Request> requests = new LinkedList<>();
	private final List<Response> responses = new LinkedList<>();

	public Context() {
		settings.put(AliceBot.THAT, ASTERISK);
		settings.put(AliceBot.USER, ASTERISK);
		settings.put(AliceBot.TOPIC, ASTERISK);
	}

	/* Method Section */

	public void appendRequest(Request request) {
		requests.add(0, request);
	}

	public void appendResponse(Response response) {
		responses.add(0, response);
		settings.put(AliceBot.THAT, response.getSentence(response.size() - 1));
	}

	/* Property Section */
	
	public String getProperty(String name) {
		return properties.get(name);
	}

	public void setProperty(String name, String value) {
		properties.put(name, value);
	}

	public Set<String> getPropertiesNames() {
		return properties.keySet();
	}
	
	public void clearProperties() {
		properties.clear();
	}
	
	public String getPredicate(String name) {
		return predicates.get(name);
	}

	public void setPredicate(String name, String value) {
		predicates.put(name, value);
	}
	
	public Set<String> getPredicatesNames() {
		return predicates.keySet();
	}

	public void clearPredicates() {
		predicates.clear();
	}
	
	public boolean hasSetting(String name) {
		return settings.containsKey(name);
	}
	
	public void setSetting(String name, Sentence value) {
		settings.put(name, value);
	}
	
	public Sentence getSetting(String name) {
		return settings.get(name);
	}

	public Request getRequests(int index) {
		if(requests.isEmpty() || index >= requests.size())
			return null;
		return requests.get(index);
	}

	public Response getResponses(int index) {
		if(responses.isEmpty() || index >= responses.size())
			return null;
		return responses.get(index);
	}
}
