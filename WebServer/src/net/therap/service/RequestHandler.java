package net.therap.service;

import net.therap.http.HttpRequest;
import net.therap.http.HttpResponse;
import net.therap.http.StatusMessage;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: imran.azad
 * Date: 5/6/14
 * Time: 10:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class RequestHandler implements Runnable {

    private Socket clientSocket;
    private String rootDirectory;
    private HttpRequest httpRequest;
    private HttpResponse httpResponse;

    public RequestHandler(Socket clientSocket, String homeDirectory) {
        this.clientSocket = clientSocket;
        this.rootDirectory = homeDirectory;
    }

    @Override
    public void run() {
        System.out.println("NEW THREAD");
        talkToClient();
    }

    private void talkToClient() {
        try (
            InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
            BufferedReader inFromClient = new BufferedReader(inputStreamReader);
            PrintWriter outToClient = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            httpRequest = new HttpRequest(inFromClient);
            httpRequest.receiveRequest();

            System.out.println("\nTime to RESPOND!!\n");

            httpResponse = new HttpResponse(outToClient);
            sendResponseBasedOnRequestMethod();

            System.out.println("\nResponse sent\n");

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void sendResponseBasedOnRequestMethod() {
        if (httpRequest.requestMethod.equals("GET")) {
            respondToGetRequests();
        } else if (httpRequest.requestMethod.equals("POST")) {
            respondToPostRequests();
        } else {
            httpResponse.sendErrorMessage(StatusMessage.BadRequest, httpRequest.protocolVersion);
        }
    }

    private void respondToGetRequests() {
        File requestedContent = new File(rootDirectory + httpRequest.requestURL);

        if (!requestedContent.exists()) {
            httpResponse.sendErrorMessage(StatusMessage.NotFound, httpRequest.protocolVersion);
        } else {
            httpResponse.sendOKMessage(httpRequest.protocolVersion);
            sendResponseOnRequestedContent(requestedContent);
        }
    }

    private void sendResponseOnRequestedContent(File requestedFile) {
        if ( httpRequest.requestURL.equals("/") ) {
            sendDefaultResponse();
        } else if ( requestedFile.isDirectory() ) {
            sendDefaultResponse();
        } else {
            httpResponse.sendResponse(requestedFile);
        }
    }

    private void sendDefaultResponse() {
        httpResponse.sendResponse(new File(rootDirectory + "/login.html"));
    }

    private void respondToPostRequests() {
        httpResponse.sendOKMessage(httpRequest.protocolVersion);
        httpResponse.sendResponse(new File(rootDirectory + httpRequest.requestURL));
    }

}
