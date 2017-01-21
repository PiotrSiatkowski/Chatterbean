package com.thetruthbeyond.console;

import com.thetruthbeyond.chatterbean.AliceBot;
import com.thetruthbeyond.chatterbean.parser.api.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Peter Siatkowski on 20.01.17.
 * Contact me on siatkowski.p@gmail.com or find me on facebook.
 */
@SuppressWarnings("InfiniteLoopStatement")
public class ConsoleClient {

    public static void main(String[] args) {
        try {
            // Bot creation.
            BotFactory factory = new BotFactory();
            AliceBot bot = factory.createAliceBot("Jason", new WorkingDirectoryExplorer());

            // Infinite query loop.
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String question = reader.readLine();
                if (question != null) {
                    String answer = bot.respond(question);
                    System.out.println(answer);
                }
            }
        } catch (AliceBotParserConfigurationException | AliceBotParserException | AliceBotExplorerException | IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

}
