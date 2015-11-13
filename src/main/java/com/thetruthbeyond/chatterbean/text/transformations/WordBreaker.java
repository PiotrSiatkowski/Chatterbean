package com.thetruthbeyond.chatterbean.text.transformations;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.thetruthbeyond.chatterbean.text.transformations.Escaper.escapeRegex;

/**
 * Created by Siata on 2015-04-17.
 *
 */
public class WordBreaker {

    private static final String WORD_SEPARATOR = " ";

    /** The regular expression which will break entries by word breaker. */
    private final Pattern breakPattern;

    public WordBreaker(List<String> breakers) {
        StringBuilder pattern = new StringBuilder("(");
        for(String breaker : breakers) {
            pattern.append(escapeRegex(breaker));
            pattern.append("|");
        }

        if(!breakers.isEmpty())
            pattern.delete(pattern.length() - 1, pattern.length());
        pattern.append(")([A-Za-z0-9]|\\s{2,})");

        breakPattern = Pattern.compile(pattern.toString());
    }

    @SuppressWarnings("StringContatenationInLoop")
    public String breakWords(String input) {
		/*
		 * See the description of java.util.regex.Matcher.appendReplacement() in
		 * the Javadocs to understand this code.
		 */
        StringBuffer result = new StringBuffer(input.length());

        Matcher matcher = breakPattern.matcher(input);
        while(matcher.find()) {
            String replace = matcher.group(2);
            if(replace.startsWith(WORD_SEPARATOR))
                replace = matcher.group(1) + WORD_SEPARATOR + replace;
            else
                replace = matcher.group(1) + WORD_SEPARATOR;

            matcher.appendReplacement(result, replace);
        }

        matcher.appendTail(result);
        return result.toString();
    }
}
