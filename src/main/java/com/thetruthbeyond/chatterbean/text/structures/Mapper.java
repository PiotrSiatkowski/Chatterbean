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

package com.thetruthbeyond.chatterbean.text.structures;

import java.util.LinkedList;
import java.util.List;

class Mapper {

	private final List<Integer> mappings = new LinkedList<>();

	private int fl; // find length.
	private int rl; // replace length.

	private int indexOffset = 0;

	Mapper(String input) {
		for(int i = 0, n = input.length(); i < n; i++)
			mappings.add(i);
	}

    void prepare(String find, String replace) {
		fl = find.length();
		rl = replace.length();

		indexOffset = 0;
	}

    void updateMapping(int beginIndex) {
		if(fl - rl != 0) {
			int index = beginIndex + 1 + indexOffset;
			for(int n = fl - 1; n > 0; --n) {
				if(index - 1 >= 0 && mappings.get(index - 1) == null)
					mappings.set(index - 1, mappings.get(index));
				mappings.remove(index);
			}

			for(int n = rl - 1; n > 0; --n)
				mappings.add(index, null);
			indexOffset = indexOffset + rl - fl;
		}
	}

    Integer[] obtainMappings() {
		Integer[] table = new Integer[mappings.size()];

		for(int i = 0, n = mappings.size(); i != n; ++i)
			table[i] = mappings.get(i);
		return table;
	}
}
