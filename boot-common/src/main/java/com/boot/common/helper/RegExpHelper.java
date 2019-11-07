package com.boot.common.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Regexp Helper class
 * contains reg expression matching method
 */
public abstract class RegExpHelper {

    /**
     * regexp match
     *
     * @param regExp regexp, for example: "[0-9]"
     * @param target target String
     * @return true if matched else false
     */
    public static boolean match(String regExp, CharSequence target) {
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(target);
        return m.matches();
    }
}
