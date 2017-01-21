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

import com.thetruthbeyond.chatterbean.AliceBot;
import com.thetruthbeyond.chatterbean.Context;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import com.thetruthbeyond.chatterbean.text.structures.Sentence;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Interface used to find bot's files in the user's file system.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class Explorer {

    abstract public InputStream getConfiguration() throws AliceBotExplorerException;
    abstract public InputStream getProperties() throws AliceBotExplorerException;
    abstract public InputStream getPredicates() throws AliceBotExplorerException;
    abstract public InputStream getPredicates(String userName) throws AliceBotExplorerException;
    abstract public InputStream getSplitters() throws AliceBotExplorerException;
    abstract public InputStream getSubstitutions() throws AliceBotExplorerException;
    abstract public InputStream getAIML(String topic) throws AliceBotExplorerException;
    abstract public InputStream[] getAIMLS() throws AliceBotExplorerException;

    abstract protected OutputStream setPredicates() throws AliceBotExplorerException;
    abstract protected OutputStream setPredicates(String userName) throws AliceBotExplorerException;

    public final void updatePredicatesFile(Context context) throws AliceBotExplorerException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("context");

            Set<String> predicates = context.getPredicatesNames();

            String indent = "  ";

            root.appendChild(document.createTextNode("\n" + indent));
            root.appendChild(document.createComment("Default values for predicates, can be changed later at runtime."));

            for(String predicate : predicates) {
                Element element = document.createElement("set");
                element.setAttribute("name", predicate);
                element.setAttribute("value", context.getPredicate(predicate));

                root.appendChild(document.createTextNode("\n" + indent));
                root.appendChild(element);
            }

            root.appendChild(document.createTextNode("\n"));
            document.appendChild(root);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(document);

            // Write predicates to file connected with the current user.
            Sentence user = context.getSetting(AliceBot.USER);

            StreamResult result;
            if(user.isEmpty() || user.equals(Sentence.ASTERISK))
                result = new StreamResult(setPredicates());
            else {
                String userName = user.getOriginal();
                result = new StreamResult(setPredicates(userName));
            }

            transformer.transform(source, result);
        } catch(Exception exception) {
            throw new AliceBotExplorerException(exception.getMessage());
        }
    }

}
