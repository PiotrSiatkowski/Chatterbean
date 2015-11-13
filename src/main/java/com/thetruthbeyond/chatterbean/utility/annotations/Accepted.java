package com.thetruthbeyond.chatterbean.utility.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Siata on 2015-08-20.
 * Annotate classes that have been accepted by a programmer as valid ones.
 */

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
public @interface Accepted {}
