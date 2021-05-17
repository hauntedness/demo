package org.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        boolean found = this.matcher.find();
        if (this.matcher.groupCount() > 0) {
            return this.matcher.group(1);
        } else if (found) {
            return this.matcher.group(1);

        } else {
            return "";
        }
    }

    public String findGroup(String reg, int group) {
        this.matcher(reg);
        boolean found = this.matcher.find();
        if (this.matcher.groupCount() >= group) {
            return this.matcher.group(group);
        } else {
            return "";
        }
    }

    public String findGroup(String reg, String group) {
        this.matcher(reg);
        this.matcher.find();
        String group1 = "";
        try {
            group1 = this.matcher.group(group);
            return group1;
        } catch (Exception e) {
            Logger.logger.error(e.getMessage());
            Logger.logger.error("group: \"" + group + "\" not found in: \n" + this.text + "\nwith: " + reg);
        }
        return group1;
    }

    public boolean matches(String reg) {
        this.matcher(reg);
        return this.matcher.matches();
    }

    public boolean find(String reg) {
        this.matcher(reg);
        return this.matcher.find();
    }


    @Override
    public String toString() {
        return text;
    }


    public static void main(String[] args) {
        String TimeForPhase = "(?<value>Total time = (?<seconds>\\d+\\.?\\d+ seconds))";

        String text = "Total time = 33122.480 seconds. CPU (76.690%) Sat May 15 05:34:25 2021";

        String m = Regexp.with(text).findGroup(TimeForPhase, "value");
        String m2 = Regexp.with(text).findGroup(TimeForPhase, "seconds");

        System.out.println("M: " + m);
        System.out.println(m2);

    }

}
