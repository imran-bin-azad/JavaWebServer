package net.therap.http;

/**
 * Created with IntelliJ IDEA.
 * User: imran.azad
 * Date: 5/7/14
 * Time: 1:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestBody {

    private String key;
    private String value;

    public void parseRequestBody(String line) {
        String[] parsedData = line.split("=");
        key = parsedData[0];
        value = parsedData[1];
    }

    public boolean keyEqualsTo(String key) {
        return this.key.equals(key);
    }

    public String getValue() {
        return value;
    }

}
