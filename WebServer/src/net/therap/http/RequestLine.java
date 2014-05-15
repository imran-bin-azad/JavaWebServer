package net.therap.http;

/**
 * Created with IntelliJ IDEA.
 * User: imran.azad
 * Date: 5/15/14
 * Time: 12:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class RequestLine {

    private String requestMethod;
    private String requestURL;
    private String protocolVersion;

    public void parseRequestHeadLine(String inputLine) {
        String[] parsedHeader = inputLine.split(" ");

        requestMethod = parsedHeader[0];
        requestURL = parsedHeader[1];
        protocolVersion = parsedHeader[2];
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

}
