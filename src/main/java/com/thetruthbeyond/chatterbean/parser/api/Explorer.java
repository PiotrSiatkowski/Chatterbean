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

package com.thetruthbeyond.chatterbean.parser.api;

import com.thetruthbeyond.chatterbean.Context;

import java.io.InputStream;

/**
 * Interface used to find bot's files in the user's file system.
 */
public interface Explorer {
    void updatePredicatesFile(Context context);

    InputStream getConfiguration() throws AliceBotExplorerException;
    InputStream getProperties() throws AliceBotExplorerException;
    InputStream getPredicates() throws AliceBotExplorerException;
    InputStream getPredicates(String userName) throws AliceBotExplorerException;
    InputStream getSplitters() throws AliceBotExplorerException;
    InputStream getSubstitutions() throws AliceBotExplorerException;
    InputStream getAIML(String topic) throws AliceBotExplorerException;
    InputStream[] getAIMLS() throws AliceBotExplorerException;
}
