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
            System.out.println("Receiving Client Request");
            httpRequest = new HttpRequest(inFromClient);
            httpRequest.receiveRequest();

            System.out.println("Time to RESPOND!!");
            httpResponse = new HttpResponse(outToClient);
            sendResponseBasedOnRequestMethod();

            System.out.println("Response sent");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void sendResponseBasedOnRequestMethod() {
        String requestMethod = httpRequest.getRequestMethod();
        String protocolVersion = httpRequest.getProtocolVersion();

        if (requestMethod.equals("GET")) {
            respondToGetRequests();
        } else if (requestMethod.equals("POST")) {
            respondToPostRequests();
        } else {
            httpResponse.sendErrorMessage(StatusMessage.BadRequest, protocolVersion);
        }
    }

    private void respondToGetRequests() {
        String requestURL = httpRequest.getRequestURL();
        String protocolVersion = httpRequest.getProtocolVersion();
        File requestedContent = new File(rootDirectory + requestURL);

        if (!requestedContent.exists()) {
            httpResponse.sendErrorMessage(StatusMessage.NotFound, protocolVersion);
        } else {
            httpResponse.sendOKMessage(protocolVersion);
            sendResponseOnRequestedContent(requestedContent);
        }
    }

    private void sendResponseOnRequestedContent(File requestedFile) {
        String requestURL = httpRequest.getRequestURL();

        if (requestURL.equals("/")) {
            sendDefaultResponse();
        } else if (requestedFile.isDirectory()) {
            sendDefaultResponse();
        } else {
            httpResponse.sendResponse(requestedFile);
        }
    }

    private void sendDefaultResponse() {
        httpResponse.sendResponse(new File(rootDirectory + "/login.html"));
    }

    private void respondToPostRequests() {
        String requestURL = httpRequest.getRequestURL();
        String protocolVersion = httpRequest.getProtocolVersion();
        httpResponse.sendOKMessage(protocolVersion);
        httpResponse.sendResponse(new File(rootDirectory + requestURL));
    }

}
