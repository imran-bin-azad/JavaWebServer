package net.therap.http;

import net.therap.http.FormData;
import net.therap.http.RequestHeader;

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

    public String requestMethod;
    public String requestURL;
    public String protocolVersion;
    public List<RequestHeader> requestHeaders;
    public List<FormData> formDataList;


    public void receiveRequest(BufferedReader inFromClient) throws IOException {

        requestHeaders = new ArrayList<>();
        int contentLength = 0;

        String inputLine;

        inputLine = inFromClient.readLine();
        getRequestHeadLine(inputLine);

        System.out.println(inputLine);

        while ((inputLine = inFromClient.readLine()) != null) {
            if (inputLine.length() == 0) {
                break;
            }

            System.out.println(inputLine);

            RequestHeader requestHeader = parseLine(inputLine);
            requestHeaders.add(requestHeader);

            if (requestHeader.key.equals("Content-Length:")) {
                contentLength = Integer.parseInt(requestHeader.values.get(0));
            }

        }

        if (contentLength > 0)
            formDataList = getBodyAsFormData(inFromClient, contentLength);

    }

    private void getRequestHeadLine(String header) {

        String[] parsedHeader = header.split(" ");

        requestMethod = parsedHeader[0];
        requestURL = parsedHeader[1];
        protocolVersion = parsedHeader[2];
    }

    private RequestHeader parseLine(String line) {

        RequestHeader requestHeader = new RequestHeader();
        String[] parsedLine = line.split(" ");

        requestHeader.key = parsedLine[0];

        requestHeader.values = new ArrayList<>();

        for (int loop=1; loop < parsedLine.length; ++loop)
            requestHeader.values.add(parsedLine[loop]);

        return requestHeader;

    }

    private List<FormData> getBodyAsFormData(BufferedReader inFromClient, int length) throws IOException {

        List<FormData> listOfFormData = new ArrayList<>();
        String formDataAsString = "";

        for (int loop=0; loop < length; ++loop) {
            formDataAsString += (char)inFromClient.read();
        }

        System.out.println(formDataAsString);

        String[] eachLine = formDataAsString.split("&");

        for (String data : eachLine) {

            String[] parsedData = data.split("=");
            FormData formData = new FormData();

            formData.key = parsedData[0];
            formData.value = parsedData[1];

            System.out.println(formData.key + " " + formData.value);

            listOfFormData.add(formData);
        }

        return listOfFormData;
    }


}

