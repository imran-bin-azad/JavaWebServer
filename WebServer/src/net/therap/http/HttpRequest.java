package net.therap.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: imran.azad
 * Date: 5/7/14
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class HttpRequest {

    private BufferedReader inFromClient;
    private int contentLength;
    private RequestLine requestLine;
    private List<RequestHeader> requestHeaders;
    private List<RequestBody> contents;

    public HttpRequest (BufferedReader inFromClient) {
        this.inFromClient = inFromClient;
    }

    public void receiveRequest() throws IOException {
        receiveRequestLine();
        receiveHeadersAndContentLength();
        receiveContents();
    }

    private void receiveRequestLine() throws IOException {
        String inputLine = inFromClient.readLine();
        requestLine = new RequestLine();
        requestLine.parseRequestHeadLine(inputLine);
    }

    private void receiveHeadersAndContentLength() throws IOException{
        String inputLine;
        requestHeaders = new ArrayList<>();
        while ((inputLine = inFromClient.readLine()) != null) {
            if (inputLine.length() <= 0)
                break;

            RequestHeader requestHeader = new RequestHeader();
            requestHeader.parseRequestHeader(inputLine);
            requestHeaders.add(requestHeader);

            if (requestHeader.keyEqualsTo("Content-Length:")) {
                List<String> values = requestHeader.getValues();
                contentLength = Integer.parseInt(values.get(0));
            }
        }
    }

    private void receiveContents() throws IOException {
        if (contentLength <= 0)
            return;

        contents = new ArrayList<>();
        String wholeBody = "";

        int length = contentLength;
        for (int loop=0; loop < length; ++loop) {
            wholeBody += (char)inFromClient.read();
        }

        String[] body = wholeBody.split("&");
        for (String line : body) {
            RequestBody requestBody = new RequestBody();
            requestBody.parseRequestBody(line);
            contents.add(requestBody);
        }
    }

    public String getRequestMethod() {
        return requestLine.getRequestMethod();
    }

    public String getRequestURL() {
        return requestLine.getRequestURL();
    }

    public String getProtocolVersion() {
        return requestLine.getProtocolVersion();
    }

}
