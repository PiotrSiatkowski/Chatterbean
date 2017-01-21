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
import java.util.Map;

import com.thetruthbeyond.chatterbean.aiml.Category;
import com.thetruthbeyond.chatterbean.functors.DateFunctor;
import com.thetruthbeyond.chatterbean.functors.RandomFunctor;
import com.thetruthbeyond.chatterbean.functors.SpecialSettingFunctor;
import com.thetruthbeyond.chatterbean.functors.TopicFunctor;
import com.thetruthbeyond.chatterbean.functors.UserFunctor;
import com.thetruthbeyond.chatterbean.graph.Graph;
import com.thetruthbeyond.chatterbean.parser.api.Explorer;
import com.thetruthbeyond.chatterbean.text.structures.Request;
import com.thetruthbeyond.chatterbean.text.structures.Response;
import com.thetruthbeyond.chatterbean.text.structures.Sentence;
import com.thetruthbeyond.chatterbean.text.structures.Transformations;

public class AliceBot {

	// Special settings constants.
	public static final String TOPIC 	= "_topic";
	public static final String THAT 	= "_that";
	public static final String USER 	= "_user";
	public static final String DATE 	= "_date";
	public static final String RANDOM 	= "_random";

	private static final String EMPTY_ANSWER = "I have understood your words, but do not really know what to say more...";
	private static final String ERROR_ANSWER = "There is some kind of error in my knowledge base. I am still quite stupid you know.";

	/** Explorer object necessary for maintain control over bot's file system.*/
	private final Explorer explorer;

	/** Context information for this bot current conversation. */
	private final Context context;
  
	/** The Graph maps user requests to AIML categories. */
	private final Graph graph;
    
	/** The Transformations used to make proper read and response. */
	private final Transformations transformations;

	/** Special settings com.thetruthbeyond.chatterbean.functors. */
	private final Map<String, SpecialSettingFunctor> functors = new HashMap<>(12);
			
	/** Special use password. */
	private String password = "";
	
	public AliceBot(Explorer explorer, Context context, Graph graph, Transformations transformations) {
		this.explorer = explorer;
		this.context = context;
		this.graph = graph;
		this.transformations = transformations;
			
		functors.put(TOPIC, new TopicFunctor(this));
		functors.put(USER, new UserFunctor(this));
		functors.put(DATE, new DateFunctor(this));
		functors.put(RANDOM, new RandomFunctor(this));
	}
  
	public Object launchFunction(String name, Object argument) {
		if(functors.containsKey(name))
			return functors.get(name).execute(argument);
		return null;
	}
	
	public String respond(String input) {
		try {
			Response response = respond(transformations.makeRequest(input));
			if(response.isWholeExpressionAnEmptyString())
				return EMPTY_ANSWER;
			return response.getWholeExpression();
		} catch(RuntimeException ignored) {
			return ERROR_ANSWER;
		}
	}

	private Response respond(Request request) {
		if(request.isWholeExpressionAnEmptyString())
			return transformations.makeResponse("");
    
		Sentence that  = context.getSetting(THAT);
		Sentence topic = context.getSetting(TOPIC);
	  
		context.appendRequest(request);

		Response response = null;
		for(int i = 0; i != request.size(); i++) {
			if(response == null)
				response = respond(request.getSentence(i), that, topic);
			else
				response.merge(respond(request.getSentence(i), that, topic));
		}
		
		if(response != null && !response.isWholeExpressionAnEmptyString())
			context.appendResponse(response);

		return response;
	}

	private Response respond(Sentence sentence, Sentence that, Sentence topic) {
		if(sentence.getNormalizedWordsSize() > 0) {
			Match match = new Match(this, sentence, that, topic);
			Category category = graph.match(match);
			if(category != null)
				return transformations.makeResponse(category.process(match));			
		}
		
		return transformations.makeResponse("");
	}

	public Explorer getExplorer() {
		return explorer;
	}

	public Context getContext() {
		return context;
	}

	public Graph getGraph() {
		return graph;
	}

	public Transformations getTransformations() {
		return transformations;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}

	@Override
	public String toString() {
		return context.getProperty("name");
	}
}
