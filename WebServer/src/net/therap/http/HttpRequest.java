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

    public String requestMethod;
    public String requestURL;
    public String protocolVersion;
    public List<RequestHeader> requestHeaders;
    public List<RequestBody> contents;

    public HttpRequest (BufferedReader inFromClient) {
        this.inFromClient = inFromClient;
    }

    public void receiveRequest() throws IOException {
        getRequestHeadLine();
        getHeadersAndContentLength();
        getBody();
    }

    private void getRequestHeadLine() throws IOException {
        String inputLine = inFromClient.readLine();
        String[] parsedHeader = inputLine.split(" ");

        requestMethod = parsedHeader[0];
        requestURL = parsedHeader[1];
        protocolVersion = parsedHeader[2];
    }

    private void getHeadersAndContentLength() throws IOException{
        String inputLine;
        requestHeaders = new ArrayList<>();
        while ((inputLine = inFromClient.readLine()) != null) {
            if (inputLine.length() <= 0)
                break;

            RequestHeader requestHeader = getRequestHeaderFromLine(inputLine);
            requestHeaders.add(requestHeader);

            if (requestHeader.key.equals("Content-Length:")) {
                contentLength = Integer.parseInt(requestHeader.values.get(0));
            }
        }
    }

    private RequestHeader getRequestHeaderFromLine(String line) {
        RequestHeader requestHeader = new RequestHeader();
        String[] parsedLine = line.split(" ");
        requestHeader.key = parsedLine[0];
        requestHeader.values = new ArrayList<>();

        for (int loop=1; loop < parsedLine.length; ++loop)
            requestHeader.values.add(parsedLine[loop]);

        return requestHeader;
    }

    private void getBody() throws IOException {
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
            RequestBody requestBody = getRequestBodyFromLine(line);
            contents.add(requestBody);
        }
    }

    private RequestBody getRequestBodyFromLine(String line) {
        String[] parsedData = line.split("=");
        RequestBody requestBody = new RequestBody();
        requestBody.key = parsedData[0];
        requestBody.value = parsedData[1];

        return requestBody;
    }

}
