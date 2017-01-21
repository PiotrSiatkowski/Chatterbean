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

package com.thetruthbeyond.chatterbean.graph;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.thetruthbeyond.chatterbean.Match;
import com.thetruthbeyond.chatterbean.aiml.Category;
import com.thetruthbeyond.chatterbean.aiml.Text;

public class Graph {

	private final Category DEFAULT_CATEGORY;

	private final GraphNode root = new GraphNode();
	private final Set<String> patterns = new TreeSet<>();
	
	/** Constructs a new empty com.thetruthbeyond.chatterbean.graph. */
	public Graph() {
		DEFAULT_CATEGORY = new Category("*", new Text("I am sorry, my answers are limited. You must provide the right questions."));
		append(DEFAULT_CATEGORY);
	}
	
	/** Constructs a new tree, with this node as the root. */  
	public Graph(List<Category> categories) {
		DEFAULT_CATEGORY = new Category("*", new Text("I am sorry, my answers are limited. You must provide the right questions."));
		append(DEFAULT_CATEGORY);
		append(categories);
	}
	  
	public final void append(List<Category> categories) {
		for(Category category : categories)
			append(category);
	}

	public void append(Category category) {
		root.append(category);
		patterns.add(category.getPattern().toString());
	}
	
	public Category match(Match match) {
		return root.match(match);
	}
	
	public boolean hasCategory(List<String> path) {
		GraphNode node = root;

		for(String element : path) {
			node = node.children.get(element);
			if(node == null)
				return false;
		}

		return true;
	}
	
	public Set<String> getAllPatterns() {
		return Collections.unmodifiableSet(patterns);
	}
}
