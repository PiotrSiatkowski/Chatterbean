package com.thetruthbeyond.chatterbean.aiml;

import com.thetruthbeyond.chatterbean.text.structures.Transformations;

/**
 * Created by Siata on 2015-05-30.
 * Superclass of all com.thetruthbeyond.chatterbean.aiml tags that need transformation in order to eliminate some user input mistakes.
 */
public interface NormalizedTag {
    void normalizeContent(Transformations tranformations);
}
