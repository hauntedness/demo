package org.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author YouXianMing1987@iCloud.com 用于简化处理正则表达式
 */
public class Regexp {

    private String text;
    private Pattern pattern;
    private Matcher matcher;

    private Regexp(String text) {
        this.text = text;
    }

    private void matcher(String reg) {
        this.pattern = Pattern.compile(reg, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        this.matcher = this.pattern.matcher(this.text);
    }

    public static Regexp with(String text) {
        return new Regexp(text);
    }

    public String findFirst(String reg) {
        this.matcher(reg);
        if (this.matcher.find()) {
            return this.matcher.group();
        } else {
            return " ";
        }
    }

    public boolean matches(String reg) {
        this.matcher(reg);
        return this.matcher.matches();
    }

    public boolean find(String reg) {
        this.matcher(reg);
        return  this.matcher.find();
    }

    @Override
    public String toString() {
        return text;
    }
}
