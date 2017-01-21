package com.thetruthbeyond.console;

import com.thetruthbeyond.chatterbean.parser.api.AliceBotExplorerException;
import com.thetruthbeyond.chatterbean.parser.api.Explorer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by Peter Siatkowski on 20.01.17.
 * Contact me on siatkowski.p@gmail.com or find me on facebook.
 */
class WorkingDirectoryExplorer extends Explorer {
    @Override
    public InputStream getConfiguration() throws AliceBotExplorerException {
        try {
            return Files.newInputStream(Paths.get("configuration.xml"), StandardOpenOption.READ);
        } catch (IOException exception) {
            throw new AliceBotExplorerException(exception.getMessage());
        }
    }

    @Override
    public InputStream getProperties() throws AliceBotExplorerException {
        try {
            return Files.newInputStream(Paths.get("properties.xml"), StandardOpenOption.READ);
        } catch (IOException exception) {
            throw new AliceBotExplorerException(exception.getMessage());
        }
    }

    @Override
    public InputStream getPredicates() throws AliceBotExplorerException {
        try {
            return Files.newInputStream(Paths.get("Predicates/Default_predicates.xml"), StandardOpenOption.READ);
        } catch (IOException exception) {
            throw new AliceBotExplorerException(exception.getMessage());
        }
    }

    @Override
    public InputStream getPredicates(String userName) throws AliceBotExplorerException {
        try {
            return Files.newInputStream(Paths.get("Predicates/" + userName + ".xml"), StandardOpenOption.READ);
        } catch (IOException exception) {
            throw new AliceBotExplorerException(exception.getMessage());
        }
    }

    @Override
    public InputStream getSplitters() throws AliceBotExplorerException {
        try {
            return Files.newInputStream(Paths.get("splitters.xml"), StandardOpenOption.READ);
        } catch (IOException exception) {
            throw new AliceBotExplorerException(exception.getMessage());
        }
    }

    @Override
    public InputStream getSubstitutions() throws AliceBotExplorerException {
        try {
            return Files.newInputStream(Paths.get("substitutions.xml"), StandardOpenOption.READ);
        } catch (IOException exception) {
            throw new AliceBotExplorerException(exception.getMessage());
        }
    }

    @Override
    public InputStream getAIML(String topic) throws AliceBotExplorerException {
        try {
            return Files.newInputStream(Paths.get("AIML/" + topic + ".xml"), StandardOpenOption.READ);
        } catch (IOException exception) {
            throw new AliceBotExplorerException(exception.getMessage());
        }
    }

    @Override
    public InputStream[] getAIMLS() throws AliceBotExplorerException {
        try {
            String[] names = Paths.get("AIML").toFile().list();

            InputStream[] streams = new InputStream[names.length];
            for (int i = 0; i != names.length; i++) {
                streams[i] = Files.newInputStream(Paths.get("AIML/" + names[i]));
            }

            return streams;
        } catch (IOException exception) {
            throw new AliceBotExplorerException(exception.getMessage());
        }
    }

    @Override
    protected OutputStream setPredicates() throws AliceBotExplorerException {
        try {
            return Files.newOutputStream(Paths.get("Predicates/Default_predicates.xml"), StandardOpenOption.READ);
        } catch (IOException exception) {
            throw new AliceBotExplorerException(exception.getMessage());
        }
    }

    @Override
    protected OutputStream setPredicates(String userName) throws AliceBotExplorerException {
        try {
            return Files.newOutputStream(Paths.get("Predicates/" + userName + ".xml"), StandardOpenOption.READ);
        } catch (IOException exception) {
            throw new AliceBotExplorerException(exception.getMessage());
        }
    }
}
