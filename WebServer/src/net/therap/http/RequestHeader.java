package net.therap.http;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: imran.azad
 * Date: 5/7/14
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestHeader {

    private String key;
    private List<String> values;

    public void parseRequestHeader(String line) {
        String[] parsedLine = line.split(" ");
        key = parsedLine[0];
        values = new ArrayList<>();

        for (int loop=1; loop < parsedLine.length; ++loop)
            values.add(parsedLine[loop]);
    }

    public boolean keyEqualsTo(String key) {
        return this.key.equals(key);
    }

    public List<String> getValues() {
        return values;
    }

}
