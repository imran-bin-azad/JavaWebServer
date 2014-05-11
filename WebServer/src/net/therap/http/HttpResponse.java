package net.therap.http;

import net.therap.http.StatusMessage;

import java.io.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: imran.azad
 * Date: 5/7/14
 * Time: 12:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpResponse {

    PrintWriter outToClient;

    public HttpResponse(PrintWriter outToClient) {
        this.outToClient = outToClient;
    }

    public void sendOKMessage(String protocolVersion) {
        outToClient.println(protocolVersion + " 200 OK\r");
        outToClient.println("Content-Type: " + "text/html" + "\r");
        outToClient.println("Date: " + new Date() + "\r");
        outToClient.println("\r");

        outToClient.flush();
    }

    public void sendErrorMessage(StatusMessage statusMessage, String protocolVersion) {
        outToClient.println(protocolVersion + " " + statusMessage.number + " " + statusMessage.message + "\r");
        outToClient.println("\r");

        outToClient.println("<!DOCTYPE HTML>\r");
        outToClient.println("<HEAD>\r");
        outToClient.println("<TITLE>" + statusMessage.number + " " + statusMessage.message + "</TITLE>\r");
        outToClient.println("</HEAD><BODY>\r");
        outToClient.println("<H1>" + statusMessage.number + "</H1>\r" + statusMessage.message + "<P>");
        outToClient.println("<HR></BODY></HTML>\r");

        outToClient.flush();
    }


    public void sendResponse(File requestedFile) {
        System.out.println("Requested File: "+requestedFile.getName());

        try (
                FileInputStream file = new FileInputStream(requestedFile);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file));
        ) {
            String line;
            while ((line=bufferedReader.readLine())!=null) {
                outToClient.println(line);
            }

            outToClient.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
