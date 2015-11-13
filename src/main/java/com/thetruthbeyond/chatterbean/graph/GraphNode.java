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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thetruthbeyond.chatterbean.Match;
import com.thetruthbeyond.chatterbean.aiml.Category;

class GraphNode {

	/* The children of this node. */
	final Map<String, GraphNode> children = new HashMap<>(3);
	
	private Category category = null;

	/** The name of a node is the pattern element it represents. */
	private String name = null;

	/** Constructs a new root node. */
	GraphNode() {}
	
	private GraphNode(String name) {
		this.name = name;
	}
	
	public void append(Category category) {
		List<String> matchPath = category.getMatchPath();
		append(category, matchPath, 0);
	}
	
	private void append(Category category, List<String> path, int index) {
		GraphNode child = children.get(path.get(index));
		if(child == null) 
			appendChild(child = new GraphNode(path.get(index)));

		if(path.size() <= index + 1)
			child.category = category;
		else
			child.append(category, path, index + 1);
	}

	private void appendChild(GraphNode child) {
		children.put(child.name, child);
	}

	/**
	 * <p>
	 * Returns an array with three child nodes, in the following order:
	 * </p>
	 * <ul>
	 * <li>The "_" node;</li>
	 * <li>The node with the given name;</li>
	 * <li>The "*" node.</li>
	 * </ul>
	 * <p>
	 * If any of these nodes can not be found among this node's children, its
	 * position is filled by {@code null}.
	 * </p>
	 */
	private GraphNode[] children(String name) {
		return new GraphNode[] { children.get("_"), children.get(name), children.get("*") };
	}

	private boolean isWildcard() {
		return name.equals("_") || name.equals("*");
	}

	private Category match(Match match, int index) {
		if(isWildcard())
			return matchWildcard(match, index);

		if(!name.equals(match.getMatchPath(index)))
			return null;

		int nextIndex = index + 1;
		if(match.getMatchPathLength() <= nextIndex)
			return category;

		return matchChildren(match, nextIndex);
	}

	private Category matchChildren(Match match, int nextIndex) {
		GraphNode[] nodes = children(match.getMatchPath(nextIndex));
		for(GraphNode node : nodes) {
			Category category = node == null ? null : node.match(match, nextIndex);
			if(category != null)
				return category;
		}

		return null;
	}

	@SuppressWarnings("VariableNotUsedInsideIf")
	private Category matchWildcard(Match match, int index) {
		int n = match.getMatchPathLength();
		for(int i = index; i < n; i++) {
			Category category = matchChildren(match, i);
			if(category != null) {
				match.appendWildcard(index, i);
				return category;
			}
		}

		if(category != null)
			match.appendWildcard(index, n);
		return category;
	}

	/**
	 * Returns the Category which Pattern matches the given Sentence, or
	 * {@code null} if it cannot be found.
	 */
	public Category match(Match match) {
		return matchChildren(match, 0);
	}
}
